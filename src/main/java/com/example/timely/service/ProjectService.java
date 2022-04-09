package com.example.timely.service;

import com.example.timely.model.Project;
import com.example.timely.model.paging.Page;
import com.example.timely.model.paging.Paged;
import com.example.timely.model.paging.Paging;
import com.example.timely.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {
    @Autowired
    ProjectRepository projectRepository;

    public Paged<Project> getProjects(int pageNumber, int size) {

        List<Project> projects = projectRepository.findAll();
        Collections.reverse(projects);
        int totalPages = ( (projects.size() - 1 ) / size ) +1 ;
        int skip = pageNumber > 1 ? (pageNumber - 1) * size : 0;
        List<Project> paged = projects.stream()
                                        .skip(skip)
                                        .limit(size)
                                        .collect(Collectors.toList());

        return new Paged<>(new Page<>(paged, totalPages), Paging.of(totalPages, pageNumber, size));
    }


}
