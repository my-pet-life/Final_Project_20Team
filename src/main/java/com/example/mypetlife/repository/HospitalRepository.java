package com.example.mypetlife.repository;

import com.example.mypetlife.entity.hospital.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HospitalRepository extends JpaRepository<Hospital, Long> {
}
