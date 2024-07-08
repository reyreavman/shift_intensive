package ru.cft.template.core.repository;

import org.assertj.core.api.Assertions;
import org.junit.ClassRule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.cft.template.core.entity.Wallet;
import ru.cft.template.core.entity.transfer.Transfer;
import ru.cft.template.core.entity.transfer.TransferStatus;
import ru.cft.template.testContainers.PostgreSQLTestContainer;

import java.util.List;

@SpringBootTest
@Transactional
@Sql("/sql/V1_1_1__Basic_schema.sql")
public class TransferAmongUsersRepositoryIT {
    @ClassRule
    public static final PostgreSQLContainer postgres = PostgreSQLTestContainer.getInstance();
    @Autowired
    public TransferRepository transferRepository;
    public List<Wallet> wallets = List.of(
            new Wallet(1L, null, 100L),
            new Wallet(2L, null, 100L),
            new Wallet(3L, null, 100L),
            new Wallet(4L, null, 100L),
            new Wallet(5L, null, 100L)
    );
    public List<Transfer> expectedTransfers = List.of(
            Transfer.builder()
                    .id(1L)
                    .senderWallet(this.wallets.get(0))
                    .recipientWallet(this.wallets.get(1))
                    .amount(50L)
                    .status(TransferStatus.SUCCESSFUL)
                    .build(),
            Transfer.builder()
                    .id(2L)
                    .senderWallet(this.wallets.get(2))
                    .recipientWallet(this.wallets.get(3))
                    .amount(20L)
                    .status(TransferStatus.FAILED)
                    .build(),
            Transfer.builder()
                    .id(3L)
                    .senderWallet(this.wallets.get(1))
                    .recipientWallet(this.wallets.get(0))
                    .amount(75L)
                    .status(TransferStatus.SUCCESSFUL)
                    .build(),
            Transfer.builder()
                    .id(4L)
                    .senderWallet(this.wallets.get(3))
                    .recipientWallet(this.wallets.get(4))
                    .amount(10L)
                    .status(TransferStatus.SUCCESSFUL)
                    .build(),
            Transfer.builder()
                    .id(5L)
                    .senderWallet(this.wallets.get(4))
                    .recipientWallet(this.wallets.get(0))
                    .amount(30L)
                    .status(TransferStatus.FAILED)
                    .build(),
            Transfer.builder()
                    .id(6L)
                    .senderWallet(this.wallets.get(2))
                    .recipientWallet(this.wallets.get(1))
                    .amount(60L)
                    .status(TransferStatus.SUCCESSFUL)
                    .build(),
            Transfer.builder()
                    .id(7L)
                    .senderWallet(this.wallets.get(0))
                    .recipientWallet(this.wallets.get(3))
                    .amount(90L)
                    .status(TransferStatus.SUCCESSFUL)
                    .build(),
            Transfer.builder()
                    .id(8L)
                    .senderWallet(this.wallets.get(4))
                    .recipientWallet(this.wallets.get(2))
                    .amount(15L)
                    .status(TransferStatus.FAILED)
                    .build(),
            Transfer.builder()
                    .id(9L)
                    .senderWallet(this.wallets.get(1))
                    .recipientWallet(this.wallets.get(4))
                    .amount(85L)
                    .status(TransferStatus.SUCCESSFUL)
                    .build(),
            Transfer.builder()
                    .id(10L)
                    .senderWallet(this.wallets.get(1))
                    .recipientWallet(this.wallets.get(4))
                    .amount(85L)
                    .status(TransferStatus.SUCCESSFUL)
                    .build(),
            Transfer.builder()
                    .id(11L)
                    .senderWallet(this.wallets.get(3))
                    .recipientWallet(this.wallets.get(0))
                    .amount(25L)
                    .status(TransferStatus.SUCCESSFUL)
                    .build());


    @Test
    void findAllByStatus_ReturnsAllTransfersWithStatus() {
        TransferStatus status = TransferStatus.SUCCESSFUL;
        List<Transfer> expectedTransfers = this.expectedTransfers.stream().filter(transfer -> transfer.getStatus().equals(status)).toList();

        List<Transfer> actualTransfers = this.transferRepository.findAllByStatus(status);

        Assertions.assertThat(actualTransfers)
                .usingRecursiveComparison()
                .ignoringFields("senderWallet", "recipientWallet")
                .isEqualTo(expectedTransfers);
    }

    @Test
    void findAllBySenderWalletId_ReturnsAllTransfersFoundWithSenderWalletId() {
        Long walletId = 4L;
        List<Transfer> expectedTransfers = this.expectedTransfers.stream().filter(transfer -> transfer.getSenderWallet().getId().equals(walletId)).toList();

        List<Transfer> actualTransfers = this.transferRepository.findAllBySenderWalletId(walletId);

        Assertions.assertThat(actualTransfers)
                .usingRecursiveComparison()
                .ignoringFields("senderWallet", "recipientWallet")
                .isEqualTo(expectedTransfers);
    }

    @Test
    void findAllByRecipientWalletId_ReturnsAllTransfersFoundWithRecipientWalletId() {
        Long walletId = 5L;
        List<Transfer> expectedTransfers = this.expectedTransfers.stream().filter(transfer -> transfer.getRecipientWallet().getId().equals(walletId)).toList();

        List<Transfer> actualTransfers = this.transferRepository.findAllByRecipientWalletId(walletId);

        Assertions.assertThat(actualTransfers)
                .usingRecursiveComparison()
                .ignoringFields("senderWallet", "recipientWallet")
                .isEqualTo(expectedTransfers);
    }


}
