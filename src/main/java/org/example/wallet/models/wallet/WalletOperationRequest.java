package org.example.wallet.models.wallet;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author mazunin-sv
 * @version 26.06.2025 5:11
 */
public record WalletOperationRequest(
        @Schema(description = "Id", example = "7209d130-5f54-431a-b6ad-af533bd46d04")
        @NotNull
        UUID walletId,
        @Schema(description = "Тип операции", example = "DEPOSIT")
        @NotNull
        OperationType operationType,
        @Schema(description = "Сумма операции(min - 1)", example = "1000")
        @Min(1) BigDecimal amount
) {}
