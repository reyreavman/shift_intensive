package ru.cft.template.core.entity.invoice;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class InvoiceFilters {
    private final Boolean createdBy;
    private final Boolean issuedTo;
    private final InvoiceStatus status;
    private final LocalDate start;
    private final LocalDate end;
}