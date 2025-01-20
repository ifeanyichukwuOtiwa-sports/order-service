package iwo.wintech.orderservice.order.rest;

import iwo.wintech.orderservice.order.domain.Order;
import iwo.wintech.orderservice.order.domain.OrderStatus;
import iwo.wintech.orderservice.order.dto.OrderRequest;
import iwo.wintech.orderservice.order.service.OrderService;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@WebFluxTest(controllers = OrderController.class)
class OrderControllerWebFluxTest {

    @Autowired
    WebTestClient webTestClient;

    @MockitoBean
    private OrderService orderService;

    @Test
    void submitOrder() {
        final String isbn = "1234567890";
        final int quantity = 3;
        var request = new OrderRequest(isbn, quantity);
        var expectedResponse = Order.ofRejected(isbn, quantity);

        BDDMockito.given(orderService.submitOrder(isbn, quantity))
                .willReturn(Mono.just(expectedResponse));

        webTestClient.post()
                .uri("/orders")
                .bodyValue(request)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Order.class).value(order -> {
                    assertThat(order).isNotNull();
                    assertThat(order.status()).isEqualTo(OrderStatus.REJECTED);
                })
                .isEqualTo(expectedResponse);
    }
}