package ru.cft.template.core.repository;

import org.springframework.data.repository.CrudRepository;
import ru.cft.template.core.entity.transfer.Transfer;
import ru.cft.template.core.entity.transfer.TransferStatus;

import java.util.List;

public interface TransferRepository extends CrudRepository<Transfer, Long> {
    List<Transfer> findAllByStatus(TransferStatus status);

    List<Transfer> findAllBySenderWalletId(Long id);

    List<Transfer> findAllByRecipientWalletId(Long id);
}
