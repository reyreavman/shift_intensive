package ru.cft.template.core.utils;

import lombok.RequiredArgsConstructor;

import java.util.Random;

@RequiredArgsConstructor
public class HesoyamRouletteGeneratorUtil {
    private final Random random;
    private final int chance;

    public boolean call() {
        return random.nextInt(1, 101) - chance <= 0;
    }
}
