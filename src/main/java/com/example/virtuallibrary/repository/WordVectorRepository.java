package com.example.virtuallibrary.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.virtuallibrary.models.WordVector;

public interface WordVectorRepository extends CrudRepository<WordVector, Long> {
    List<WordVector> findAll();

}
