package com.retrobazar.user.application.port.out;

public interface PasswordEncoderPort {

    String encode(String rawPassword);
}
