package ru.cft.template.common;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@RequiredArgsConstructor
public class HesoyamRouletteGenerator {
    private final Random random;
    private final int chance;

    public boolean call() {
        return random.nextInt(1, 101) - chance <= 0;
    }
}
