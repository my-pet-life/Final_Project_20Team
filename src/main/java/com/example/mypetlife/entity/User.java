package com.example.mypetlife.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "users")
public class User {

    @Id @GeneratedValue
    private Long id;
    private String username;
    private String email;
    private String password;
    private String phone;
    private String birthDate;
    private String petSpices;
    private LocalDate created_at;
}
