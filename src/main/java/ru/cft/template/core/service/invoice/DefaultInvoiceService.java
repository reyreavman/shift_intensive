package ru.cft.template.core.service.invoice;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.cft.template.api.dto.invoice.InvoiceDataDTO;
import ru.cft.template.api.dto.invoice.InvoiceTotalDTO;
import ru.cft.template.api.payload.invoice.NewInvoicePayload;
import ru.cft.template.api.payload.invoice.NewPayInvoicePayload;
import ru.cft.template.core.entity.User;
import ru.cft.template.core.entity.Wallet;
import ru.cft.template.core.entity.invoice.Invoice;
import ru.cft.template.core.entity.invoice.InvoiceFilters;
import ru.cft.template.core.entity.invoice.InvoiceStatus;
import ru.cft.template.core.entity.transfer.Transfer;
import ru.cft.template.core.entity.transfer.TransferStatus;
import ru.cft.template.core.exception.EntityNotFoundException;
import ru.cft.template.core.exception.InvalidInvoiceAmountException;
import ru.cft.template.core.exception.InvalidInvoicePayerException;
import ru.cft.template.core.exception.InvoiceAlreadyPaidException;
import ru.cft.template.core.exception.NotEnoughMoneyException;
import ru.cft.template.core.exception.NotInvoiceOwnerException;
import ru.cft.template.core.repository.InvoiceRepository;
import ru.cft.template.core.repository.TransferRepository;
import ru.cft.template.core.repository.UserRepository;
import ru.cft.template.core.repository.WalletRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DefaultInvoiceService implements InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final UserRepository userRepository;
    private final TransferRepository transferRepository;
    private final WalletRepository walletRepository;
    private final ConversionService conversionService;

    @Override
    @Transactional
    public InvoiceDataDTO createInvoice(Long userId, @Valid NewInvoicePayload invoicePayload) {
        User sender = this.userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(User.class.getSimpleName(), Map.of("senderId", String.valueOf(userId))));
        User recipient = this.userRepository.findById(invoicePayload.recipientId()).orElseThrow(() -> new EntityNotFoundException(User.class.getSimpleName(), Map.of("recipientId", String.valueOf(userId))));
        Invoice invoice = this.invoiceRepository.save(new Invoice(null, sender, recipient, invoicePayload.amount(), invoicePayload.comment(), InvoiceStatus.NOT_PAID, LocalDateTime.now()));
        return this.conversionService.convert(invoice, InvoiceDataDTO.class);
    }

    @Override
    @Transactional
    public InvoiceDataDTO payInvoice(Long userId, @Valid NewPayInvoicePayload invoicePayload) {
        Invoice invoice = this.invoiceRepository.findById(invoicePayload.invoiceId()).orElseThrow(() -> new EntityNotFoundException(Invoice.class.getSimpleName(), Map.of("invoiceId", String.valueOf(invoicePayload.invoiceId()))));
        if (!Objects.equals(invoicePayload.amount(), invoice.getAmount())) throw new InvalidInvoiceAmountException(invoice.getAmount(), invoicePayload.amount());
        User recipient = this.userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(User.class.getSimpleName(), Map.of("userId", String.valueOf(userId))));
        if (!invoice.getRecipient().equals(recipient)) throw new InvalidInvoicePayerException(userId, invoice.getId());
        if (invoice.getStatus().equals(InvoiceStatus.PAID)) throw new InvoiceAlreadyPaidException(invoice.getId());
        Wallet recipientWallet = this.walletRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(Wallet.class.getSimpleName(), Map.of("userId", String.valueOf(userId))));
        if (recipientWallet.getBalance() >= invoice.getAmount()) {
            Wallet senderWallet = this.walletRepository.findById(invoice.getSender().getId()).orElseThrow(() -> new EntityNotFoundException(Wallet.class.getSimpleName(), Map.of("ownerId", String.valueOf(userId))));
            recipientWallet.setBalance(recipientWallet.getBalance() - invoice.getAmount());
            senderWallet.setBalance(senderWallet.getBalance() + invoice.getAmount());
            invoice.setStatus(InvoiceStatus.PAID);
            this.transferRepository.save(new Transfer(null, recipientWallet, senderWallet, invoice.getAmount(), TransferStatus.SUCCESSFUL, LocalDateTime.now()));
            return this.conversionService.convert(invoice, InvoiceDataDTO.class);
        } else throw new NotEnoughMoneyException(recipientWallet.getBalance(), recipientWallet.getBalance(), invoice.getAmount());
    }

    @Override
    @Transactional
    public InvoiceDataDTO cancelInvoice(Long userId, UUID invoiceId) {
        Invoice invoice = this.invoiceRepository.findById(invoiceId).orElseThrow(() -> new EntityNotFoundException(Invoice.class.getSimpleName(), Map.of("invoiceId", String.valueOf(invoiceId))));
        if (!invoice.getSender().equals(userId)) throw new NotInvoiceOwnerException(userId, invoiceId);
        invoice.setStatus(InvoiceStatus.CANCELED);
        return this.conversionService.convert(invoice, InvoiceDataDTO.class);
    }

    @Override
    public InvoiceDataDTO getInvoiceDataById(UUID invoiceUUID) {
        return this.conversionService.convert(this.invoiceRepository.findById(invoiceUUID), InvoiceDataDTO.class);
    }

    @Override
    public List<InvoiceDataDTO> getUserInvoicesWithFilters(Long userId, InvoiceFilters filters) {

    }

    @Override
    public InvoiceTotalDTO getUserInvoicesTotalWithFilters(Long userId, InvoiceFilters filters) {
        return null;
    }

    @Override
    public InvoiceDataDTO getOldestUserInvoice(Long userId, InvoiceFilters filters) {
        return null;
    }
}
