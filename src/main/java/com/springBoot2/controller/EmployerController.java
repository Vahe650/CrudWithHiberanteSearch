package com.springBoot2.controller;

import com.springBoot2.model.Degree;
import com.springBoot2.model.Employer;
import com.springBoot2.repository.EmployerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@AllArgsConstructor
public class EmployerController {


    private EmployerRepository employerRepository;


    @GetMapping(value = "/")
    public String indax(ModelMap map) {
        Stream<Employer> limit = employerRepository.findAll().stream()
                .filter(employer -> !employer.getTasks().isEmpty())
                .limit(25);
        map.addAttribute("employersWithTasks", limit.collect(Collectors.toList()));
        Stream<Employer> employerStream = employerRepository.findAll().stream()
                .filter(employer -> employer.getTasks().isEmpty());
        map.addAttribute("employers", employerStream.collect(Collectors.toList()));
        int sum = Stream.of(1, 2, 3, 4, 5)
                .reduce(10, (acc, x) -> acc + x);
        System.out.println(sum);
        return "index";
    }

    @RequestMapping(value = "/addEmployer")
    public String addEmployer(ModelMap map) {
        map.addAttribute("employer", new Employer());
        map.addAttribute("allDegrees", Stream.of(Degree.values()).collect(Collectors.toList()));
        return "employer";
    }

    @PostMapping(value = "/employerForm")
    public String employerForm(@ModelAttribute(name = "employer") Employer employer) {
        Optional.of(employer).ifPresent(employerRepository::save);
        return "redirect:/";
    }

    @RequestMapping(value = "updateEmployersData")
    public String updateEmployersData(@RequestParam(value = "employerId") int id) {
        return "redirect:/addTask?employerId=" + id;

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
        employerRepository.findById(id).ifPresent(employerRepository::delete);
        return "redirect:/";
    }
}
