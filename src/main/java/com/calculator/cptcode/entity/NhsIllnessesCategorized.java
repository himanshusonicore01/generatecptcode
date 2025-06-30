package com.calculator.cptcode.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "nhsillnessescategorized")
public class NhsIllnessesCategorized {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int Id;
    private String illness;
    private String category;
    private String status;
    private int join_related_word;
}
