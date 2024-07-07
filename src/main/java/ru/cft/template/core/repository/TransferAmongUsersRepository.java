package ru.cft.template.core.repository;

import org.springframework.data.repository.CrudRepository;
import ru.cft.template.core.entity.transfer.TransferAmongUsers;
import ru.cft.template.core.entity.transfer.TransferStatus;

import java.util.List;

public interface TransferAmongUsersRepository extends CrudRepository<TransferAmongUsers, Long> {
    List<TransferAmongUsers> findAllByStatus(TransferStatus status);

    List<TransferAmongUsers> findAllBySenderWalletId(Long id);

    List<TransferAmongUsers> findAllByRecipientWalletId(Long id);
}
