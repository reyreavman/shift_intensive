package ru.cft.template.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.cft.template.api.dto.wallet.WalletDTO;
import ru.cft.template.api.dto.wallet.WalletHesoyamDTO;
import ru.cft.template.core.service.wallet.WalletService;

@RestController
@RequestMapping("/kartoshka-api/wallets")
@RequiredArgsConstructor
public class WalletController {
    private final WalletService walletService;

    /*
    После настройки секьюрити requestParam отсюда уйдет
    */
    @PostMapping("hesoyam")
    public WalletHesoyamDTO createHesoyamRequest(@RequestParam("userId") long userId) {
        return walletService.hesoyam(userId);
    }

    /*
    После настройки секьюрити requestParam отсюда уйдет
    */
    @GetMapping
    public WalletDTO getUserWallet(@RequestParam("userId") long userId) {
        return walletService.findById(userId);
    }
}
