package iwo.wintech.orderservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientConfiguration {

    @Bean
    public WebClient webClient(final ClientProperties props, final WebClient.Builder builder) {
        return builder.baseUrl(props.catalogServiceUri().toString())
                .build();
    }
}
