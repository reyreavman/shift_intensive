package ru.cft.template.core.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.cft.template.api.dto.WalletDTO;
import ru.cft.template.core.entity.Wallet;

@Component
public class WalletEntityToDTOConverter implements Converter<Wallet, WalletDTO> {
    @Override
    public WalletDTO convert(Wallet wallet) {
        return new WalletDTO(wallet.getId(), wallet.getUser().getId(), wallet.getBalance());
    }
}
