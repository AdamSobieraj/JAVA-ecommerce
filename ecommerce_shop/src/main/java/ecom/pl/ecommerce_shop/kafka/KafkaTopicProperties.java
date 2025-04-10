package ecom.pl.ecommerce_shop.kafka;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "kafka")
public class KafkaTopicProperties {

  private List<Topic> topics;

  @Setter
  @Getter
  public static class Topic {
    private String name;
    private boolean enabled;
    private String issuer;
    private String groupId;

  }
}