package ru.cft.template.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import ru.cft.template.core.converter.UserEntityToDTOConverter;
import ru.cft.template.core.converter.UserPayloadToEntityConverter;

@Configuration
public class ConversionConfig {
    @Bean
    public ConversionService conversionService() {
        DefaultConversionService defaultConversionService = new DefaultConversionService();
        defaultConversionService.addConverter(new UserEntityToDTOConverter());
        defaultConversionService.addConverter(new UserPayloadToEntityConverter());
        return defaultConversionService;
    }
}
