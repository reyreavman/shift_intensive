package ru.cft.template.core.repository;

import org.springframework.data.repository.CrudRepository;
import ru.cft.template.core.entity.transfer.Transfer;
import ru.cft.template.core.entity.transfer.TransferStatus;

import java.util.List;

public interface TransferRepository extends CrudRepository<Transfer, Long> {

    List<Transfer> findAllBySenderWalletId(Long id);

    List<Transfer> findAllBySenderWalletIdAndStatus(Long senderWalletId, TransferStatus status);

    List<Transfer> findAllByRecipientWalletId(Long id);

    List<Transfer> findAllByRecipientWalletIdAndStatus(Long recipientWalletId, TransferStatus status);

    List<Transfer> findAllBySenderWalletIdOrRecipientWalletId(Long senderWalletId, Long recipientWalletId);

    List<Transfer> findAllBySenderWalletIdOrRecipientWalletIdAndStatus(Long senderWalletId, Long recipientWalletId, TransferStatus status);

}
