package ru.cft.template.core.beansConfiguration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.cft.template.core.utils.HesoyamRouletteGeneratorUtil;

import java.util.Random;

@Configuration
public class GeneratorBean {
    @Bean
    public HesoyamRouletteGeneratorUtil hesoyamRouletteGeneratorUtil() {
        return new HesoyamRouletteGeneratorUtil(new Random(), 25);
    }
}
