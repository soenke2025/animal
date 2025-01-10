package com.example.springboot.service;

import com.example.springboot.entity.Animal;
import com.example.springboot.repository.AnimalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AnimalQueryService {

    @Autowired
    private AnimalRepository animalRepository;

}