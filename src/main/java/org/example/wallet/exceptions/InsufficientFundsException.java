package org.example.wallet.exceptions;

import lombok.Getter;

/**
 * @author mazunin-sv
 * @version 26.06.2025 5:18
 */

@Getter
public class InsufficientFundsException extends RuntimeException {

    public InsufficientFundsException() {
        super("Недостаточно средств.");
    }

    public InsufficientFundsException(String message) {
        super(message);
    }
}
