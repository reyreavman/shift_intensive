package ru.cft.template.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cft.template.api.dto.session.CreateSessionByEmailDTO;
import ru.cft.template.api.dto.session.SessionDTO;
import ru.cft.template.core.service.session.SessionService;

import java.util.List;

@RestController
@RequestMapping("/kartoshka-api/sessions")
@RequiredArgsConstructor
public class SessionController {
    private final SessionService sessionService;

    @GetMapping
    public List<SessionDTO> getAll() {
        return sessionService.getAllActive();
    }

    @GetMapping("current")
    public SessionDTO getCurrentSession() {
        return sessionService.getCurrent();
    }

    @PostMapping
    public SessionDTO createSession(@Valid @RequestBody CreateSessionByEmailDTO sessionDTO) {
        return sessionService.createSession(sessionDTO);
    }

    @PostMapping("logout")
    public void logoutFromSession() {
        sessionService.logoutFromSession();
    }
}
