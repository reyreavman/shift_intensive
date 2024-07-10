package ru.cft.template.core.repository;

import org.springframework.data.repository.CrudRepository;
import ru.cft.template.core.entity.Wallet;

import java.util.Optional;

public interface WalletRepository extends CrudRepository<Wallet, Long> {
    Optional<Wallet> findByUserPhoneNumber(String phoneNumber);

    Optional<Wallet> findByUserEmail(String email);
}
