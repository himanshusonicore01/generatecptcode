package com.calculator.cptcode.repository;

import com.calculator.cptcode.entity.NhsIllnessesCategorized;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NhsIllnessesCategorizedRepository extends JpaRepository<NhsIllnessesCategorized, Integer> {

    @Query(value = "select * from nhsillnessescategorized where join_related_word In (:illnessMatchKey)", nativeQuery = true)
    List<NhsIllnessesCategorized> findByJoinRelatedWord(List<String> illnessMatchKey);

}
