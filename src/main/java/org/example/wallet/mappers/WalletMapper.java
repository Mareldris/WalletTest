package org.example.wallet.mappers;

import org.example.wallet.models.wallet.WalletOperationRequest;
import org.example.wallet.models.wallet.WalletOperationResponse;
import org.example.wallet.models.wallet.WalletResponse;
import org.example.wallet.persistence.entities.WalletEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.ReportingPolicy;

/**
 * @author mazunin-sv
 * @version 26.06.2025 8:33
 */

@Mapper(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WalletMapper {

    @Mapping(target = "walletId", source = "request.walletId")
    WalletOperationResponse mapWalletOperationResponseFromWalletEntity(WalletEntity walletEntity, WalletOperationRequest request);

    WalletResponse mapWalletResponseFromWalletEntity(WalletEntity walletEntity);
}
