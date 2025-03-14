package hello.architecture.user.controller;

import hello.architecture.user.controller.response.UserResponse;
import hello.architecture.user.domain.User;
import hello.architecture.user.service.UserService;
import hello.architecture.user.service.dto.Login;
import hello.architecture.user.service.dto.UserCreate;
import hello.architecture.user.service.dto.UserUpdate;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@Tag(name = "유저(users)")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/users")
    public ResponseEntity<UserResponse> create(@RequestBody UserCreate userCreate) {
        User response = userService.create(userCreate);
        return ResponseEntity
                .status(CREATED)
                .body(UserResponse.of(response));
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody Login login) {
        User response = userService.login(login);
        return ResponseEntity
                .ok()
                .body(UserResponse.of(response));
    }


    @PutMapping("/me")
    @Parameter(in = ParameterIn.HEADER, name = "EMAIL")
    public ResponseEntity<UserResponse> updateMyInfo(
            @Parameter(name = "EMAIL", in = ParameterIn.HEADER)
            @RequestHeader("EMAIL") String email,
            @RequestBody UserUpdate update
    ) {
        User response = userService.getByEmail(email);
        userService.update(response.getId(), update);
        User afterUpdate = userService.getByEmail(email);
        return ResponseEntity
                .ok()
                .body(UserResponse.of(afterUpdate));
    }

}
