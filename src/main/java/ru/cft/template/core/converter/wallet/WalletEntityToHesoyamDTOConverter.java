package ru.cft.template.core.converter.wallet;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.cft.template.api.dto.wallet.WalletHesoyamDTO;
import ru.cft.template.core.entity.Wallet;

@Component
public class WalletEntityToHesoyamDTOConverter implements Converter<Wallet, WalletHesoyamDTO> {
    @Override
    public WalletHesoyamDTO convert(Wallet wallet) {
        return new WalletHesoyamDTO(wallet.getId(), wallet.isHesoyamWinner() ? "win" : "lose", wallet.isHesoyamWinner() ? wallet.getHesoyamWinningAmount() : 0, wallet.getBalance());
    }
}
