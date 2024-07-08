package ru.cft.template.core.service.invoice;

import ru.cft.template.api.dto.invoice.InvoiceDataDTO;
import ru.cft.template.api.dto.invoice.InvoiceTotalDTO;
import ru.cft.template.api.payload.invoice.InvoicePayload;
import ru.cft.template.api.payload.invoice.PayInvoicePayload;
import ru.cft.template.core.entity.invoice.InvoiceFilters;

import java.util.List;
import java.util.UUID;

public interface InvoiceService {
    InvoiceDataDTO createInvoice(Long userId, InvoicePayload invoicePayload);

    InvoiceDataDTO payInvoice(Long userId, PayInvoicePayload invoicePayload);

    InvoiceDataDTO cancelInvoice(Long userId, UUID invoiceId);

    InvoiceDataDTO getInvoiceDataById(UUID invoiceUUID);

    List<InvoiceDataDTO> getUserInvoicesWithFilters(Long userId, InvoiceFilters filters);

    InvoiceTotalDTO getUserInvoicesTotalWithFilters(Long userId, InvoiceFilters filters);

    InvoiceDataDTO getOldestUserInvoice(Long userId, InvoiceFilters filters);
}
