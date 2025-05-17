package com.challenge.banregio.controller;

import com.challenge.banregio.dto.PaymentRequestDTO;
import com.challenge.banregio.service.LoanPaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
    name = "Banregio Challenge Loans",
    description = "Endpoints for Banregio Challenge"
)
@RestController
@RequestMapping(
    path = "/api/loan",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
)
@RequiredArgsConstructor
public class LoanPaynmentController {

    private final LoanPaymentService service;

    @Operation(summary = "Loan Payment", description = "Endpoint for loan payment")
    @ApiResponse(responseCode = "200", description = "Loan payment executed successfully")
    @ApiResponse(responseCode = "400", description = "Invalid request")
    @PostMapping(path = "/payment")
    public ResponseEntity<Void> paymentLoan(@RequestBody @Valid final PaymentRequestDTO request) {
        service.executePayment(request);
        return ResponseEntity.ok().build();
    }
}
