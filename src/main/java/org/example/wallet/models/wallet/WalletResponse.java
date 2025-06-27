package org.example.wallet.models.wallet;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

/**
 * @author mazunin-sv
 * @version 27.06.2025 6:01
 */
public record WalletResponse(
        @Schema(description = "Id", example = "7209d130-5f54-431a-b6ad-af533bd46d04")
        UUID walletId,
        @Schema(description = "Баланс после операции", example = "1001")
        Long balance
) {
}
