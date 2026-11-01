package com.retrobazar.user.infrastructure.adapter.in.web;

import com.retrobazar.user.application.command.LoginUserCommand;
import com.retrobazar.user.application.command.RegisterUserCommand;
import com.retrobazar.user.application.port.in.LoginUserUseCase;
import com.retrobazar.user.application.port.in.RegisterUserUseCase;
import com.retrobazar.user.domain.User;
import com.retrobazar.user.infrastructure.adapter.in.web.dto.LoginRequestDto;
import com.retrobazar.user.infrastructure.adapter.in.web.dto.LoginResponseDto;
import com.retrobazar.user.infrastructure.adapter.in.web.dto.RegisterUserRequestDto;
import com.retrobazar.user.infrastructure.adapter.in.web.dto.UserResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final RegisterUserUseCase registerUserUseCase;
    private final LoginUserUseCase loginUserUseCase;

    public AuthController(
            RegisterUserUseCase registerUserUseCase,
            LoginUserUseCase loginUserUseCase
    ) {
        this.registerUserUseCase = registerUserUseCase;
        this.loginUserUseCase = loginUserUseCase;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> registerUser(
            @Valid @RequestBody RegisterUserRequestDto request
    ) {
        RegisterUserCommand command = request.toCommand();
        User user = registerUserUseCase.registerUser(command);
        UserResponseDto response = UserResponseDto.fromUser(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
            @Valid @RequestBody LoginRequestDto request
    ) {
        LoginUserCommand command = request.toCommand();
        String accessToken = loginUserUseCase.login(command);

        return ResponseEntity.ok(LoginResponseDto.bearer(accessToken));
    }
}
