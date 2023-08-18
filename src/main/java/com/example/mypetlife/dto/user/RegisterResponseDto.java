package com.example.mypetlife.dto.user;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class RegisterResponseDto {

    private final Long id;
    private final String username;
    private final String email;
    private final String petSpecies;
    private final LocalDateTime createdAt;

}
