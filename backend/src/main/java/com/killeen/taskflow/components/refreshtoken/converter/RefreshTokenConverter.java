package com.killeen.taskflow.components.refreshtoken.converter;

import java.util.List;

import org.mapstruct.Mapper;

import com.killeen.taskflow.components.refreshtoken.model.RefreshToken;
import com.killeen.taskflow.db.model.generated.RefreshTokenDb;

@Mapper(componentModel = "spring")
public interface RefreshTokenConverter {
    RefreshToken toDto(RefreshTokenDb db);
    RefreshTokenDb toDb(RefreshToken dto);
    List<RefreshToken> toDtoList(List<RefreshTokenDb> dbList);
}
