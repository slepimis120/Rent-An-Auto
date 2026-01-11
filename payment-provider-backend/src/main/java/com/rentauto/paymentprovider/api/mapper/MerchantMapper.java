package com.rentauto.paymentprovider.api.mapper;

import com.rentauto.paymentprovider.api.dto.RegisterResponse;
import com.rentauto.paymentprovider.domain.Merchant;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MerchantMapper {

    MerchantMapper INSTANCE = Mappers.getMapper(MerchantMapper.class);

    RegisterResponse toRegisterResponse(Merchant merchant);
}