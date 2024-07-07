package ru.cft.template.core.service.wallet;

import ru.cft.template.api.dto.wallet.WalletDTO;
import ru.cft.template.api.dto.wallet.WalletHesoyamDTO;

public interface WalletService {
    WalletDTO findByUserId(Long userId);

    WalletHesoyamDTO hesoyam(Long userId);
}
