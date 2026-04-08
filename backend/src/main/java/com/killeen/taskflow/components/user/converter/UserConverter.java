package com.killeen.taskflow.components.user.converter;

import org.mapstruct.Mapper;

import com.killeen.taskflow.components.user.model.User;
import com.killeen.taskflow.db.model.generated.UserDb;


@Mapper(componentModel = "spring")
public interface UserConverter {
    User toDto(UserDb db);
    UserDb toDb(User dto);
}
