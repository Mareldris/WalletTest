package org.example.wallet.exceptions;

import lombok.Getter;

/**
 * @author mazunin-sv
 * @version 26.06.2025 5:17
 */
@Getter
public class WalletNotFoundException extends RuntimeException {

    public WalletNotFoundException() {
        super("Кошелек не найден.");
    }

    public WalletNotFoundException(String message) {
        super(message);
    }
}