package ru.cft.template.core.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.flywaydb.core.internal.util.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.cft.template.core.Context;
import ru.cft.template.core.entity.Session;
import ru.cft.template.core.entity.User;
import ru.cft.template.core.service.session.SessionService;

import java.io.IOException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SessionAuthenticationFilter extends OncePerRequestFilter {
    private final SessionService sessionService;
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String sessionToken = getTokenFromRequest(request);
        if (Objects.isNull(sessionToken)) {
            filterChain.doFilter(request, response);
        }
        Session session = sessionService.getValidSession(sessionToken);
        if (Objects.nonNull(session)) {
            User user = session.getUser();
            UsernamePasswordAuthenticationToken authenticationToken = UsernamePasswordAuthenticationToken.authenticated(
              user, null, user.getAuthorities()
            );
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            Context.get().setSession(session);
            Context.get().setUser(user);
        }
        filterChain.doFilter(request, response);
    }


    private String getTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader("X-Session-Token");
        return StringUtils.hasText(token) ? token : null;
    }
}
