package ru.cft.template.core.repository;

import org.springframework.data.repository.CrudRepository;
import ru.cft.template.core.entity.invoice.Invoice;

import java.util.List;
import java.util.UUID;

public interface InvoiceRepository extends CrudRepository<Invoice, UUID> {
    List<Invoice> findAllBySenderId(Long senderId);

    List<Invoice> findAllByRecipientId(Long recipientId);

    List<Invoice> findBySenderIdOrderByCreationDateTimeAsc(Long senderId);

    List<Invoice> findByRecipientIdOrderByCreationDateTimeAsc(Long recipientId);
}
