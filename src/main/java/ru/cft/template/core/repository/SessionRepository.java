package ru.cft.template.core.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.cft.template.core.entity.Session;
import ru.cft.template.core.entity.User;

import java.time.LocalDateTime;
import java.util.List;

public interface SessionRepository extends CrudRepository<Session, Long> {
    Session findByToken(String token);

    List<Session> findAllByUserAndActiveTrueAndExpirationTimeAfter(User user, LocalDateTime now);

    @Modifying
    @Query(value = "update session set active = false where expiration_time < :localDateTime",
            nativeQuery = true)
    void updateAllByExpirationTimeAfter(LocalDateTime now);
}
