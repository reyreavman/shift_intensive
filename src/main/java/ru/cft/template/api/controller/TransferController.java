package ru.cft.template.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.cft.template.api.dto.transfer.CreateTransferByEmailDTO;
import ru.cft.template.api.dto.transfer.CreateTransferByPhoneNumberDTO;
import ru.cft.template.api.dto.transfer.TransferByEmailDTO;
import ru.cft.template.api.dto.transfer.TransferByPhoneNumberDTO;
import ru.cft.template.api.dto.transfer.TransferDataDTO;
import ru.cft.template.common.Paths;
import ru.cft.template.core.entity.transfer.TransferDirectionType;
import ru.cft.template.core.entity.transfer.TransferStatus;
import ru.cft.template.core.service.transfer.TransferService;

import java.util.List;

@RestController
@RequestMapping(Paths.TRANSFER_PATH)
@RequiredArgsConstructor
public class TransferController {
    private final TransferService transferService;

    /*
    После настройки секьюрити requestParam отсюда уйдет
    */
    @PostMapping("/email")
    public TransferByEmailDTO createTransferByEmail(@RequestParam long senderId,
                                                    @Valid @RequestBody CreateTransferByEmailDTO createTransferByEmailDTO) {
        return transferService.createTransferToUserByEmail(senderId, createTransferByEmailDTO);
    }

    @PostMapping("/recipientPhoneNumber")
    public TransferByPhoneNumberDTO createTransferByPhoneNumber(@RequestParam long senderId,
                                                                @Valid @RequestBody CreateTransferByPhoneNumberDTO createTransferByPhoneNumberDTO) {
        return transferService.createTransferToUserByPhoneNumber(senderId, createTransferByPhoneNumberDTO);
    }

    @GetMapping("{transferId}")
    public TransferDataDTO getTransferInfo(@PathVariable long transferId) {
        return this.transferService.findTransferById(transferId);
    }

    /*
    После настройки секьюрити requestParam("userId") отсюда уйдет
    */
    @GetMapping
    public List<TransferDataDTO> getTransfersInfoWithFilters(@RequestParam long userId,
                                                             @RequestParam(required = false) TransferStatus status,
                                                             @RequestParam(required = false) TransferDirectionType directionType) {
        return transferService.findTransfersByDirectionTypeAndStatus(userId, directionType, status);
    }
}
