package org.example.wallet.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.wallet.models.wallet.WalletOperationRequest;
import org.example.wallet.models.wallet.WalletOperationResponse;
import org.example.wallet.models.wallet.WalletResponse;
import org.example.wallet.services.WalletService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * @author mazunin-sv
 * @version 26.06.2025 5:12
 */
@Tag(name = WalletController.TAG)
@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class WalletController {

    public static final String TAG = "Wallet API";

    private final WalletService walletService;

    @Operation(summary = "Операция снятия/пополнения", tags = WalletController.TAG, responses = {
            @ApiResponse(description = "DTO is valid", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = WalletOperationResponse.class))),
            @ApiResponse(description = "DTO is NOT valid", responseCode = "400", content = @Content)
    })
    @PostMapping("/wallet")
    public ResponseEntity<WalletOperationResponse> processWalletOp(@Valid @RequestBody WalletOperationRequest request) {
        WalletOperationResponse response = walletService.processOperation(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/wallets/{wallet_uuid}")
    public ResponseEntity<WalletResponse> getWallet(@PathVariable("wallet_uuid") UUID walletUuid) {
        WalletResponse response = walletService.getWallet(walletUuid);
        return ResponseEntity.ok(response);
    }
}
