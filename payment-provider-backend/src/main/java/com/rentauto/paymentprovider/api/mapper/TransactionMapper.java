package com.rentauto.paymentprovider.api.mapper;

import com.rentauto.paymentprovider.api.dto.PaymentInitRequest;
import com.rentauto.paymentprovider.api.dto.PaymentInitResponse;
import com.rentauto.paymentprovider.api.dto.PaymentStatusResponse;
import com.rentauto.paymentprovider.domain.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "merchant", ignore = true)
    @Mapping(target = "paymentStatus", constant = "PENDING")
    @Mapping(target = "pspTimestamp", expression = "java(java.time.Instant.now())")
    @Mapping(target = "stan", ignore = true)
    @Mapping(target = "externalTransactionId", ignore = true)
    @Mapping(target = "paymentUrl", ignore = true)
    Transaction toEntity(PaymentInitRequest request);

    @Mapping(target = "stan", source = "transaction.stan")
    @Mapping(target = "paymentUrl", source = "paymentUrl")
    @Mapping(target = "status", source = "transaction.paymentStatus")
    PaymentInitResponse toInitResponse(Transaction transaction, String paymentUrl);

    @Mapping(target = "stan", source = "transaction.stan")
    @Mapping(target = "status", source = "transaction.paymentStatus")
    PaymentStatusResponse toStatusResponse(Transaction transaction);
}
