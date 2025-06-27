package org.example.wallet.services;

import org.example.wallet.exceptions.WalletConcurrencyException;
import org.example.wallet.exceptions.WalletNotFoundException;
import org.example.wallet.mappers.WalletMapper;
import org.example.wallet.models.wallet.OperationType;
import org.example.wallet.models.wallet.WalletOperationRequest;
import org.example.wallet.models.wallet.WalletOperationResponse;
import org.example.wallet.models.wallet.WalletResponse;
import org.example.wallet.persistence.entities.WalletEntity;
import org.example.wallet.persistence.repositories.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.OptimisticLockingFailureException;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * @author mazunin-sv
 * @version 27.06.2025 7:08
 */
@ExtendWith(MockitoExtension.class)
class WalletServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private WalletTransactionalOps walletTransactionalOps;

    @Mock
    private WalletMapper walletMapper;

    private WalletService walletService;

    private final UUID walletId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        Integer maxRetry = 5;
        walletService = new WalletService(walletRepository, walletTransactionalOps, walletMapper, maxRetry);
    }

    @Test
    void testGetWalletSuccess() {
        WalletEntity walletEntity = new WalletEntity();
        walletEntity.setWalletId(walletId);
        walletEntity.setBalance(BigDecimal.valueOf(1000));

        WalletResponse expectedResponse = new WalletResponse(walletId, 1000L);

        when(walletRepository.getByWalletId(walletId)).thenReturn(Optional.of(walletEntity));
        when(walletMapper.mapWalletResponseFromWalletEntity(walletEntity)).thenReturn(expectedResponse);

        WalletResponse actual = walletService.getWallet(walletId);

        assertEquals(expectedResponse, actual);
    }

    @Test
    void testGetWalletNotFound() {
        when(walletRepository.getByWalletId(walletId)).thenReturn(Optional.empty());

        assertThrows(WalletNotFoundException.class, () -> walletService.getWallet(walletId));
    }

    @Test
    void testProcessOperationSuccessOnFirstTry() {
        WalletOperationRequest request = new WalletOperationRequest(walletId, OperationType.DEPOSIT, BigDecimal.valueOf(100));
        WalletOperationResponse expected = new WalletOperationResponse(walletId, "DEPOSIT", 100L, 1100L);

        when(walletTransactionalOps.performOperation(request)).thenReturn(expected);

        WalletOperationResponse result = walletService.processOperation(request);

        assertEquals(expected, result);
        verify(walletTransactionalOps, times(1)).performOperation(request);
    }

    @Test
    void testProcessOperationRetryAndSucceed() {
        WalletOperationRequest request = new WalletOperationRequest(walletId, OperationType.WITHDRAW, BigDecimal.valueOf(100));
        WalletOperationResponse expected = new WalletOperationResponse(walletId, "WITHDRAW", 100L, 900L);

        when(walletTransactionalOps.performOperation(request))
                .thenThrow(new OptimisticLockingFailureException("conflict"))
                .thenReturn(expected);

        WalletOperationResponse result = walletService.processOperation(request);

        assertEquals(expected, result);
        verify(walletTransactionalOps, times(2)).performOperation(request);
    }

    @Test
    void testProcessOperationExceedsMaxRetries() {
        WalletOperationRequest request = new WalletOperationRequest(walletId, OperationType.DEPOSIT, BigDecimal.valueOf(100));

        when(walletTransactionalOps.performOperation(request))
                .thenThrow(new OptimisticLockingFailureException("conflict"));

        assertThrows(WalletConcurrencyException.class, () -> walletService.processOperation(request));

        verify(walletTransactionalOps, times(5)).performOperation(request);
    }

    @Test
    void testProcessOperationInterruptedDuringRetry() {
        WalletOperationRequest request = new WalletOperationRequest(walletId, OperationType.DEPOSIT, BigDecimal.valueOf(100));

        when(walletTransactionalOps.performOperation(request))
                .thenAnswer(invocation -> {
                    Thread.currentThread().interrupt();
                    throw new OptimisticLockingFailureException("conflict");
                });

        assertThrows(RuntimeException.class, () -> walletService.processOperation(request));
    }
}