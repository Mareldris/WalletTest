package org.example.wallet.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.wallet.exceptions.InsufficientFundsException;
import org.example.wallet.exceptions.WalletNotFoundException;
import org.example.wallet.mappers.WalletMapper;
import org.example.wallet.models.wallet.WalletOperationRequest;
import org.example.wallet.models.wallet.WalletOperationResponse;
import org.example.wallet.persistence.entities.WalletEntity;
import org.example.wallet.persistence.repositories.WalletRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author mazunin-sv
 * @version 27.06.2025 6:53
 */

@Service
@AllArgsConstructor
public class WalletTransactionalOps {

    private final WalletRepository walletRepository;

    private final WalletMapper walletMapper;

    @Transactional
    public WalletOperationResponse performOperation(WalletOperationRequest request) {
        WalletEntity wallet = walletRepository.getByWalletId(request.walletId())
                .orElseThrow(() -> new WalletNotFoundException("Кошелек не найден, walletId -  " + request.walletId()));

        BigDecimal newBalance = switch (request.operationType()) {
            case DEPOSIT -> wallet.getBalance().add(request.amount());
            case WITHDRAW -> {
                if (wallet.getBalance().compareTo(request.amount()) < 0) {
                    throw new InsufficientFundsException("Недостаточно средств.");
                }
                yield wallet.getBalance().subtract(request.amount());
            }
        };

        wallet.setBalance(newBalance);
        walletRepository.save(wallet);

        return walletMapper.mapWalletOperationResponseFromWalletEntity(wallet, request);
    }
}
