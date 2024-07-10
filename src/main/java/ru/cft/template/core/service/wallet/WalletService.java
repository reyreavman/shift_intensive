package ru.cft.template.core.service.wallet;

import ru.cft.template.api.dto.wallet.WalletDTO;
import ru.cft.template.api.dto.wallet.WalletHesoyamDTO;
import ru.cft.template.core.entity.Wallet;

public interface WalletService {
    WalletDTO findById(Long walletId);

    Wallet findByIdEntity(Long walletId);

    Wallet findByUserPhoneNumber(String phoneNumber);

    Wallet findByUserEmail(String email);

    WalletHesoyamDTO hesoyam(Long walletId);
}
