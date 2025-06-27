package org.example.wallet.configuration;

import lombok.extern.slf4j.Slf4j;
import org.example.wallet.exceptions.InsufficientFundsException;
import org.example.wallet.exceptions.WalletNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author mazunin-sv
 * @version 26.06.2025 5:16
 */
@Slf4j
@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(WalletNotFoundException.class)
    public ResponseEntity<?> handleNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Кошелек не найден.");
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<?> handleInsufficient() {
        return ResponseEntity.badRequest()
                .body("Недостаточно средств.");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex) {
        return ResponseEntity.badRequest()
                .body("Неверный запрос.");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAll(Exception ex) {
        return ResponseEntity.internalServerError()
                .body("Внутренняя ошибка сервера.");
    }
}
