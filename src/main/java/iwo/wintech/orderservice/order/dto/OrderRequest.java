package iwo.wintech.orderservice.order.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record OrderRequest(
        @NotBlank(message = "The book Isbn must e defined")
        String isbn,
        @Min(value = 1, message = "You must order at least 1 item")
        @Max(value = 5, message = "You must not order more than 5 items")
        int quantity
) {
}
