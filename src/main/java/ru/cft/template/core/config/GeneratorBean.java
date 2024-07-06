package ru.cft.template.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.cft.template.common.HesoyamRouletteGenerator;

import java.util.Random;

@Configuration
public class GeneratorBean {
    @Bean
    public HesoyamRouletteGenerator hesoyamRouletteGenerator() {
        return new HesoyamRouletteGenerator(new Random(), 25);
    }
}
