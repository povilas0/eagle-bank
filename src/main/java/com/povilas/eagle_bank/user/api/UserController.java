package com.povilas.eagle_bank.user.api;

import com.povilas.eagle_bank.user.domain.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    private final UserService userService;
    private final UserApiMapper mapper;

    public UserController(UserService userService, UserApiMapper mapper) {
        this.userService = userService;
        this.mapper = mapper;
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable String userId) {
        userService.deleteUser(mapper.toDeleteCommand(userId));
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
        return mapper.toResponse(userService.createUser(mapper.toCreateCommand(request)));
    }
}
