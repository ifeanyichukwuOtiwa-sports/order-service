package iwo.wintech.orderservice.order.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.Instant;

@Table(value = "orders")
public record Order (
        @Id Long id,
        String bookIsbn,
        String bookName,
        BigDecimal bookPrice,
        int quantity,
        OrderStatus status,
        @CreatedDate Instant createdDate,
        @LastModifiedDate Instant lastModifiedDate,
        @Version int version
){

    public static Order of(final String bookIsbn, final String bookName, final BigDecimal price, final int quantity,
                           final OrderStatus status) {
        return new Order(null, bookIsbn, bookName, price, quantity, status, null, null, 0);
    }

    public static Order ofRejected(String bookIsbn, final int quantity) {
        return of(bookIsbn, null, null, quantity, OrderStatus.REJECTED);
    }
}
