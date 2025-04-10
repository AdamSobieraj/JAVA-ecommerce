package ecom.pl.ecommerce_shop.kafka;

import ecom.pl.ecommerce_shop.database.DataImport;
import jakarta.annotation.PostConstruct;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class KafkaDynamicListenerService {

  private final DefaultKafkaConsumerFactory<String, byte[]> consumerFactory;
  private final KafkaTopicProperties topicProperties;
  private final ImportService importService;

  @Value("${kafka.group-id}")
  private String groupId;

  public KafkaDynamicListenerService(DefaultKafkaConsumerFactory<String, byte[]> consumerFactory,
      KafkaTopicProperties topicProperties, ImportService importService) {
    this.consumerFactory = consumerFactory;
    this.topicProperties = topicProperties;
    this.importService = importService;
  }

  @PostConstruct
  public void initializeListeners() {
    topicProperties.getTopics().forEach(topic -> {
      if (topic.isEnabled()) {
        createListenerForTopic(topic);
      } else {
        System.out.println("Skipping listener for disabled topic: " + topic.getName());
      }
    });
  }

  private void createListenerForTopic(KafkaTopicProperties.Topic topic) {
    ContainerProperties containerProps = new ContainerProperties(topic.getName());

    containerProps.setGroupId(groupId);

    DefaultKafkaConsumerFactory<String, byte[]> byteArrayConsumerFactory =
            new DefaultKafkaConsumerFactory<>(
                    consumerFactory.getConfigurationProperties(), // Inherit default configs
                    new StringDeserializer(), // Key deserializer
                    new ByteArrayDeserializer() // Value deserializer for this listener
            );

    containerProps.setMessageListener((MessageListener<String, byte[]>) record -> {

      Map<String, Object> headers = new HashMap<>();
      record.headers().forEach(header -> headers.put(header.key(), new String(header.value())));

      headers.put("kafka_receivedPartitionId", record.partition());
      headers.put("kafka_receivedTimestamp", record.timestamp());
      headers.put("kafka_receivedTopic", record.topic());
      headers.put("kafka_offset", record.offset());

      Message<byte[]> message = new GenericMessage<>(record.value(), headers);

      DataImport rawDataImport = DataImport.builder()
          .id(UUID.randomUUID())
          .timestamp(LocalDateTime.now())
          .status(Status.IMPORTED)
          .issuer(topic.getIssuer())
          .build();

      // save to db
    });

    KafkaMessageListenerContainer< String, byte[]> container =
        new KafkaMessageListenerContainer<>(byteArrayConsumerFactory, containerProps);
    container.start();
  }

}
