package ru.cft.template.core.repository;

import org.springframework.data.repository.CrudRepository;
import ru.cft.template.core.entity.transfer.TransferAmongUsers;
import ru.cft.template.core.entity.transfer.TransferStatus;

public interface TransferAmongUsersRepository extends CrudRepository<TransferAmongUsers, Long> {
    Iterable<TransferAmongUsers> findAllByStatus(TransferStatus status);

    Iterable<TransferAmongUsers> findAllBySenderId(Long id);

    Iterable<TransferAmongUsers> findAllByRecipientId(Long id);
}
