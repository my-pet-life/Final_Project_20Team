package com.example.mypetlife.dto.user;

import com.example.mypetlife.entity.user.PetSpecies;
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
    private final PetSpecies petSpecies;
    private final LocalDateTime createdAt;

}
