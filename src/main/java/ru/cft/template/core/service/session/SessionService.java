package ru.cft.template.core.service.session;

import ru.cft.template.api.dto.session.CreateSessionByEmailDTO;
import ru.cft.template.api.dto.session.SessionDTO;
import ru.cft.template.core.entity.Session;

import java.util.List;

public interface SessionService {
    Session getValidSession(String token);

    List<SessionDTO> getAllActive();

    SessionDTO getCurrent();

    SessionDTO createSession(CreateSessionByEmailDTO sessionDTO);

    void logoutFromSession();

    void invalidateExpiredSessions();

}
