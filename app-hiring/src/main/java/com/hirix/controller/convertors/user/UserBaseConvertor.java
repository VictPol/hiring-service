package com.hirix.controller.convertors.user;

import com.hirix.controller.requests.create.UserCreateRequest;
import com.hirix.domain.User;
import com.hirix.exception.PoorInfoInRequestToCreateUpdateEntity;
import org.springframework.core.convert.converter.Converter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public abstract class UserBaseConvertor<S, T> implements Converter<S, T> {

    public User doConvert(UserCreateRequest request, User userForSave) {
        try {
            userForSave.setEmail(request.getEmail());
            userForSave.setPassword(request.getPassword());
            userForSave.setNickName(request.getNickName());
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in request body to create user" +
                    e.getCause());
        }
        if (userForSave.getEmail() == null ||
                userForSave.getPassword() == null ||
                userForSave.getNickName() == null) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in request body to create user");
        }

        userForSave.setChanged(Timestamp.valueOf(LocalDateTime.now()));
        return userForSave;
    }
}
