package com.example.mypetlife.entity.hospital;

import com.example.mypetlife.entity.Review;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "hospitals")
public class Hospital {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hospital_id")
    private Long id;

    @Embedded
    private Address address;

    @Column(name = "hospital_tel")
    private String hospitalTel;

    @Column(name = "hospital_operation")
    private String hospitalOperation;

    @OneToMany(mappedBy = "hospitalId")
    private List<Review> reviews = new ArrayList<>();
}
