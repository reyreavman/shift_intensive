package ru.cft.template.api.dto.invoice;

import ru.cft.template.core.entity.invoice.InvoiceFilters;
import ru.cft.template.core.entity.invoice.InvoiceStatus;

public record InvoiceTotalDTO(
        Long userId,
        InvoiceStatus status,
        InvoiceFilters filters,
        Long total
) {
}
