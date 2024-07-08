package ru.cft.template.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import ru.cft.template.api.dto.invoice.InvoiceDataDTO;
import ru.cft.template.api.dto.invoice.InvoiceTotalDTO;
import ru.cft.template.api.payload.invoice.NewInvoicePayload;
import ru.cft.template.api.payload.invoice.NewPayInvoicePayload;
import ru.cft.template.common.Paths;
import ru.cft.template.core.entity.invoice.InvoiceFilters;
import ru.cft.template.core.entity.invoice.InvoiceStatus;
import ru.cft.template.core.service.invoice.InvoiceService;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(Paths.INVOICES_PATH)
@RequiredArgsConstructor
public class InvoiceController {
    private final InvoiceService invoiceService;

    @PostMapping
    public ResponseEntity<InvoiceDataDTO> createInvoice(@RequestParam("userId") Long userId,
                                                        @RequestBody NewInvoicePayload invoicePayload,
                                                        UriComponentsBuilder uriComponentsBuilder) {
        InvoiceDataDTO invoiceDTO = this.invoiceService.createInvoice(userId, invoicePayload);
        return ResponseEntity.created(uriComponentsBuilder
                        .replacePath(Paths.INVOICES_PATH.concat("/{invoiceId}"))
                        .build(Map.of("invoiceId", invoiceDTO.invoiceId())))
                .body(invoiceDTO);
    }

    @PostMapping("pay")
    public ResponseEntity<InvoiceDataDTO> payInvoice(@RequestParam("userID") Long userId,
                                                     @RequestBody NewPayInvoicePayload invoicePayload) {
        InvoiceDataDTO invoiceDTO = this.invoiceService.payInvoice(userId, invoicePayload);
        return ResponseEntity.ok().body(invoiceDTO);
    }

    @GetMapping("{invoiceId}")
    public ResponseEntity<InvoiceDataDTO> getInvoiceInfo(@PathVariable("invoiceId") UUID invoiceId) {
        return ResponseEntity.ok().body(this.invoiceService.getInvoiceDataById(invoiceId));
    }

    @GetMapping
    public ResponseEntity<?> getInvoiceInfo(@RequestParam("userId") Long userId,
                                            @RequestParam Map<String, String> params) {
        return ResponseEntity.ok().body(this.invoiceService.getUserInvoicesWithFilters(Long.valueOf(params.get("userId")),
                this.buildInvoiceFiltersFromRequestParamsMap(params)));
    }

    @GetMapping("total")
    public ResponseEntity<InvoiceTotalDTO> getInvoiceTotal(@RequestParam("userId") Long userId,
                                                           @RequestParam Map<String, String> params) {
        return ResponseEntity.ok().body(this.invoiceService.getUserInvoicesTotalWithFilters(userId,
                this.buildInvoiceFiltersFromRequestParamsMap(params)));
    }

    @GetMapping("oldest")
    public ResponseEntity<InvoiceDataDTO> getOldestInvoice(@RequestParam("userId") Long userId,
                                                           @RequestParam Map<String, String> params) {
        return ResponseEntity.ok().body(this.invoiceService.getOldestUserInvoice(userId,
                this.buildInvoiceFiltersFromRequestParamsMap(params)));
    }

    @DeleteMapping("{invoiceId}")
    public ResponseEntity<InvoiceDataDTO> cancelInvoiceByOwner(@PathVariable("invoiceId") UUID invoiceId,
                                                 @RequestParam("userId") Long userId) {
        return ResponseEntity.ok().body(this.invoiceService.cancelInvoice(userId, invoiceId));
    }

    private InvoiceFilters buildInvoiceFiltersFromRequestParamsMap(Map<String, String> params) {
        return InvoiceFilters.builder()
                .createdBy(params.containsKey("createdBy"))
                .issuedTo(params.containsKey("issuedTo"))
                .status(params.containsKey("status") ? InvoiceStatus.valueOf(params.get("status")) : null)
                .start(params.containsKey("start") ? LocalDate.parse(params.get("start")) : null)
                .end(params.containsKey("end") ? LocalDate.parse(params.get("end")) : null)
                .build();
    }
}
