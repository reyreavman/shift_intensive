package ru.cft.template.core.mapper;

import org.springframework.stereotype.Component;
import ru.cft.template.api.dto.session.SessionDTO;
import ru.cft.template.core.entity.Session;

@Component
public class SessionMapper {

    public SessionDTO mapToSessionDTO(Session session) {
        return new SessionDTO(session.getToken(), session.getExpirationTime(), session.isActive());
    }
}
