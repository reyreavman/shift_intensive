package ru.cft.template.core.service.wallet;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import ru.cft.template.api.dto.WalletDTO;
import ru.cft.template.api.dto.WalletHesoyamDTO;
import ru.cft.template.common.HesoyamRouletteGenerator;
import ru.cft.template.core.entity.User;
import ru.cft.template.core.entity.Wallet;
import ru.cft.template.core.exception.EntityNotFoundException;
import ru.cft.template.core.repository.UserRepository;
import ru.cft.template.core.repository.WalletRepository;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class DefaultWalletService implements WalletService {
    private final WalletRepository walletRepository;
    private final UserRepository userRepository;

    private final ConversionService conversionService;
    private final HesoyamRouletteGenerator hesoyamGenerator;

    @Override
    public WalletDTO findByUserId(Long userId) {
        Wallet wallet = this.walletRepository.findById(userId).orElseGet(() -> {
            User user = this.userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(User.class.getSimpleName(), Map.of("userId", String.valueOf(userId))));
            return this.walletRepository.save(new Wallet(null, user, 100L));
        });
        return this.conversionService.convert(wallet, WalletDTO.class);
    }

    @Override
    public WalletHesoyamDTO hesoyam(Long userId) {
        Wallet wallet = this.walletRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(Wallet.class.getSimpleName(), Map.of("userId", String.valueOf(userId))));
        if (this.hesoyamGenerator.call()) {
            wallet.setHesoyamWinner(true);
            wallet.setBalance(wallet.getBalance() + wallet.getHesoyamWinningAmount());
        }
        return this.conversionService.convert(wallet, WalletHesoyamDTO.class);
    }
}