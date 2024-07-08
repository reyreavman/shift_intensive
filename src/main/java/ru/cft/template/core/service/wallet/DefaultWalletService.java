package ru.cft.template.core.service.wallet;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.cft.template.api.dto.wallet.WalletDTO;
import ru.cft.template.api.dto.wallet.WalletHesoyamDTO;
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
    @Transactional
    public WalletDTO findByUserId(Long userId) {
        return walletMapper.mapToWalletDTO(walletRepository.findById(userId).orElseThrow());
    }

    @Override
    @Transactional
    public WalletHesoyamDTO hesoyam(Long userId) {
        return walletMapper.mapToWalletHesoyam(walletRepository.findById(userId).orElseThrow(), this.hesoyamGenerator.call());
    }
}
