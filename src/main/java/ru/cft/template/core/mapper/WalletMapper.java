package ru.cft.template.core.mapper;

import org.springframework.stereotype.Component;
import ru.cft.template.api.dto.wallet.HesoyamResult;
import ru.cft.template.api.dto.wallet.WalletDTO;
import ru.cft.template.api.dto.wallet.WalletHesoyamDTO;
import ru.cft.template.core.entity.Wallet;

@Component
public class WalletMapper {
    public WalletHesoyamDTO mapToWalletHesoyam(Wallet wallet, HesoyamResult result, Long amount) {
        return new WalletHesoyamDTO(wallet.getId(), result, result.equals(HesoyamResult.WINNER) ? amount : 0L, wallet.getBalance());
    }

    public WalletDTO mapToWalletDTO(Wallet wallet) {
        return new WalletDTO(wallet.getId(), wallet.getBalance());
    }
}
