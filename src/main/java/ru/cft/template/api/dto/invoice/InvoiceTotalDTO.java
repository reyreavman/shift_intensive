package ru.cft.template.api.dto.invoice;

import lombok.Builder;
import ru.cft.template.core.entity.invoice.InvoiceFilters;
import ru.cft.template.core.entity.invoice.InvoiceStatus;

@Builder
public record InvoiceTotalDTO(
        Long userId,
        InvoiceStatus status,
        InvoiceFilters filters,
        Long total
) {
}
