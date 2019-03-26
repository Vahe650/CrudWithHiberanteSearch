package com.springBoot2.controller;

import com.springBoot2.model.Degree;
import com.springBoot2.model.Employer;
import com.springBoot2.repository.EmployerRepository;
import com.springBoot2.repository.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class EmployerController {

    private final TaskRepository taskRepository;
    private final EmployerRepository employerRepository;



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
        return "redirect:/addTask?employerId="+ id;

    }

    @RequestMapping(value = "updateEmployer")
    public String updateEmployer(@ModelAttribute("employer") Employer employer, @RequestParam(name = "employerId", required = false) int id) {
        Optional<Employer> one = employerRepository.findById(id);
        one.ifPresent(
                empl -> {
                    empl.setName(employer.getName());
                    empl.setSurname(employer.getSurname());
                    empl.setDegree(employer.getDegree());
                    employerRepository.save(empl);
                });
        return "redirect:/addTask?employerId=" + id;
    }

    @RequestMapping(value = "/deleteEmployer")
    public String deleteEmployer(@RequestParam("employerId") int id) {
        Optional<Employer> employer = employerRepository.findById(id);
        employer.ifPresent(employerRepository::delete);
        return "redirect:/";
    }
}
