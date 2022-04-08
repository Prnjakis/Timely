package com.example.timely.repository;

import com.example.timely.model.Project;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface ProjectRepository extends CrudRepository<Project, Integer> {
    @Override
    List<Project> findAll();
}
