package ru.cft.template.core.service.invoice;

import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.cft.template.api.dto.invoice.CreateInvoiceDTO;
import ru.cft.template.api.dto.invoice.InvoiceDataDTO;
import ru.cft.template.api.dto.invoice.InvoiceTotalDTO;
import ru.cft.template.api.dto.invoice.PayInvoiceDTO;
import ru.cft.template.api.dto.invoice.common.InvoiceDirectionType;
import ru.cft.template.api.dto.invoice.common.InvoiceFilters;
import ru.cft.template.core.entity.User;
import ru.cft.template.core.entity.Wallet;
import ru.cft.template.core.entity.invoice.Invoice;
import ru.cft.template.core.entity.invoice.InvoiceStatus;
import ru.cft.template.core.entity.transfer.TransferStatus;
import ru.cft.template.core.mapper.InvoiceMapper;
import ru.cft.template.core.mapper.TransferMapper;
import ru.cft.template.core.repository.InvoiceRepository;
import ru.cft.template.core.repository.TransferRepository;
import ru.cft.template.core.repository.UserRepository;
import ru.cft.template.core.repository.WalletRepository;

import java.time.chrono.ChronoLocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class DefaultInvoiceService implements InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final UserRepository userRepository;
    private final TransferRepository transferRepository;
    private final WalletRepository walletRepository;

    private final InvoiceMapper invoiceMapper;
    private final TransferMapper transferMapper;


    @Override
    public InvoiceDataDTO createInvoice(Long userId, CreateInvoiceDTO createInvoiceDTO) {
        User sender = userRepository.findById(userId).orElseThrow();
        User recipient = userRepository.findById(createInvoiceDTO.recipientId()).orElseThrow();
        Invoice savedInvoice = invoiceRepository.save(invoiceMapper.mapToInvoice(sender, recipient, createInvoiceDTO.amount(), createInvoiceDTO.comment()));
        return invoiceMapper.mapToInvoiceDataDTO(savedInvoice);
    }

    @Override
    @Transactional
    public InvoiceDataDTO payInvoice(Long userId, PayInvoiceDTO invoicePayload) {
        Invoice invoice = invoiceRepository.findById(invoicePayload.invoiceId()).orElseThrow();
        User recipient = userRepository.findById(userId).orElseThrow();

        if (invoice.getStatus().equals(InvoiceStatus.PAID))
            throw new ServiceException("Cчет - %s уже оплачен".formatted(invoice.getId()));
        if (!invoice.getRecipient().equals(recipient))
            throw new ServiceException("Пользователь - %d не может оплатить данный счет.".formatted(userId));
        if (!Objects.equals(invoicePayload.amount(), invoice.getAmount()))
            throw new ServiceException("Попытка оплатить не полную сумму услуги.");

        Wallet recipientWallet = walletRepository.findById(userId).orElseThrow();
        Wallet senderWallet = walletRepository.findById(invoice.getSender().getId()).orElseThrow();
        if (recipientWallet.getBalance() >= invoice.getAmount()) {
            recipientWallet.setBalance(recipientWallet.getBalance() - invoice.getAmount());
            senderWallet.setBalance(senderWallet.getBalance() + invoice.getAmount());
            invoice.setStatus(InvoiceStatus.PAID);
            transferRepository.save(transferMapper.mapToTransfer(invoice.getAmount(), recipientWallet, senderWallet, TransferStatus.SUCCESSFUL));
            return invoiceMapper.mapToInvoiceDataDTO(invoice);
        } else {
            transferRepository.save(transferMapper.mapToTransfer(invoice.getAmount(), recipientWallet, senderWallet, TransferStatus.FAILED));
            throw new ServiceException("На счету недостаточно средств.");
        }
    }

    @Override
    @Transactional
    public InvoiceDataDTO cancelInvoice(Long userId, UUID invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow();
        if (!Objects.equals(invoice.getSender().getId(), userId))
            throw new ServiceException("Пользователь - %d не может отменить данный счет.");
        invoice.setStatus(InvoiceStatus.CANCELED);
        return invoiceMapper.mapToInvoiceDataDTO(invoice);
    }

    @Override
    public InvoiceDataDTO getInvoiceDataById(UUID invoiceUUID) {
        return invoiceMapper.mapToInvoiceDataDTO(invoiceRepository.findById(invoiceUUID).orElseThrow());
    }

    @Override
    public List<InvoiceDataDTO> getUserInvoicesWithFilters(Long userId, InvoiceFilters filters) {
        List<Predicate<Invoice>> predicates = invoiceFiltersPredicates(filters);
        if (filters.getDirectionType().equals(InvoiceDirectionType.INCOMING))
            return invoiceRepository.findAllByRecipientId(userId).stream()
                    .filter(predicates.get(0))
                    .filter(predicates.get(1))
                    .filter(predicates.get(2))
                    .map(invoiceMapper::mapToInvoiceDataDTO)
                    .toList();
        return invoiceRepository.findAllBySenderId(userId).stream()
                .filter(predicates.get(0))
                .filter(predicates.get(1))
                .filter(predicates.get(2))
                .map(invoiceMapper::mapToInvoiceDataDTO)
                .toList();
    }

    @Override
    public InvoiceTotalDTO getUserInvoicesTotalWithFilters(Long userId, InvoiceFilters filters) {
        List<Predicate<Invoice>> predicates = invoiceFiltersPredicates(filters);

        if (filters.getDirectionType().equals(InvoiceDirectionType.INCOMING)) {
            long totalAmount = invoiceRepository.findAllByRecipientId(userId).stream()
                    .filter(predicates.get(0))
                    .filter(predicates.get(1))
                    .filter(predicates.get(2))
                    .mapToLong(Invoice::getAmount).sum();
            return invoiceMapper.mapToInvoiceTotalDTO(userId, filters, totalAmount);
        }
        long totalAmount = invoiceRepository.findAllByRecipientId(userId).stream()
                .filter(predicates.get(0))
                .filter(predicates.get(1))
                .filter(predicates.get(2))
                .mapToLong(Invoice::getAmount).sum();
        return invoiceMapper.mapToInvoiceTotalDTO(userId, filters, totalAmount);
    }

    @Override
    public InvoiceDataDTO getOldestUserInvoice(Long userId, InvoiceFilters filters) {
        List<Predicate<Invoice>> predicates = invoiceFiltersPredicates(filters);

        if (filters.getDirectionType().equals(InvoiceDirectionType.INCOMING)) {
            Invoice oldestUserInvoice = invoiceRepository.findByRecipientIdOrderByCreationDateTimeAsc(userId).stream()
                    .filter(predicates.get(0))
                    .filter(predicates.get(1))
                    .filter(predicates.get(2))
                    .findFirst().orElseThrow();
            return invoiceMapper.mapToInvoiceDataDTO(oldestUserInvoice);
        }
        Invoice oldestUserInvoice = invoiceRepository.findBySenderIdOrderByCreationDateTimeAsc(userId).stream()
                .filter(predicates.get(0))
                .filter(predicates.get(1))
                .filter(predicates.get(2))
                .findFirst().orElseThrow();
        return invoiceMapper.mapToInvoiceDataDTO(oldestUserInvoice);
    }

    private List<Predicate<Invoice>> invoiceFiltersPredicates(InvoiceFilters filters) {
        return List.of(
                invoice -> Objects.isNull(filters.getStatus()) || invoice.getStatus().equals(filters.getStatus()),
                invoice -> Objects.isNull(filters.getStart()) || invoice.getCreationDateTime().isAfter(ChronoLocalDateTime.from(filters.getStart())),
                invoice -> Objects.isNull(filters.getEnd()) || invoice.getCreationDateTime().isBefore(ChronoLocalDateTime.from(filters.getEnd()))
        );
    }
}
