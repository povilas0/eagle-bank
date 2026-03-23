package com.povilas.eagle_bank.user.api;

import com.povilas.eagle_bank.user.domain.Address;
import com.povilas.eagle_bank.user.domain.CreateUserCommand;
import com.povilas.eagle_bank.user.domain.DeleteUserCommand;
import com.povilas.eagle_bank.user.domain.UpdateUserCommand;
import com.povilas.eagle_bank.user.domain.User;
import org.springframework.stereotype.Component;

@Component
public class UserApiMapper {

    public CreateUserCommand toCreateCommand(CreateUserRequest request) {
        Address address = new Address(
                request.address().line1(),
                request.address().line2(),
                request.address().line3(),
                request.address().town(),
                request.address().county(),
                request.address().postcode()
        );
        return new CreateUserCommand(request.name(), address, request.phoneNumber(), request.email(), request.password());
    }

    public DeleteUserCommand toDeleteCommand(String userId) {
        return new DeleteUserCommand(userId);
    }

    public UpdateUserCommand toUpdateCommand(UpdateUserRequest request) {
        Address address = request.address() == null ? null : new Address(
                request.address().line1(),
                request.address().line2(),
                request.address().line3(),
                request.address().town(),
                request.address().county(),
                request.address().postcode()
        );
        return new UpdateUserCommand(request.name(), address, request.phoneNumber(), request.email());
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
