package ru.cft.template.core.service.wallet;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.cft.template.api.dto.wallet.HesoyamResult;
import ru.cft.template.api.dto.wallet.WalletDTO;
import ru.cft.template.api.dto.wallet.WalletHesoyamDTO;
import ru.cft.template.core.entity.Wallet;
import ru.cft.template.core.exception.service.ServiceException;
import ru.cft.template.core.mapper.WalletMapper;
import ru.cft.template.core.repository.WalletRepository;
import ru.cft.template.core.utils.HesoyamRouletteGeneratorUtil;

@Service
@RequiredArgsConstructor
public class DefaultWalletService implements WalletService {
    private final WalletRepository walletRepository;

    private final WalletMapper walletMapper;
    private final HesoyamRouletteGeneratorUtil hesoyamGenerator;

    @Override
    public WalletDTO findById(Long walletId) {
        return walletMapper.mapToWalletDTO(findWalletById(walletId));
    }

    @Override
    public Wallet findByIdEntity(Long walletId) {
        return findWalletById(walletId);
    }

    @Override
    public Wallet findByUserPhoneNumber(String phoneNumber) {
        return walletRepository.findByUserPhoneNumber(phoneNumber).orElseThrow(() -> new ServiceException("Кошелек пользователя не найден."));
    }

    @Override
    public Wallet findByUserEmail(String email) {
        return walletRepository.findByUserEmail(email).orElseThrow(() -> new ServiceException("Кошелек пользователя не найден."));
    }

    @Override
    @Transactional
    public WalletHesoyamDTO hesoyam(Long walletId) {
        Long hesoyamWinnerAward = 10L;
        Wallet wallet = findWalletById(walletId);
        HesoyamResult hesoyamResult = hesoyamGenerator.call();
        if (hesoyamResult.equals(HesoyamResult.WINNER)) {
            wallet.setBalance(wallet.getBalance() + hesoyamWinnerAward);
        }
        return walletMapper.mapToWalletHesoyam(wallet, hesoyamResult, hesoyamWinnerAward);
    }

    private Wallet findWalletById(Long walletId) {
        return walletRepository.findById(walletId).orElseThrow(() -> new ServiceException("Кошелек пользователя %d не найден.".formatted(walletId)));
    }
}
