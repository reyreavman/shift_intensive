package ru.cft.template.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.DefaultConversionService;
import ru.cft.template.core.converter.UserEntityToDTOConverter;
import ru.cft.template.core.converter.UserPayloadToEntityConverter;

@Configuration
public class ConversionConfig {
    @Bean
    public DefaultConversionService conversionService(UserEntityToDTOConverter userEntityToDTOConverter, UserPayloadToEntityConverter userPayloadToEntityConverter) {
        DefaultConversionService defaultConversionService = new DefaultConversionService();
        defaultConversionService.addConverter(userEntityToDTOConverter);
        defaultConversionService.addConverter(userPayloadToEntityConverter);
        return defaultConversionService;
    }
}
