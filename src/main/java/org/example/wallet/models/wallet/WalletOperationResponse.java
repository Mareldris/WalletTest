package org.example.wallet.models.wallet;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

/**
 * @author mazunin-sv
 * @version 26.06.2025 8:29
 */

public record WalletOperationResponse(
        @Schema(description = "Id", example = "7209d130-5f54-431a-b6ad-af533bd46d04")
        UUID walletId,
        @Schema(description = "Тип операции", example = "DEPOSIT")
        String operationType,
        @Schema(description = "Сумма операции", example = "1000")
        Long amount,
        @Schema(description = "Баланс после операции", example = "1001")
        Long balance
) {}
