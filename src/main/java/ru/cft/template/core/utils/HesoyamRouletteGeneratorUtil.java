package ru.cft.template.core.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.cft.template.api.dto.wallet.HesoyamResult;

import java.util.Random;

@Component
@RequiredArgsConstructor
public class HesoyamRouletteGeneratorUtil {
    private final Random random = new Random();
    @Getter
    private final int chance = 25;

    public HesoyamResult call() {
        return random.nextInt(1, 101) <= chance ? HesoyamResult.WINNER : HesoyamResult.LOSER;
    }
}
