package com.springBoot2.repository;


import com.springBoot2.model.Employer;
import com.springBoot2.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task,Integer> {
    List<Task> findAllByEmployer(Employer employer);



}
