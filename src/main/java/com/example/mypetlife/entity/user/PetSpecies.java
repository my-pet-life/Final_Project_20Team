package com.example.mypetlife.entity.user;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PetSpecies {

    DOG("강아지"),
    CAT("고양이"),
    HAMSTER("햄스터"),
    HEDGEHOG("고슴도치"),
    BIRDS("조류"),
    REPTILE("파충류"),
    FISH("어류"),
    TURTLE("거북이"),
    ETC("기타");

    private final String description;
}
