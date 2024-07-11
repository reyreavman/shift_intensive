package ru.cft.template.core.service.invoice;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.cft.template.api.dto.invoice.CreateInvoiceDTO;
import ru.cft.template.api.dto.invoice.InvoiceDataDTO;
import ru.cft.template.api.dto.invoice.InvoiceTotalDTO;
import ru.cft.template.api.dto.invoice.PayInvoiceDTO;
import ru.cft.template.api.dto.invoice.common.InvoiceDirectionType;
import ru.cft.template.api.dto.invoice.common.InvoiceFilters;
import ru.cft.template.api.dto.invoice.common.InvoiceStatus;
import ru.cft.template.core.entity.User;
import ru.cft.template.core.entity.invoice.Invoice;
import ru.cft.template.core.entity.transfer.Transfer;
import ru.cft.template.core.entity.transfer.TransferStatus;
import ru.cft.template.core.exception.service.ServiceException;
import ru.cft.template.core.mapper.InvoiceMapper;
import ru.cft.template.core.repository.InvoiceRepository;
import ru.cft.template.core.service.transfer.TransferService;
import ru.cft.template.core.service.user.UserService;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class DefaultInvoiceService implements InvoiceService {
    private final InvoiceRepository invoiceRepository;

    private final UserService userService;
    private final TransferService transferService;

    private final InvoiceMapper invoiceMapper;


    @Override
    public InvoiceDataDTO createInvoice(Long userId, CreateInvoiceDTO createInvoiceDTO) {
        User sender = userService.findUserByIdEntity(userId);
        User recipient = userService.findUserByIdEntity(createInvoiceDTO.recipientId());
        Invoice savedInvoice = invoiceRepository.save(invoiceMapper.mapToInvoice(sender, recipient, createInvoiceDTO));
        return invoiceMapper.mapToInvoiceDataDTO(savedInvoice);
    }

    @Override
    @Transactional
    public InvoiceDataDTO payInvoice(Long id, PayInvoiceDTO invoicePayload) {
        Invoice invoice = findInvoiceById(invoicePayload.invoiceId());

        validateInvoiceToPay(invoice, invoice.getRecipient());
        Transfer transfer = transferService.createTransfer(invoice.getRecipient().getId(), invoice.getSender().getId(), invoice.getAmount());

        if (transfer.getStatus().equals(TransferStatus.SUCCESSFUL)) {
            invoice.setStatus(InvoiceStatus.PAID);
            invoice.setTransfer(transfer);
        }

        return invoiceMapper.mapToInvoiceDataDTO(invoice);
    }

    @Override
    @Transactional
    public InvoiceDataDTO cancelInvoice(Long userId, UUID invoiceId) {
        Invoice invoice = findInvoiceById(invoiceId);
        if (!Objects.equals(invoice.getSender().getId(), userId))
            throw new ServiceException("Пользователь - %d не может отменить данный счет.");
        invoice.setStatus(InvoiceStatus.CANCELED);
        return invoiceMapper.mapToInvoiceDataDTO(invoice);
    }

    @Override
    public InvoiceDataDTO getInvoiceDataById(UUID invoiceUUID) {
        return invoiceMapper.mapToInvoiceDataDTO(findInvoiceById(invoiceUUID));
    }

    @Override
    public List<InvoiceDataDTO> getUserInvoicesWithFilters(Long userId, InvoiceFilters filters) {
        return invoicesFiltersChain(invoiceRepository.findAllBySenderIdOrRecipientId(userId, userId).stream(), userId, filters)
                .map(invoiceMapper::mapToInvoiceDataDTO)
                .toList();
    }

    @Override
    public InvoiceTotalDTO getUserInvoicesTotalWithFilters(Long userId, InvoiceFilters filters) {
        long totalAmount = invoicesFiltersChain(invoiceRepository.findAllBySenderIdOrRecipientId(userId, userId).stream(), userId, filters)
                .mapToLong(Invoice::getAmount)
                .sum();
        return invoiceMapper.mapToInvoiceTotalDTO(userId, totalAmount);
    }

    @Override
    public InvoiceDataDTO getOldestUserInvoice(Long userId, InvoiceFilters filters) {
        Invoice oldestUserInvoice = invoicesFiltersChain(invoiceRepository.findBySenderIdOrRecipientIdOrderByCreationDateTimeAsc(userId, userId).stream(), userId, filters)
                .findFirst().orElseThrow();
        return invoiceMapper.mapToInvoiceDataDTO(oldestUserInvoice);
    }

    private Stream<Invoice> invoicesFiltersChain(Stream<Invoice> invoicesStream, Long userId, InvoiceFilters filters) {
        return invoicesStream
                .filter(invoice -> Objects.isNull(filters.getDirectionType()) ||
                        filters.getDirectionType().equals(InvoiceDirectionType.INCOMING) ?
                        invoice.getRecipient().getId().equals(userId) :
                        invoice.getSender().getId().equals(userId))
                .filter(invoice -> Objects.isNull(filters.getStatus()) ||
                        invoice.getStatus().equals(filters.getStatus()))
                .filter(invoice -> Objects.isNull(filters.getStart()) ||
                        invoice.getCreationDateTime().toLocalDate().isAfter(filters.getStart()))
                .filter(invoice -> Objects.isNull(filters.getEnd()) ||
                        invoice.getCreationDateTime().toLocalDate().isBefore(filters.getEnd()));
    }

    private void validateInvoiceToPay(Invoice invoice, User recipient) {
        if (invoice.getStatus().equals(InvoiceStatus.PAID))
            throw new ServiceException("Cчет - %s уже оплачен".formatted(invoice.getId()));
        if (!invoice.getRecipient().equals(recipient))
            throw new ServiceException("Пользователь - %d не может оплатить данный счет.".formatted(recipient.getId()));
    }

    private Invoice findInvoiceById(UUID invoiceId) {
        return invoiceRepository.findById(invoiceId).orElseThrow(() -> new ServiceException("Счет с id - %s не найден".formatted(invoiceId.toString())));
    }
}
