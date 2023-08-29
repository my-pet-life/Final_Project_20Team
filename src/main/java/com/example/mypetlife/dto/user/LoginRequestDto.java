package com.example.mypetlife.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class LoginRequestDto {

    @NotBlank(message = "이메일을 입력하세요")
    private String email;

    @NotBlank(message = "패스워드를 입력하세요")
    private String password;
}
