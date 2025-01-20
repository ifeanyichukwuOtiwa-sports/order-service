package iwo.wintech.orderservice.order.client;

import iwo.wintech.orderservice.order.domain.book.Book;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.time.Duration;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@TestMethodOrder(MethodOrderer.Random.class)
class BookClientTest {
    private MockWebServer mockWebServer;
    private BookApiClient bookApiClient;

    @BeforeEach
    void setUp() throws IOException {
        this.mockWebServer = new MockWebServer();
        this.mockWebServer.start();
        final HttpUrl baseUrl = mockWebServer.url("/");
        final WebClient webClient = WebClient.builder()
                .baseUrl(baseUrl.uri().toString())
                .build();
        bookApiClient = new BookClient(webClient, Duration.ofSeconds(3));
    }

    @Test
    void getBookByIsbn_whenBookExistsThenReturnBook() throws InterruptedException {
        // Given
        final String isbn = "1234567890";
        final MockResponse mockResponse = new MockResponse()
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody("""
                        {
                          "isbn": "%s",
                          "title": "Title",
                          "author": "Author",
                          "price": 19.99,
                          "publisher": "Polarsophia"
                        }
                        """.formatted(isbn));

        mockWebServer.enqueue(mockResponse);

        // When
        final Mono<Book> bookByIsbn = bookApiClient.getBookByIsbn(isbn);

        // Then
        StepVerifier.create(bookByIsbn)
                .expectNextMatches(book -> book.isbn().equals(isbn))
                .verifyComplete();

        final RecordedRequest request = mockWebServer.takeRequest();
        assertThat(request.getPath()).isEqualTo("/books/" + isbn);
    }

    @AfterEach
    void tearDown() throws IOException {
        this.mockWebServer.shutdown();
    }
}