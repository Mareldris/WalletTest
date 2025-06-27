package org.example.wallet.persistence.repositories;

import org.example.wallet.persistence.entities.WalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * @author mazunin-sv
 * @version 26.06.2025 5:14
 */
@Repository
public interface WalletRepository extends JpaRepository<WalletEntity, Long> {

    Optional<WalletEntity> getByWalletId(UUID id);
}
