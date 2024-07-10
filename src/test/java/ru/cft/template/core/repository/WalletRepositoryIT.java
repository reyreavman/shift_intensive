package ru.cft.template.core.repository;

import org.assertj.core.api.Assertions;
import org.junit.ClassRule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.cft.template.core.entity.User;
import ru.cft.template.core.entity.Wallet;
import ru.cft.template.testContainers.PostgreSQLTestContainer;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
@Sql("/sql/V1_1_1__Basic_schema.sql")
public class WalletRepositoryIT {
    @ClassRule
    public static final PostgreSQLContainer postgres = PostgreSQLTestContainer.getInstance();
    @Autowired
    public WalletRepository walletRepository;
    public static final List<User> preparedUsers = List.of(
            User.builder().phoneNumber("79123456789").email("ivanov@example.com").build(),
            User.builder().phoneNumber("79876543210").email("sidoorova@example.com").build(),
            User.builder().phoneNumber("79032145678").email("kuznetsov@example.com").build(),
            User.builder().phoneNumber("79243256789").email("popova@example.com").build(),
            User.builder().phoneNumber("79456372891").email("divanov@example.com").build()
    );
    public static final List<Wallet> preparedWallets = List.of(
            new Wallet(1L, preparedUsers.get(0), 100L),
            new Wallet(2L, preparedUsers.get(1), 100L),
            new Wallet(3L, preparedUsers.get(2), 100L),
            new Wallet(4L, preparedUsers.get(3), 100L),
            new Wallet(5L, preparedUsers.get(4), 100L)
    );

    @Test
    void findByUserPhoneNumber_ReturnsUserFoundByPhoneNumber() {
        String phoneNumber = "79123456789";
        Wallet expectedWallet = Optional.of(preparedWallets.get(0)).get();

        Wallet actualWallet = walletRepository.findByUserPhoneNumber(phoneNumber).get();

        Assertions.assertThat(actualWallet.getUser().getPhoneNumber()).isEqualTo(expectedWallet.getUser().getPhoneNumber());
        Assertions.assertThat(actualWallet.getUser().getEmail()).isEqualTo(expectedWallet.getUser().getEmail());
    }

    @Test
    void findByUserEmail_ReturnsUserFoundByEmail() {
        String email = "popova@example.com";
        Wallet expectedWallet = Optional.of(preparedWallets.get(3)).get();

        Wallet actualWallet = walletRepository.findByUserEmail(email).get();

        Assertions.assertThat(actualWallet.getUser().getPhoneNumber()).isEqualTo(expectedWallet.getUser().getPhoneNumber());
        Assertions.assertThat(actualWallet.getUser().getEmail()).isEqualTo(expectedWallet.getUser().getEmail());
    }
}
