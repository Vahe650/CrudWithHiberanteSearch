package com.springBoot2.controller;


import com.springBoot2.model.Degree;
import com.springBoot2.model.Employer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import com.springBoot2.repository.EmployerRepository;
import com.springBoot2.repository.TaskRepository;

import java.util.Arrays;
import java.util.Optional;

@Controller
public class EmployerController {

    private final TaskRepository taskRepository;

    private final EmployerRepository employerRepository;

    @Autowired
    public EmployerController(TaskRepository taskRepository, EmployerRepository employerRepository) {
        this.taskRepository = taskRepository;
        this.employerRepository = employerRepository;
    }


    @GetMapping(value = "/")
    public String indax(ModelMap map) {
        map.addAttribute("employers", employerRepository.findAll());
        map.addAttribute("tasks", taskRepository.findAll());
        return "index";
    }

    @RequestMapping(value = "/addEmployer")
    public String addEmployer(ModelMap map) {
        map.addAttribute("employer", new Employer());
        map.addAttribute("allDegrees", Arrays.asList(Degree.values()));
        return "employer";
    }

    @PostMapping(value = "/employerForm")
    public String employerForm(@ModelAttribute(name = "employer") Employer employer) {
        employerRepository.save(employer);
        return "redirect:/";

    }

    @RequestMapping(value = "updateEmployersData")
    public String updateEmployersData(@RequestParam(value = "employerId") int id) {
        Optional<Employer> one=employerRepository.findById(id);
        return "redirect:/addTask?employerId=" + employerRepository.findById(id).get().getId();

    }

    @RequestMapping(value = "updateEmployer")
    public String updateEmployer(@ModelAttribute("employer") Employer employer,@RequestParam(name = "employerId",required = false) int id) {
        Optional<Employer> one = employerRepository.findById(id);
        if (one.isPresent()) {
            one.get().setName(employer.getName());
            one.get().setSurname(employer.getSurname());
            one.get().setDegree(employer.getDegree());
            employerRepository.save(one.get());
        }
        return "redirect:/addTask?employerId=" + one.get().getId();
    }

    @RequestMapping(value = "/deleteEmployer")
    public String deleteEmployer(@RequestParam("employerId") int id) {
        final Employer employer = employerRepository.findById(id).get();
        employerRepository.delete(employer);
        return "redirect:/";
    }

}
