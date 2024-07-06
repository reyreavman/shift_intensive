package ru.cft.template.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.ConverterRegistry;
import ru.cft.template.core.converter.WalletEntityToDTOConverter;
import ru.cft.template.core.converter.user.UserEntityToDTOConverter;
import ru.cft.template.core.converter.user.UserPayloadToEntityConverter;

@Configuration
public class ConversionBeans {
    @Bean
    public ConverterRegistry conversionService(ConverterRegistry converterRegistry,
                                               UserEntityToDTOConverter userEntityToDTOConverter,
                                               UserPayloadToEntityConverter userPayloadToEntityConverter,
                                               WalletEntityToDTOConverter walletEntityToDTOConverter) {
        converterRegistry.addConverter(userEntityToDTOConverter);
        converterRegistry.addConverter(userPayloadToEntityConverter);
        converterRegistry.addConverter(walletEntityToDTOConverter);
        return converterRegistry;
    }
}
