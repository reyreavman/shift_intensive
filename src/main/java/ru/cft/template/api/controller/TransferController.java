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
import ru.cft.template.core.service.transfer.TransferAmongUsersService;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

@RestController
@RequestMapping(Paths.TRANSFER_PATH)
@RequiredArgsConstructor
public class TransferController {
    private final TransferAmongUsersService transferAmongUsersService;

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
            TransferByEmailDTO transfer = this.transferAmongUsersService.createTransferToUserByEmail(senderId, transferByEmailPayload);
            return ResponseEntity.created(
                            uriComponentsBuilder.replacePath(Paths.TRANSFER_PATH.concat("/{transferId}")).build(Map.of("transferId", transfer.id())))
                    .body(transfer);
        }
        TransferByPhoneNumberDTO transfer = this.transferAmongUsersService.createTransferToUserByPhoneNumber(senderId, transferByPhoneNumberPayload);
        return ResponseEntity.created(
                        uriComponentsBuilder.replacePath(Paths.TRANSFER_PATH.concat("/{transferId}")).build(Map.of("transferId", transfer.id())))
                .body(transfer);
    }

    /*
    После настройки секьюрити requestParam("userId") отсюда уйдет
    */
    @GetMapping
    public ResponseEntity<?> getTransferInfo(@RequestParam("userId") long userId,
                                             @RequestParam("transferId") Long transferId,
                                             @RequestParam("status") TransferStatus status,
                                             @RequestParam("type") TransferDirectionType type) {
        if (Objects.nonNull(transferId) && (Objects.nonNull(status) || Objects.nonNull(type)))
            throw new MultipleParamsException(Stream.of(transferId, status, type).filter(Objects::nonNull).map(String::valueOf).toList());
        if (Objects.nonNull(status) && Objects.nonNull(type))
            return ResponseEntity.ok().body(this.transferAmongUsersService.findTransfersByDirectionTypeAndStatus(userId, type, status));
        if (Objects.nonNull(transferId))
            return ResponseEntity.ok().body(this.transferAmongUsersService.findTransferById(transferId));
        if (Objects.nonNull(type))
            return ResponseEntity.ok().body(this.transferAmongUsersService.findTransfersByDirectionType(userId, type));
        if (Objects.nonNull(status))
            return ResponseEntity.ok().body(this.transferAmongUsersService.findTransfersByStatus(userId, status));
        return ResponseEntity.ok().body(this.transferAmongUsersService.findAllTransfersByUserId(userId));
    }
}
