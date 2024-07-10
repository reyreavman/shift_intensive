package ru.cft.template.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.cft.template.api.dto.invoice.CreateInvoiceDTO;
import ru.cft.template.api.dto.invoice.InvoiceDataDTO;
import ru.cft.template.api.dto.invoice.InvoiceTotalDTO;
import ru.cft.template.api.dto.invoice.PayInvoiceDTO;
import ru.cft.template.api.dto.invoice.common.InvoiceDirectionType;
import ru.cft.template.api.dto.invoice.common.InvoiceFilters;
import ru.cft.template.api.dto.invoice.common.InvoiceStatus;
import ru.cft.template.core.service.invoice.InvoiceService;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/kartoshka-api/invoices")
@RequiredArgsConstructor
public class InvoiceController {
    private final InvoiceService invoiceService;

    /*
    После настройки секьюрити requestParam(userId) отсюда уйдет
    */
    @PostMapping
    public InvoiceDataDTO createInvoice(@RequestParam long userId,
                                        @Valid @RequestBody CreateInvoiceDTO createInvoiceDTO) {
        return invoiceService.createInvoice(userId, createInvoiceDTO);
    }

    /*
    После настройки секьюрити requestParam(userId) отсюда уйдет
    */
    @PostMapping("pay")
    public InvoiceDataDTO payInvoice(@RequestParam long userId,
                                     @Valid @RequestBody PayInvoiceDTO invoicePayload) {
        return invoiceService.payInvoice(userId, invoicePayload);
    }

    @GetMapping("{invoiceId:\\d+}")
    public InvoiceDataDTO getInvoiceInfo(@PathVariable("invoiceId") UUID invoiceId) {
        return invoiceService.getInvoiceDataById(invoiceId);
    }

    /*
    После настройки секьюрити requestParam(userId) отсюда уйдет
    */
    @GetMapping
    public List<InvoiceDataDTO> getInvoiceInfoWithFilters(@RequestParam long userId,
                                                          @RequestParam(required = false) InvoiceDirectionType direction,
                                                          @RequestParam(required = false) InvoiceStatus status,
                                                          @RequestParam(required = false) LocalDate start,
                                                          @RequestParam(required = false) LocalDate end) {
        return invoiceService.getUserInvoicesWithFilters(userId, buildInvoiceFilters(direction, status, start, end));
    }

    /*
    После настройки секьюрити requestParam(userId) отсюда уйдет
    */
    @GetMapping("total")
    public InvoiceTotalDTO getInvoiceTotal(@RequestParam long userId,
                                           @RequestParam(required = false) InvoiceDirectionType direction,
                                           @RequestParam(required = false) InvoiceStatus status,
                                           @RequestParam(required = false) LocalDate start,
                                           @RequestParam(required = false) LocalDate end) {
        return invoiceService.getUserInvoicesTotalWithFilters(userId, buildInvoiceFilters(direction, status, start, end));
    }

    /*
    После настройки секьюрити requestParam(userId) отсюда уйдет
    */
    @GetMapping("oldest")
    public InvoiceDataDTO getOldestInvoice(@RequestParam long userId,
                                           @RequestParam(required = false) InvoiceDirectionType direction,
                                           @RequestParam(required = false) InvoiceStatus status,
                                           @RequestParam(required = false) LocalDate start,
                                           @RequestParam(required = false) LocalDate end) {
        return invoiceService.getOldestUserInvoice(userId, buildInvoiceFilters(direction, status, start, end));
    }

    /*
    После настройки секьюрити requestParam(userId) отсюда уйдет
    */
    @DeleteMapping("{invoiceId:\\d+}")
    public InvoiceDataDTO cancelInvoiceByOwner(@PathVariable UUID invoiceId,
                                               @RequestParam long userId) {
        return invoiceService.cancelInvoice(userId, invoiceId);
    }

    private InvoiceFilters buildInvoiceFilters(InvoiceDirectionType directionType, InvoiceStatus status, LocalDate start, LocalDate end) {
        return InvoiceFilters.builder()
                .directionType(directionType)
                .status(status)
                .start(start)
                .end(end)
                .build();
    }
}
