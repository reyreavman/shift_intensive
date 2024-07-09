package ru.cft.template.api.dto.invoice.common;

import lombok.Builder;
import lombok.Getter;
import ru.cft.template.core.entity.invoice.InvoiceStatus;

import java.time.LocalDate;

@Getter
@Builder
public class InvoiceFilters {
    private final InvoiceDirectionType directionType;
    private final InvoiceStatus status;
    private final LocalDate start;
    private final LocalDate end;
}
