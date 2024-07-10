package ru.cft.template.core.repository;

import org.junit.ClassRule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.cft.template.api.dto.invoice.common.InvoiceStatus;
import ru.cft.template.core.entity.User;
import ru.cft.template.core.entity.invoice.Invoice;
import ru.cft.template.testContainers.PostgreSQLTestContainer;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Sql("/sql/V1_1_1__Basic_schema.sql")
public class InvoiceRepositoryIT {
    @ClassRule
    public static final PostgreSQLContainer postgres = PostgreSQLTestContainer.getInstance();
    @Autowired
    public InvoiceRepository invoiceRepository;

    public static List<User> preparedUsers = WalletRepositoryIT.preparedUsers;
    public static List<Invoice> preparedInvoices = List.of(
            Invoice.builder()
                    .sender(preparedUsers.get(0))
                    .recipient(preparedUsers.get(1))
                    .amount(300L)
                    .comment("This is a sample invoice comment")
                    .status(InvoiceStatus.PAID)
                    .build(),
            Invoice.builder()
                    .sender(preparedUsers.get(2))
                    .recipient(preparedUsers.get(3))
                    .amount(500L)
                    .comment("This is another sample invoice comment")
                    .status(InvoiceStatus.NOT_PAID)
                    .build(),
            Invoice.builder()
                    .sender(preparedUsers.get(1))
                    .recipient(preparedUsers.get(0))
                    .amount(250L)
                    .comment("This is a sample invoice for a product purchase")
                    .status(InvoiceStatus.PAID)
                    .build(),
            Invoice.builder()
                    .sender(preparedUsers.get(3))
                    .recipient(preparedUsers.get(2))
                    .amount(700L)
                    .comment("This is a sample invoice for a service")
                    .status(InvoiceStatus.NOT_PAID)
                    .build(),
            Invoice.builder()
                    .sender(preparedUsers.get(4))
                    .recipient(preparedUsers.get(0))
                    .amount(100L)
                    .comment("This is a small sample invoice")
                    .status(InvoiceStatus.CANCELED)
                    .build(),
            Invoice.builder()
                    .sender(preparedUsers.get(1))
                    .recipient(preparedUsers.get(3))
                    .amount(450L)
                    .comment("This is a sample invoice with a medium amount")
                    .status(InvoiceStatus.PAID)
                    .build(),
            Invoice.builder()
                    .sender(preparedUsers.get(2))
                    .recipient(preparedUsers.get(4))
                    .amount(900L)
                    .comment("This is a sample invoice with a large amount")
                    .status(InvoiceStatus.NOT_PAID)
                    .build(),
            Invoice.builder()
                    .sender(preparedUsers.get(0))
                    .recipient(preparedUsers.get(2))
                    .amount(600L)
                    .comment("This is a sample invoice between users 1 and 3")
                    .status(InvoiceStatus.PAID)
                    .build(),
            Invoice.builder()
                    .sender(preparedUsers.get(3))
                    .recipient(preparedUsers.get(1))
                    .amount(800L)
                    .comment("This is a sample invoice between users 4 and 2")
                    .status(InvoiceStatus.NOT_PAID)
                    .build(),
            Invoice.builder()
                    .sender(preparedUsers.get(4))
                    .recipient(preparedUsers.get(3))
                    .amount(150L)
                    .comment("This is a sample invoice between users 5 and 4")
                    .status(InvoiceStatus.CANCELED)
                    .build()
    );

    private void assertions(List<Invoice> actualInvoices, List<Invoice> expectedInvoices, int size) {
        assertThat(actualInvoices)
                .usingRecursiveComparison()
                .ignoringFields("id", "sender", "recipient")
                .isEqualTo(expectedInvoices);
        assertThat(actualInvoices)
                .hasSize(size);
    }

    @Test
    void findAllBySenderId_ReturnsAllInvoicesWithSenderId() {
        Long senderId = 4L;
        List<Invoice> expectedInvoices = preparedInvoices.stream()
                .filter(invoice -> invoice.getSender().getId().equals(senderId))
                .toList();

        List<Invoice> actualInvoices = invoiceRepository.findAllBySenderId(senderId);

        assertions(actualInvoices, expectedInvoices,2);
    }

    @Test
    void findAllByRecipientId_ReturnsAllInvoicesWithRecipientId() {
        Long recipientId = 1L;
        List<Invoice> expectedInvoices = preparedInvoices.stream()
                .filter(invoice -> invoice.getRecipient().getId().equals(recipientId))
                .toList();

        List<Invoice> actualInvoices = invoiceRepository.findAllByRecipientId(recipientId);

        assertions(actualInvoices, expectedInvoices, 2);

    }
}
