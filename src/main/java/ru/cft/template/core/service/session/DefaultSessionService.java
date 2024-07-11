package ru.cft.template.core.service.session;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.cft.template.api.dto.session.CreateSessionByEmailDTO;
import ru.cft.template.api.dto.session.SessionDTO;
import ru.cft.template.core.Context;
import ru.cft.template.core.entity.Session;
import ru.cft.template.core.entity.User;
import ru.cft.template.core.mapper.SessionMapper;
import ru.cft.template.core.repository.SessionRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DefaultSessionService implements SessionService {
    private final SessionRepository sessionRepository;
    private final SessionMapper sessionMapper;
    private final AuthenticationManager authenticationManager;

    @Override
    public Session getValidSession(String token) {
        Session session = sessionRepository.findByToken(token);
        if (Objects.nonNull(session) && session.getExpirationTime().isBefore(LocalDateTime.now())) {
            invalidate(session);
            return null;
        }
        return session;
    }

    @Override
    public List<SessionDTO> getAllActive() {
        User user = Context.get().getUser();
        return sessionRepository
                .findAllByUserAndActiveTrueAndExpirationTimeAfter(user, LocalDateTime.now())
                .stream()
                .map(sessionMapper::mapToSessionDTO)
                .toList();
    }

    @Override
    public SessionDTO getCurrent() {
        return sessionMapper.mapToSessionDTO(Context.get().getSession());
    }

    @Override
    public SessionDTO createSession(CreateSessionByEmailDTO sessionDTO) {
        Authentication authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(sessionDTO.email(), sessionDTO.password()));

        Session session = buildNewSession((User) authentication.getPrincipal());
        sessionRepository.save(session);
        return sessionMapper.mapToSessionDTO(session);
    }

    @Override
    public void logoutFromSession() {
        Session session = Context.get().getSession();
        invalidate(session);
    }

    @Override
    public void invalidateExpiredSessions() {
        sessionRepository.updateAllByExpirationTimeAfter(LocalDateTime.now());
    }

    private void invalidate(Session session) {
        session.setActive(false);
    }

    private Session buildNewSession(User principal) {
        return Session.builder()
                .active(true)
                .expirationTime(LocalDateTime.now().plusHours(1))
                .user(principal)
                .build();
    }
}
