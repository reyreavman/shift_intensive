package ru.cft.template.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import ru.cft.template.api.dto.transfer.TransferByEmailDTO;
import ru.cft.template.api.dto.transfer.TransferByPhoneNumberDTO;
import ru.cft.template.api.payload.transfer.NewTransferByEmailPayload;
import ru.cft.template.api.payload.transfer.NewTransferByPhoneNumberPayload;
import ru.cft.template.common.Paths;
import ru.cft.template.core.entity.transfer.TransferDirectionType;
import ru.cft.template.core.entity.transfer.TransferStatus;
import ru.cft.template.core.exception.MultipleParamsException;
import ru.cft.template.core.exception.NoRequestBodyException;
import ru.cft.template.core.service.transfer.TransferService;

import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping(Paths.TRANSFER_PATH)
@RequiredArgsConstructor
public class TransferController {
    private final TransferService transferService;

    /*
    После настройки секьюрити requestParam отсюда уйдет
    */
    @PostMapping
    public ResponseEntity<?> createTransfer(@RequestParam("senderId") long senderId,
                                            @RequestBody(required = false) NewTransferByEmailPayload transferByEmailPayload,
                                            @RequestBody(required = false) NewTransferByPhoneNumberPayload transferByPhoneNumberPayload,
                                            UriComponentsBuilder uriComponentsBuilder) {
        if (Objects.isNull(transferByEmailPayload) && Objects.isNull(transferByPhoneNumberPayload))
            throw new NoRequestBodyException();
        if (Objects.nonNull(transferByEmailPayload)) {
            TransferByEmailDTO transfer = this.transferService.createTransferToUserByEmail(senderId, transferByEmailPayload);
            return ResponseEntity.created(
                            uriComponentsBuilder.replacePath(Paths.TRANSFER_PATH.concat("/{transferId}")).build(Map.of("transferId", transfer.id())))
                    .body(transfer);
        }
        TransferByPhoneNumberDTO transfer = this.transferService.createTransferToUserByPhoneNumber(senderId, transferByPhoneNumberPayload);
        return ResponseEntity.created(
                        uriComponentsBuilder.replacePath(Paths.TRANSFER_PATH.concat("/{transferId}")).build(Map.of("transferId", transfer.id())))
                .body(transfer);
    }

    /*
    После настройки секьюрити requestParam("userId") отсюда уйдет
    */
    @GetMapping
    public ResponseEntity<?> getTransferInfo(@RequestParam("userId") long userId,
                                             @RequestParam Map<String, String> params) {
        if (params.containsKey("transferId") && params.keySet().size() > 2)
            throw new MultipleParamsException(params.values().stream().filter(Objects::nonNull).toList());
        if (params.containsKey("status") && params.containsKey("type"))
            return ResponseEntity.ok().body(this.transferService.findTransfersByDirectionTypeAndStatus(userId, TransferDirectionType.valueOf(params.get("type").toUpperCase()), TransferStatus.valueOf(params.get("status").toUpperCase())));
        if (params.containsKey("transferId"))
            return ResponseEntity.ok().body(this.transferService.findTransferById(Long.valueOf(params.get("transferId"))));
        if (params.containsKey("type"))
            return ResponseEntity.ok().body(this.transferService.findTransfersByDirectionType(userId, TransferDirectionType.valueOf(params.get("type").toUpperCase())));
        if (params.containsKey("status"))
            return ResponseEntity.ok().body(this.transferService.findTransfersByStatus(userId, TransferStatus.valueOf(params.get("status").toUpperCase())));
        return ResponseEntity.ok().body(this.transferService.findAllTransfersByUserId(userId));
    }
}
