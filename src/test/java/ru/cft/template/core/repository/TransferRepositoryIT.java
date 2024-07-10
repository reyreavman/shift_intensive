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
public class TransferRepositoryIT {
    @ClassRule
    public static final PostgreSQLContainer postgres = PostgreSQLTestContainer.getInstance();
    @Autowired
    public TransferRepository transferRepository;
    
    public static List<Wallet> preparedWallets = WalletRepositoryIT.preparedWallets;
    public static List<Transfer> preparedTransfers = List.of(
            Transfer.builder()
                    .id(1L)
                    .senderWallet(preparedWallets.get(0))
                    .recipientWallet(preparedWallets.get(1))
                    .amount(50L)
                    .status(TransferStatus.SUCCESSFUL)
                    .build(),
            Transfer.builder()
                    .id(2L)
                    .senderWallet(preparedWallets.get(2))
                    .recipientWallet(preparedWallets.get(3))
                    .amount(20L)
                    .status(TransferStatus.FAILED)
                    .build(),
            Transfer.builder()
                    .id(3L)
                    .senderWallet(preparedWallets.get(1))
                    .recipientWallet(preparedWallets.get(0))
                    .amount(75L)
                    .status(TransferStatus.SUCCESSFUL)
                    .build(),
            Transfer.builder()
                    .id(4L)
                    .senderWallet(preparedWallets.get(3))
                    .recipientWallet(preparedWallets.get(4))
                    .amount(10L)
                    .status(TransferStatus.SUCCESSFUL)
                    .build(),
            Transfer.builder()
                    .id(5L)
                    .senderWallet(preparedWallets.get(4))
                    .recipientWallet(preparedWallets.get(0))
                    .amount(30L)
                    .status(TransferStatus.FAILED)
                    .build(),
            Transfer.builder()
                    .id(6L)
                    .senderWallet(preparedWallets.get(2))
                    .recipientWallet(preparedWallets.get(1))
                    .amount(60L)
                    .status(TransferStatus.SUCCESSFUL)
                    .build(),
            Transfer.builder()
                    .id(7L)
                    .senderWallet(preparedWallets.get(0))
                    .recipientWallet(preparedWallets.get(3))
                    .amount(90L)
                    .status(TransferStatus.SUCCESSFUL)
                    .build(),
            Transfer.builder()
                    .id(8L)
                    .senderWallet(preparedWallets.get(4))
                    .recipientWallet(preparedWallets.get(2))
                    .amount(15L)
                    .status(TransferStatus.FAILED)
                    .build(),
            Transfer.builder()
                    .id(9L)
                    .senderWallet(preparedWallets.get(1))
                    .recipientWallet(preparedWallets.get(4))
                    .amount(85L)
                    .status(TransferStatus.SUCCESSFUL)
                    .build(),
            Transfer.builder()
                    .id(10L)
                    .senderWallet(preparedWallets.get(1))
                    .recipientWallet(preparedWallets.get(4))
                    .amount(85L)
                    .status(TransferStatus.SUCCESSFUL)
                    .build(),
            Transfer.builder()
                    .id(11L)
                    .senderWallet(preparedWallets.get(3))
                    .recipientWallet(preparedWallets.get(0))
                    .amount(25L)
                    .status(TransferStatus.SUCCESSFUL)
                    .build());

    private void assertions(List<Transfer> actualTransfers, List<Transfer> expectedTransfers) {
        Assertions.assertThat(actualTransfers)
                .usingRecursiveComparison()
                .ignoringFields("senderWallet", "recipientWallet")
                .isEqualTo(expectedTransfers);
    }

    @Test
    void findAllBySenderWalletId_ReturnsAllTransferWithSenderWalletId() {
        Long senderWalletId = 3L;
        List<Transfer> expectedTransfers = preparedTransfers
                .stream()
                .filter(transfer -> transfer.getSenderWallet().getId().equals(senderWalletId))
                .toList();

        List<Transfer> actualTransfers = transferRepository.findAllBySenderWalletId(senderWalletId);

        assertions(actualTransfers, expectedTransfers);
    }

    @Test
    void findAllBySenderWalletIdAndStatus_ReturnsAllTransferWithSenderWalletIdAndStatus() {
        Long senderWalletId = 5L;
        TransferStatus transferStatus = TransferStatus.FAILED;
        List<Transfer> expectedTransfers = preparedTransfers
                .stream()
                .filter(transfer -> transfer.getSenderWallet().getId().equals(senderWalletId) && transfer.getStatus().equals(transferStatus))
                .toList();

        List<Transfer> actualTransfers = transferRepository.findAllBySenderWalletIdAndStatus(senderWalletId, transferStatus);

        assertions(actualTransfers, expectedTransfers);
    }

    @Test
    void findAllByRecipientWalletId_ReturnsAllTransfersFoundWithRecipientWalletId() {
        Long walletId = 5L;
        List<Transfer> expectedTransfers = preparedTransfers
                .stream()
                .filter(transfer -> transfer.getRecipientWallet().getId().equals(walletId))
                .toList();

        List<Transfer> actualTransfers = transferRepository.findAllByRecipientWalletId(walletId);

        assertions(actualTransfers, expectedTransfers);
    }

    @Test
    void findAllByRecipientWalletIdAndStatus_ReturnsAllTransfersWithRecipientWalletIdAndStatus() {
        Long walletId = 1L;
        TransferStatus status = TransferStatus.SUCCESSFUL;
        List<Transfer> expectedTransfers = preparedTransfers
                .stream()
                .filter(transfer -> transfer.getRecipientWallet().getId().equals(walletId) && transfer.getStatus().equals(status))
                .toList();

        List<Transfer> actualTransfers = transferRepository.findAllByRecipientWalletIdAndStatus(walletId, status);

        assertions(actualTransfers, expectedTransfers);
    }

    @Test
    void findAllBySenderWalletIdOrRecipientWalletId_ReturnsAllTransfersWithSenderWalletIdOrRecipientWalletId() {
        Long walletId = 4L;
        List<Transfer> expectedTransfers = preparedTransfers
                .stream()
                .filter(transfer -> transfer.getRecipientWallet().getId().equals(walletId) || transfer.getSenderWallet().getId().equals(walletId))
                .toList();

        List<Transfer> actualTransfers = transferRepository.findAllBySenderWalletIdOrRecipientWalletId(walletId, walletId);

        assertions(actualTransfers, expectedTransfers);
    }

    @Test
    void findAllBySenderWalletIdOrRecipientWalletIdAndStatus_ReturnsAllTransfersWithSenderWalletIdOrRecipientWalletId() {
        Long walletId = 2L;
        TransferStatus status = TransferStatus.SUCCESSFUL;
        List<Transfer> expectedTransfers = preparedTransfers
                .stream()
                .filter(transfer -> transfer.getRecipientWallet().getId().equals(walletId) || transfer.getSenderWallet().getId().equals(walletId))
                .filter(transfer -> transfer.getStatus().equals(status))
                .toList();

        List<Transfer> actualTransfers = transferRepository.findAllBySenderWalletIdOrRecipientWalletIdAndStatus(walletId, walletId, status);

        assertions(actualTransfers, expectedTransfers);
        Assertions.assertThat(actualTransfers)
                .hasSize(5);
    }
}
