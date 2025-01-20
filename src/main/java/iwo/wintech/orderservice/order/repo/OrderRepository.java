package iwo.wintech.orderservice.order.repo;

import iwo.wintech.orderservice.order.domain.Order;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;

public interface OrderRepository extends ReactiveSortingRepository<Order, Long>, ReactiveCrudRepository<Order, Long> {
}
