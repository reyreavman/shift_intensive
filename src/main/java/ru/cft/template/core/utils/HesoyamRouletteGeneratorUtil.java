package ru.cft.template.core.utils;

import lombok.RequiredArgsConstructor;
import ru.cft.template.api.dto.wallet.HesoyamResult;

import java.util.Random;

@RequiredArgsConstructor
public class HesoyamRouletteGeneratorUtil {
    private final Random random;
    private final int chance;

    public HesoyamResult call() {
        return random.nextInt(1, 101) <= chance ? HesoyamResult.WINNER : HesoyamResult.LOSER;
    }
}
