package iwo.wintech.orderservice.order.service;

import iwo.wintech.orderservice.order.client.BookApiClient;
import iwo.wintech.orderservice.order.domain.Order;
import iwo.wintech.orderservice.order.domain.OrderStatus;
import iwo.wintech.orderservice.order.domain.book.Book;
import iwo.wintech.orderservice.order.repo.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final BookApiClient bookApiClient;

    @Override
    public Flux<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Mono<Order> submitOrder(final String isbn, final int quantity) {
        final Order ofRejected = Order.ofRejected(isbn, quantity);

        return bookApiClient.getBookByIsbn(isbn)
                .map(book -> buildAcceptedOrder(book, quantity))
                .defaultIfEmpty(ofRejected)
                .flatMap(orderRepository::save);
    }

    private Order buildAcceptedOrder(final Book book, final int quantity) {
        return Order.of(book.isbn(), book.title() + "-" + book.author(), book.price(), quantity, OrderStatus.ACCEPTED);
    }

}
