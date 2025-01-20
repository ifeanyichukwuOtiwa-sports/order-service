package iwo.wintech.orderservice.order.repo;

import iwo.wintech.orderservice.config.DataConfig;
import iwo.wintech.orderservice.order.domain.Order;
import iwo.wintech.orderservice.order.domain.OrderStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import reactor.test.StepVerifier;


@DataR2dbcTest
@Import(DataConfig.class)
@Testcontainers
class OrderRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:14.4"));

    @DynamicPropertySource
    static void registerProperties(final DynamicPropertyRegistry reg){
        reg.add("spring.r2dbc.url", OrderRepositoryTest::r2dbcUrl);
        reg.add("spring.r2dbc.username", postgresContainer::getUsername);
        reg.add("spring.r2dbc.password", postgresContainer::getPassword);
        reg.add("spring.flyway.url", postgresContainer::getJdbcUrl);
    }

    private static String r2dbcUrl() {
        return "r2dbc:postgresql://%s:%s/%s".formatted(
                postgresContainer.getHost(),
                postgresContainer.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT),
                postgresContainer.getDatabaseName()
        );
    }

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void createRejectedOrder() {
        final Order rejected = Order.ofRejected("1234567890", 3);
        StepVerifier.create(orderRepository.save(rejected))
                .expectNextMatches(order -> order.status() == OrderStatus.REJECTED)
                .verifyComplete();
    }
}