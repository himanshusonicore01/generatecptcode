package com.calculator.cptcode.repository;

import com.calculator.cptcode.entity.IllnessRelatedWords;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IllnessRelatedWordsRepository extends JpaRepository<IllnessRelatedWords, Integer> {

    @Query(value = "select * from illnessrelatedwords", nativeQuery = true)
    List<IllnessRelatedWords> findAllData();
}
