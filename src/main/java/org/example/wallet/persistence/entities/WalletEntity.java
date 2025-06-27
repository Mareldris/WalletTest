package org.example.wallet.persistence.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author mazunin-sv
 * @version 26.06.2025 4:45
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "wallets")
public class WalletEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "wallet_id", updatable = false, nullable = false)
    private UUID walletId;

    @Version
    private Integer version;

    @Digits(integer = 20, fraction = 12)
    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;
}
