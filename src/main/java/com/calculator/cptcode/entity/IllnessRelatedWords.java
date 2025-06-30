package com.calculator.cptcode.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "illnessrelatedwords")
public class IllnessRelatedWords {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT in DB
    private int Id;

    private String illness;

    @Column(columnDefinition = "TEXT")
    private String related_words; // changed from List<String> to String

    private String join_category_status; // changed from int to String
}
