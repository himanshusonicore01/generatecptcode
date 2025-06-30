package com.calculator.cptcode.repository;

import com.calculator.cptcode.entity.MedicalTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MedicalTestRepository extends JpaRepository<MedicalTest, Integer> {

    @Query(value = "Select * from medical_test", nativeQuery = true)
    List<MedicalTest> getAllMedicalTest();
}
