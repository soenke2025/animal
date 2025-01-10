package com.example.springboot.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "animals")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Animal {
    @Id
    private Long id;

    private String name;

    private String description;

}