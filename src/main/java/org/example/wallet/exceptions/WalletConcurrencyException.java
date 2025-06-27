package org.example.wallet.exceptions;

/**
 * @author mazunin-sv
 * @version 27.06.2025 6:41
 */
public class WalletConcurrencyException extends RuntimeException {

    public WalletConcurrencyException() {
        super("Ошибка одновременного изменения. Повторите попытку позже.");
    }

    public WalletConcurrencyException(String message) {
        super(message);
    }
}
