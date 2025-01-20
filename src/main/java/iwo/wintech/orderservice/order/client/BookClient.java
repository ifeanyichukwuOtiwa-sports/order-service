package iwo.wintech.orderservice.order.client;

import iwo.wintech.orderservice.order.domain.book.Book;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Component
@Slf4j
public class BookClient implements BookApiClient {
    private static final String BOOK_BASE_URL = "/books/";
    private final WebClient webClient;
    @Value("${polar.catalog-service-client-timeout:PT3S}")
    private final Duration timeout;

    public BookClient(final WebClient webClient, @Value("${polar.catalog-service-client-timeout:PT3S}") final Duration timeout) {
        this.webClient = webClient;
        this.timeout = timeout;
    }


    @Override
    public Mono<Book> getBookByIsbn(final String isbn) {
        log.info("Fetching book by ISBN: {}", isbn);
        return webClient.get()
                .uri(BOOK_BASE_URL + isbn)
                .retrieve()
                .bodyToMono(Book.class)
                .timeout(timeout, Mono.empty())
                .onErrorResume(WebClientResponseException.NotFound.class, e -> {
                    log.warn("Book not found");
                    return Mono.empty();
                })
                .retryWhen(Retry.backoff(3, Duration.ofMillis(100)))
                .onErrorResume(Exception.class,  e -> Mono.empty());
    }
}
