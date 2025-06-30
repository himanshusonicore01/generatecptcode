package com.calculator.cptcode.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "medical_test")
public class MedicalTest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;

    private String cpt_code;

    @Column(columnDefinition = "TEXT")
    private String related_words;

}
