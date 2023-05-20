package com.hirix.controller.convertors.user;

import com.hirix.controller.requests.create.UserCreateRequest;
import com.hirix.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class UserCreateConvertor extends UserBaseConvertor<UserCreateRequest, User> {

    @Override
    public User convert(UserCreateRequest request) {
        User user = new User();
        user.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        return doConvert(request, user);
    }
}
