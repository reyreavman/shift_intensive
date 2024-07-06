package ru.cft.template.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.cft.template.api.dto.WalletDTO;
import ru.cft.template.api.dto.WalletHesoyamDTO;
import ru.cft.template.common.Paths;
import ru.cft.template.core.service.wallet.WalletService;

@RestController
@RequestMapping(Paths.WALLETS_PATH)
@RequiredArgsConstructor
public class WalletController {
    private final WalletService walletService;

    /*
    После настройки секьюрити requestParam отсюда уйдет
    */
    @GetMapping
    public ResponseEntity<WalletDTO> getUserWallet(@RequestParam("id") long userId) {
        return ResponseEntity.ok().body(this.walletService.findByUserId(userId));
    }

    /*
    После настройки секьюрити requestParam отсюда уйдет
    */
    @PostMapping("hesoyam")
    public ResponseEntity<WalletHesoyamDTO> createHesoyamRequest(@RequestParam("id") long userId) {
        return ResponseEntity.ok().body(this.walletService.hesoyam(userId));
    }
}
