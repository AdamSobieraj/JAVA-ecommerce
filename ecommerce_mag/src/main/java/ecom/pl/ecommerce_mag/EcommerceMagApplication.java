package ecom.pl.ecommerce_mag;

import ecom.pl.ecommerce_mag.config.KafkaProducerBuilder;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class EcommerceMagApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcommerceMagApplication.class, args);
        Map<String, String> topicSources = prepareTopicSources();

        String bootstrapServers = "localhost:9092";
        Producer<String, byte[]> producer = KafkaProducerBuilder.createProducer(bootstrapServers);

        try {
            int i = 0;
            while (true) {
                for (Map.Entry<String, String> entry : topicSources.entrySet()) {
                    String topic = entry.getKey();
                    String source = entry.getValue();

                    if ("file".equals(source)) {
                        byte[] fileContent = "</br>to jest test płaśli siężźęł</br>".getBytes();
                        ProducerRecord<String, byte[]> record = new ProducerRecord<>(topic, null, fileContent);
                        producer.send(record);
                        System.out.println("Sent binary file to " + topic);
                    } else {
                        String message = source + " - Message number " + i;
                        System.out.println("DATA 22 : " + message.getBytes().length);
                        ProducerRecord< String,byte[]> record = new ProducerRecord<>(topic, null, message.getBytes());
                        producer.send(record);
                        System.out.println("Sent to " + topic + ": " + message);
                    }
                }
                i++;
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            producer.close();
        }


    }

    private static Map<String, String> prepareTopicSources() {
        Map<String, String> topicSources = new HashMap<>();

        boolean prepareTest = false;

        if (prepareTest) {
            topicSources.put("topic1", "Source1");
            topicSources.put("topic2", "Source2");
            topicSources.put("topic3", "Source3");
        } else {
            topicSources.put("topic4", "file");
        }

        return topicSources;

    }
}
