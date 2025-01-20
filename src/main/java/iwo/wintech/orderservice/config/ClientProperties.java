package iwo.wintech.orderservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URI;
import java.time.Duration;

@ConfigurationProperties(prefix = "polar")
public record ClientProperties(
        URI catalogServiceUri,
        Duration catalogServiceClientTimeout
) {
}
