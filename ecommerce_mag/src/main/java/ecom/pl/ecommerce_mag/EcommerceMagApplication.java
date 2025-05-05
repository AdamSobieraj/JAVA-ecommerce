package ecom.pl.ecommerce_mag;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ecom.pl.ecommerce_mag.config.KafkaProducerBuilder;
import ecom.pl.ecommerce_mag.dto.ProductDto;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
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
            boolean loop = true;
            while (loop) {
                for (Map.Entry<String, String> entry : topicSources.entrySet()) {
                    String topic = entry.getKey();
                    String source = entry.getValue();

                    if ("file".equals(source)) {
                        byte[] fileContent = getProducts();
                        ProducerRecord<String, byte[]> record = new ProducerRecord<>(topic, null, fileContent);
                        producer.send(record);
                        System.out.println("Sent binary file to " + topic);
                        loop = false;
                    } else {
                        String message = source + " - Message number " + i;
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

    public static byte [] getProducts() {
        ObjectMapper objectMapper = new ObjectMapper();
        InputStream inputStream = EcommerceMagApplication.class.getResourceAsStream("/data/products.json");

        byte[] data;
        try {
            List<ProductDto> products = objectMapper.readValue(inputStream, new TypeReference<List<ProductDto>>() {});
            data = objectMapper.writeValueAsBytes(products);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return data;
    }

}
