package com.povilas.eagle_bank.user.web;

import com.povilas.eagle_bank.user.domain.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    private final UserService userService;
    private final UserWebMapper mapper;

    public UserController(UserService userService, UserWebMapper mapper) {
        this.userService = userService;
        this.mapper = mapper;
    }

    @GetMapping("/{userId}")
    public UserResponse getUser(@PathVariable String userId) {
        return mapper.toResponse(userService.getUser(userId));
    }

    @PatchMapping("/{userId}")
    public UserResponse updateUser(@PathVariable String userId,
                                   @Valid @RequestBody UpdateUserRequest request) {
        return mapper.toResponse(userService.updateUser(userId, mapper.toUpdateCommand(request)));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createUser(@Valid @RequestBody CreateUserRequest request) {
        var domainUser = mapper.toDomain(request);
        var created = userService.createUser(
                domainUser.name(),
                domainUser.address(),
                domainUser.phoneNumber(),
                domainUser.email()
        );
        return mapper.toResponse(created);
    }
}
