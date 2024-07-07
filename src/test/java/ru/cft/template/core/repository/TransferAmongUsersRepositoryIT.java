package ru.cft.template.core.repository;

import org.junit.ClassRule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.cft.template.core.entity.transfer.TransferAmongUsers;
import ru.cft.template.core.entity.transfer.TransferStatus;
import ru.cft.template.testContainers.PostgreSQLTestContainer;

import java.util.List;

@SpringBootTest
@Transactional
@Sql("/sql/V1_1_1__Basic_schema.sql")
public class TransferAmongUsersRepositoryIT {
    @ClassRule
    public static final PostgreSQLContainer postgres = PostgreSQLTestContainer.getInstance();
    @Autowired
    public TransferAmongUsersRepository transferRepository;
    public List<TransferAmongUsers> transfers = List.of(

    );

    @Test
    void findAllByStatus_ReturnsAllTransfersWithStatus() {
        TransferStatus status = TransferStatus.SUCCESSFUL;

    }
}
