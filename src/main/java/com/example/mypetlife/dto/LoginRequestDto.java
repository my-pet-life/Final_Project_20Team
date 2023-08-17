package com.example.mypetlife.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(force = true)
public class LoginRequestDto {

    @NotBlank(message = "이메일을 입력하세요")
    private final String email;

    @NotBlank(message = "패스워드를 입력하세요")
    private final String password;
}
