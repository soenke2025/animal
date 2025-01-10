package com.example.springboot.controller;

import org.springframework.web.bind.annotation.RestController;
import com.example.springboot.entity.Animal;
import com.example.springboot.service.AnimalQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;


import java.util.List;
import java.util.Optional;

@RestController
public class AnimalQueryController {

	

	private final AnimalQueryService animalQueryService;

    public AnimalQueryController(AnimalQueryService animalQueryService) {
        this.animalQueryService = animalQueryService;
    }


    @GetMapping("/")
	public String index() {
		return "Greetings from Spring Boot!";
	}

}
