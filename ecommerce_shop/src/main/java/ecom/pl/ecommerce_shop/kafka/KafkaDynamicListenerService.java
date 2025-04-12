package ecom.pl.ecommerce_shop.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import ecom.pl.ecommerce_shop.product.ProductDto;
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

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class KafkaDynamicListenerService {

  private final DefaultKafkaConsumerFactory<String, byte[]> consumerFactory;
  private final KafkaTopicProperties topicProperties;
  private final MagUpdateService magUpdateService;

  @Value("${kafka.group-id}")
  private String groupId;

  public KafkaDynamicListenerService(DefaultKafkaConsumerFactory<String, byte[]> consumerFactory,
      KafkaTopicProperties topicProperties, MagUpdateService magUpdateService) {
    this.consumerFactory = consumerFactory;
    this.topicProperties = topicProperties;
    this.magUpdateService = magUpdateService;
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

      Message<byte[]> message = new GenericMessage<>(record.value(), headers);

      ObjectMapper objectMapper = new ObjectMapper();

      List<ProductDto> products;

      try {
          products = objectMapper.readValue(message.getPayload(), objectMapper.getTypeFactory()
                  .constructCollectionType(List.class, ProductDto.class));
      } catch (IOException e) {
          throw new RuntimeException(e);
      }

      products.forEach(product -> {
        System.out.println("Received product: " + product.getName() + ", Price: " + product.getPrice());
      });

      // save to db
      magUpdateService.updateMag(products);

    });

    KafkaMessageListenerContainer< String, byte[]> container =
        new KafkaMessageListenerContainer<>(byteArrayConsumerFactory, containerProps);
    container.start();
  }

}
