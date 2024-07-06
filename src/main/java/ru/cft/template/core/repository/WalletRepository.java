package ru.cft.template.core.repository;

import org.springframework.data.repository.CrudRepository;
import ru.cft.template.core.entity.Wallet;

public interface WalletRepository extends CrudRepository<Wallet, Long> {
}
