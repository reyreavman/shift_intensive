package ru.cft.template.core.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordUtil {
    private final PasswordEncoder passwordEncoder;

    public String encode(String password) {
        return this.passwordEncoder.encode(password);
    }

    public boolean matches(String password, String encodedPassword) {
        return this.passwordEncoder.matches(password, encodedPassword);
    }
}
