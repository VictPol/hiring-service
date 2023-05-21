package com.hirix.controller.convertors.user;

import com.hirix.controller.requests.patch.UserPatchRequest;
import com.hirix.domain.User;
import com.hirix.exception.EntityNotFoundException;
import com.hirix.exception.PoorInfoInRequestToCreateUpdateEntity;
import com.hirix.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserPatchConvertor implements Converter<UserPatchRequest, User> {
    private final UserRepository userRepository;

    @Override
    public User convert(UserPatchRequest request) {
        Long id;
        try {
            id = request.getId();
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about user id in request body to update user. Must be Long type. " +
                            e.getCause());
        }
        if (id < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor user id in request body to get user. " +
                    "Id must be more than 0L");
        }
        Optional<User> optionalUser;
        try {
            optionalUser = userRepository.findById(id);
        } catch (Exception e) {
            throw new EntityNotFoundException("Can not get user by id from DB, " + e.getCause());
        }
        User user = optionalUser.orElseThrow(() -> new NoSuchElementException("No user with such id"));
        try {
            if (request.getEmail() != null) {
                user.setEmail(request.getEmail());
            }
            if (request.getPassword() != null) {
                user.setPassword(request.getPassword());
            }
            if (request.getNickName() != null) {
                user.setNickName(request.getNickName());
            }
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in request body to patch update user. " +
                    e.getCause());
        }
        user.setChanged(Timestamp.valueOf(LocalDateTime.now()));
        return user;
    }
}

