package iwo.wintech.orderservice.order.client;

import iwo.wintech.orderservice.order.domain.book.Book;
import reactor.core.publisher.Mono;

public interface BookApiClient {
    Mono<Book> getBookByIsbn(String isbn);
}
