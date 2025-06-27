package org.example.wallet.services;

import jakarta.persistence.OptimisticLockException;
import lombok.AllArgsConstructor;
import org.example.wallet.exceptions.WalletConcurrencyException;
import org.example.wallet.exceptions.WalletNotFoundException;
import org.example.wallet.mappers.WalletMapper;
import org.example.wallet.models.wallet.WalletOperationRequest;
import org.example.wallet.models.wallet.WalletOperationResponse;
import org.example.wallet.models.wallet.WalletResponse;
import org.example.wallet.persistence.entities.WalletEntity;
import org.example.wallet.persistence.repositories.WalletRepository;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author mazunin-sv
 * @version 26.06.2025 5:13
 */
@Service
@AllArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;

    private final WalletTransactionalOps walletTransactionalOps;

    private final WalletMapper walletMapper;

    private Integer maxRetry;

    public WalletOperationResponse processOperation(WalletOperationRequest request) {
        int attempts = 0;
        while (true) {
            try {
                return walletTransactionalOps.performOperation(request);
            } catch (OptimisticLockingFailureException | OptimisticLockException e) {
                if (++attempts >= maxRetry) {
                    throw new WalletConcurrencyException("Ошибка одновременного изменения. Повторите попытку позже.");
                }
                try {
                    Thread.sleep(50L * attempts);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Операция прервана", ie);
                }
            }
        }
    }

    public WalletResponse getWallet(UUID walletId) {
        WalletEntity walletEntity = walletRepository.getByWalletId(walletId).orElseThrow(WalletNotFoundException::new);

        return walletMapper.mapWalletResponseFromWalletEntity(walletEntity);
    }
}
