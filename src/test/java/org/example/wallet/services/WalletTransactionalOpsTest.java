package org.example.wallet.services;

import org.example.wallet.exceptions.InsufficientFundsException;
import org.example.wallet.exceptions.WalletNotFoundException;
import org.example.wallet.mappers.WalletMapper;
import org.example.wallet.models.wallet.OperationType;
import org.example.wallet.models.wallet.WalletOperationRequest;
import org.example.wallet.models.wallet.WalletOperationResponse;
import org.example.wallet.persistence.entities.WalletEntity;
import org.example.wallet.persistence.repositories.WalletRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * @author mazunin-sv
 * @version 27.06.2025 7:00
 */
@ExtendWith(MockitoExtension.class)
class WalletTransactionalOpsTest {

    @InjectMocks
    private WalletTransactionalOps walletTransactionalOps;
    @Mock
    private WalletRepository walletRepository;
    @Mock
    private WalletMapper walletMapper;

    private final UUID walletId = UUID.randomUUID();

    @Test
    void testDepositSuccess() {
        BigDecimal initialBalance = new BigDecimal("1000.00");
        BigDecimal amount = new BigDecimal("500.00");

        WalletEntity wallet = new WalletEntity(walletId, null, initialBalance);
        WalletOperationRequest request = new WalletOperationRequest(walletId, OperationType.DEPOSIT, amount);

        WalletEntity savedWallet = new WalletEntity(walletId, null, initialBalance.add(amount));
        WalletOperationResponse expectedResponse = new WalletOperationResponse(walletId, "DEPOSIT", 500L, 1500L);

        when(walletRepository.getByWalletId(walletId)).thenReturn(Optional.of(wallet));
        when(walletRepository.save(any())).thenReturn(savedWallet);
        when(walletMapper.mapWalletOperationResponseFromWalletEntity(any(), eq(request))).thenReturn(expectedResponse);

        WalletOperationResponse actual = walletTransactionalOps.performOperation(request);

        assertEquals(expectedResponse, actual);
        assertEquals(new BigDecimal("1500.00"), wallet.getBalance());
    }

    @Test
    void testWithdrawSuccess() {

        BigDecimal initialBalance = new BigDecimal("1000.00");
        BigDecimal amount = new BigDecimal("200.00");

        WalletEntity wallet = new WalletEntity(walletId, null, initialBalance);
        WalletOperationRequest request = new WalletOperationRequest(walletId, OperationType.WITHDRAW, amount);

        WalletOperationResponse expectedResponse = new WalletOperationResponse(walletId, "WITHDRAW", 200L, 800L);

        when(walletRepository.getByWalletId(walletId)).thenReturn(Optional.of(wallet));
        when(walletMapper.mapWalletOperationResponseFromWalletEntity(any(), eq(request))).thenReturn(expectedResponse);

        WalletOperationResponse actual = walletTransactionalOps.performOperation(request);

        assertEquals(expectedResponse, actual);
        assertEquals(new BigDecimal("800.00"), wallet.getBalance());
    }

    @Test
    void testWalletNotFoundThrowsException() {
        WalletOperationRequest request = new WalletOperationRequest(walletId, OperationType.DEPOSIT, BigDecimal.TEN);

        when(walletRepository.getByWalletId(walletId)).thenReturn(Optional.empty());

        assertThrows(WalletNotFoundException.class, () -> walletTransactionalOps.performOperation(request));
    }

    @Test
    void testWithdrawInsufficientFundsThrowsException() {
        BigDecimal balance = new BigDecimal("100.00");
        BigDecimal amount = new BigDecimal("200.00");

        WalletEntity wallet = new WalletEntity(walletId, null, balance);
        WalletOperationRequest request = new WalletOperationRequest(walletId, OperationType.WITHDRAW, amount);

        when(walletRepository.getByWalletId(walletId)).thenReturn(Optional.of(wallet));

        assertThrows(InsufficientFundsException.class, () -> walletTransactionalOps.performOperation(request));
    }
}