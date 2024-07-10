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

@SpringBootTest
@Transactional
@Sql("/sql/V1_1_1__Basic_schema.sql")
public class WalletRepositoryIT {
    @ClassRule
    public static final PostgreSQLContainer postgres = PostgreSQLTestContainer.getInstance();
    @Autowired
    public WalletRepository walletRepository;

    public static final List<User> preparedUsers = List.of(
            User.builder()
                    .id(1L)
                    .phoneNumber("79123456789")
                    .email("ivanov@example.com")
                    .passwordHash("hashed_password")
                    .build(),
            User.builder()
                    .id(2L)
                    .phoneNumber("79876543210")
                    .email("sidoorova@example.com")
                    .passwordHash("another_hashed_password")
                    .build(),
            User.builder()
                    .id(3L)
                    .phoneNumber("79032145678")
                    .email("kuznetsov@example.com")
                    .passwordHash("third_hashed_password")
                    .build(),
            User.builder()
                    .id(4L)
                    .phoneNumber("79243256789")
                    .email("popova@example.com")
                    .passwordHash("fourth_hashed_password")
                    .build(),
            User.builder()
                    .id(5L)
                    .phoneNumber("79456372891")
                    .email("divanov@example.com")
                    .passwordHash("fifth_hashed_password")
                    .build()
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
        Wallet expectedWallet = preparedWallets.get(0);

        Wallet actualWallet = walletRepository.findByUserPhoneNumber(phoneNumber).get();

        Assertions.assertThat(actualWallet.getUser().getPhoneNumber())
                .isEqualTo(expectedWallet.getUser().getPhoneNumber());
        Assertions.assertThat(actualWallet.getUser().getEmail())
                .isEqualTo(expectedWallet.getUser().getEmail());
    }

    @Test
    void findByUserEmail_ReturnsUserFoundByEmail() {
        String email = "popova@example.com";
        Wallet expectedWallet = preparedWallets.get(3);

        Wallet actualWallet = walletRepository.findByUserEmail(email).get();

        Assertions.assertThat(actualWallet.getUser().getPhoneNumber())
                .isEqualTo(expectedWallet.getUser().getPhoneNumber());
        Assertions.assertThat(actualWallet.getUser().getEmail())
                .isEqualTo(expectedWallet.getUser().getEmail());
    }
}
