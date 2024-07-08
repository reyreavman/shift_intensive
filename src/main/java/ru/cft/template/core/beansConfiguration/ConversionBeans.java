package ru.cft.template.core.beansConfiguration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.ConverterRegistry;
import ru.cft.template.core.converter.transfer.TransferEntityToDataDTOConverter;
import ru.cft.template.core.converter.transfer.TransferEntityToEmailDTOConverter;
import ru.cft.template.core.converter.transfer.TransferEntityToPhoneNumberDTOConverter;
import ru.cft.template.core.converter.user.UserEntityToDTOConverter;
import ru.cft.template.core.converter.user.UserPayloadToEntityConverter;
import ru.cft.template.core.converter.wallet.WalletEntityToDTOConverter;
import ru.cft.template.core.converter.wallet.WalletEntityToHesoyamDTOConverter;

@Configuration
public class ConversionBeans {
    @Bean
    public ConverterRegistry conversionService(ConverterRegistry converterRegistry,
                                               UserEntityToDTOConverter userEntityToDTOConverter,
                                               UserPayloadToEntityConverter userPayloadToEntityConverter,
                                               WalletEntityToDTOConverter walletEntityToDTOConverter,
                                               WalletEntityToHesoyamDTOConverter walletEntityToHesoyamDTOConverter,
                                               TransferEntityToPhoneNumberDTOConverter transferEntityToPhoneNumberDTOConverter,
                                               TransferEntityToEmailDTOConverter transferEntityToEmailDTOConverter,
                                               TransferEntityToDataDTOConverter transferEntityToDataDTOConverter) {
        converterRegistry.addConverter(userEntityToDTOConverter);
        converterRegistry.addConverter(userPayloadToEntityConverter);
        converterRegistry.addConverter(walletEntityToDTOConverter);
        converterRegistry.addConverter(walletEntityToHesoyamDTOConverter);
        converterRegistry.addConverter(transferEntityToPhoneNumberDTOConverter);
        converterRegistry.addConverter(transferEntityToEmailDTOConverter);
        converterRegistry.addConverter(transferEntityToDataDTOConverter);
        return converterRegistry;
    }
}
