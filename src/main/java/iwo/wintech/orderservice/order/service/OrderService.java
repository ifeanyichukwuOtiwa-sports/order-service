package iwo.wintech.orderservice.order.service;

import iwo.wintech.orderservice.order.domain.Order;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrderService {
    Flux<Order> getAllOrders();
    Mono<Order> submitOrder(String isbn, int quantity);
}
