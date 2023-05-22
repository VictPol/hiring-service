package com.hirix.controller.convertors.user;

import com.hirix.controller.requests.update.UserUpdateRequest;
import com.hirix.domain.User;
import com.hirix.exception.EntityNotFoundException;
import com.hirix.exception.PoorInfoInRequestToCreateUpdateEntity;
import com.hirix.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserUpdateConvertor extends UserBaseConvertor<UserUpdateRequest, User> {

    private final UserRepository userRepository;

    @Override
    public User convert(UserUpdateRequest request) {
        Long id;
        try {
            id = request.getId();
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about employee id in request body to update user. Must be Long type. " + e.getCause());
        }
        if (id < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in request body to update user. " +
                    "Id must be more than 0L");
        }
        Optional<User> optionalUser;
        try {
            optionalUser = userRepository.findById(id);
        } catch (Exception e) {
            throw new EntityNotFoundException("Can not get user by id from DB, " + e.getCause());
        }
        User user = optionalUser.orElseThrow(() -> new NoSuchElementException("No user with such id"));
        return doConvert(request, user);
    }
}
