package com.example.springboot.service;

import org.springframework.stereotype.Service;

@Service
public class AnimalIdGenerationService {
    public Long newAnimalId() {
        return System.nanoTime();
    }
}