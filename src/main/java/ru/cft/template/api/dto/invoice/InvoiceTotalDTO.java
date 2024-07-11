package ru.cft.template.api.dto.invoice;

import lombok.Builder;

@Builder
public record InvoiceTotalDTO(
        Long userId,
        Long total
) {
}
