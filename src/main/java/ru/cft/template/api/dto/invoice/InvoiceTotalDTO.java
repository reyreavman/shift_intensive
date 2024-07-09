package ru.cft.template.api.dto.invoice;

import lombok.Builder;
import ru.cft.template.api.dto.invoice.common.InvoiceFilters;

@Builder
public record InvoiceTotalDTO(
        Long userId,
        InvoiceFilters filters,
        Long total
) {
}
