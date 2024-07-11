package ru.cft.template.core.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.cft.template.core.service.session.SessionService;

@Service
@RequiredArgsConstructor
public class ScheduledTaskExecutor {
    private final SessionService service;

    @Transactional
    @Scheduled(fixedDelayString = "PT60S")
    public void invalidateExpiredSessions() {
        service.invalidateExpiredSessions();
    }
}
