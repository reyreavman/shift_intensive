package ru.cft.template.core.mapper;

import org.springframework.stereotype.Component;
import ru.cft.template.api.dto.invoice.InvoiceDataDTO;
import ru.cft.template.api.dto.invoice.InvoiceTotalDTO;
import ru.cft.template.core.entity.User;
import ru.cft.template.core.entity.invoice.Invoice;
import ru.cft.template.core.entity.invoice.InvoiceFilters;
import ru.cft.template.core.entity.invoice.InvoiceStatus;

@Component
public class InvoiceMapper {
    public Invoice mapToInvoice(User sender, User recipient, Long amount, String comment) {
        return Invoice.builder()
                .id(null)
                .sender(sender)
                .recipient(recipient)
                .amount(amount)
                .comment(comment)
                .status(InvoiceStatus.NOT_PAID)
                .build();
    }

    public InvoiceDataDTO mapToInvoiceDataDTO(Invoice invoice) {
        return InvoiceDataDTO.builder()
                .invoiceId(invoice.getId())
                .senderId(invoice.getSender().getId())
                .recipientId(invoice.getRecipient().getId())
                .amount(invoice.getAmount())
                .comment(invoice.getComment())
                .status(invoice.getStatus())
                .createdDateTime(invoice.getCreationDateTime())
                .build();
    }

    public InvoiceTotalDTO mapToInvoiceTotalDTO(Long userId, InvoiceStatus status, InvoiceFilters filters, Long total) {
        return InvoiceTotalDTO.builder()
                .userId(userId)
                .status(status)
                .filters(filters)
                .total(total)
                .build();
    }
}
