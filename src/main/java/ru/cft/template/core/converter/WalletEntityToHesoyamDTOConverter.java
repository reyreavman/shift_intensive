package ru.cft.template.core.converter;

import org.springframework.core.convert.converter.Converter;
import ru.cft.template.api.dto.WalletHesoyamDTO;
import ru.cft.template.core.entity.Wallet;

public class WalletEntityToHesoyamDTOConverter implements Converter<Wallet, WalletHesoyamDTO> {
    @Override
    public WalletHesoyamDTO convert(Wallet wallet) {
        return new WalletHesoyamDTO(wallet.getId(), wallet.isHesoyamWinner() ? "win" : "lose", wallet.getHesoyamWinningAmount(), wallet.getBalance());
    }
}
