package ru.cft.template.core.repository;

import org.junit.ClassRule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.cft.template.core.entity.User;
import ru.cft.template.testContainers.PostgreSQLTestContainer;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Sql("/sql/V1_1_1__Basic_schema.sql")
public class UserRepositoryIT {
    @ClassRule
    public static final PostgreSQLContainer postgres = PostgreSQLTestContainer.getInstance();
    @Autowired
    public UserRepository userRepository;

    public static User expectedToFindUser = User.builder()
            .id(4L)
            .firstName("Екатерина")
            .middleName("Дмитриевна")
            .lastName("Попова")
            .phoneNumber("79243256789")
            .email("popova@example.com")
            .birthdate(LocalDate.parse("2010-04-04"))
            .passwordHash("fourth_hashed_password")
            .build();

    @Test
    void findByEmail_ReturnsUserFoundByEmail() {
        String filter = "popova@example.com";

        Optional<User> actualUser = this.userRepository.findByEmail(filter);

        assertThat(actualUser)
                .usingRecursiveComparison()
                .ignoringFields("creationTimeStamp", "lastUpdateTimeStamp")
                .isEqualTo(Optional.of(expectedToFindUser));
    }

    @Test
    void findByPhoneNumber_ReturnsUserFoundByPhoneNumber() {
        String filter = "79243256789";

        Optional<User> actualUser = this.userRepository.findByPhoneNumber(filter);

        assertThat(actualUser)
                .usingRecursiveComparison()
                .ignoringFields("creationTimeStamp", "lastUpdateTimeStamp")
                .isEqualTo(Optional.of(expectedToFindUser));
    }

    @Test
    void saveUser_ReturnsSavedUser() {
        User expectedUser = User.builder()
                .id(null)
                .firstName("Тест")
                .middleName("Тестович")
                .lastName("Тестов")
                .phoneNumber("79119119191")
                .email("test@example.com")
                .birthdate(LocalDate.of(2000, 1,1))
                .passwordHash("Example!1")
                .build();

        User actualUser = this.userRepository.save(expectedUser);

        assertThat(actualUser)
                .isEqualTo(expectedUser);
    }
}
