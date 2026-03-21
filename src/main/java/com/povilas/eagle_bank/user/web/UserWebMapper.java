package com.povilas.eagle_bank.user.web;

import com.povilas.eagle_bank.user.domain.Address;
import com.povilas.eagle_bank.user.domain.User;
import org.springframework.stereotype.Component;

@Component
public class UserWebMapper {

    public User toDomain(CreateUserRequest request) {
        Address address = new Address(
                request.address().line1(),
                request.address().line2(),
                request.address().line3(),
                request.address().town(),
                request.address().county(),
                request.address().postcode()
        );
        return new User(null, request.name(), address, request.phoneNumber(), request.email(), null, null);
    }

    public UserResponse toResponse(User user) {
        AddressResponse address = new AddressResponse(
                user.address().line1(),
                user.address().line2(),
                user.address().line3(),
                user.address().town(),
                user.address().county(),
                user.address().postcode()
        );
        return new UserResponse(
                user.id(),
                user.name(),
                address,
                user.phoneNumber(),
                user.email(),
                user.createdTimestamp(),
                user.updatedTimestamp()
        );
    }
}
