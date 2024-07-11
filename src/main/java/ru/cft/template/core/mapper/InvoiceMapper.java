package ru.cft.template.core.mapper;

import org.springframework.stereotype.Component;
import ru.cft.template.api.dto.invoice.CreateInvoiceDTO;
import ru.cft.template.api.dto.invoice.InvoiceDataDTO;
import ru.cft.template.api.dto.invoice.InvoiceTotalDTO;
import ru.cft.template.api.dto.invoice.common.InvoiceStatus;
import ru.cft.template.core.entity.User;
import ru.cft.template.core.entity.invoice.Invoice;

@Component
public class InvoiceMapper {
    public Invoice mapToInvoice(User sender, User recipient, CreateInvoiceDTO invoiceDTO) {
        return Invoice.builder()
                .id(null)
                .sender(sender)
                .recipient(recipient)
                .amount(invoiceDTO.amount())
                .comment(invoiceDTO.comment())
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
                .transfer(invoice.getTransfer())
                .createdDateTime(invoice.getCreationDateTime())
                .build();
    }

    public InvoiceTotalDTO mapToInvoiceTotalDTO(Long userId, Long total) {
        return InvoiceTotalDTO.builder()
                .userId(userId)
                .total(total)
                .build();
    }
}
