package com.springBoot2.controller;

import com.springBoot2.config.EmployeeSearchDao;
import com.springBoot2.functonalInterface.EmployerFactory;
import com.springBoot2.model.Degree;
import com.springBoot2.model.Employer;
import com.springBoot2.repository.EmployerRepository;
import lombok.AllArgsConstructor;
import org.hibernate.search.exception.EmptyQueryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class EmployerController {
    @Autowired
    private EmployerRepository employerRepository;
    @Autowired
    private EmployeeSearchDao employeeSearchDao;


    @GetMapping(value = "/home")
    public String indax(ModelMap map) {
        map.addAttribute("employersWithTasks", employerRepository.findAll().stream().filter(employer -> !employer.getTasks().isEmpty())
                .limit(25).collect(Collectors.toList()));
        map.addAttribute("employers", employerRepository.findAll().stream().filter(employer -> employer.getTasks().isEmpty()).collect(Collectors.toList()));
        int sum = Stream.of(1, 2, 3, 4, 5)
                .reduce(10, (acc, x) -> acc + x);
        System.out.println(sum);
        return "index1";
    }

    @GetMapping(value = "/")
    public String home() {
        return "redirect:/home";
    }

    @RequestMapping(value = "/addEmployer")
    public String addEmployer(ModelMap map) {
        EmployerFactory<Employer> personFactory = Employer::new;
        map.addAttribute("employer", personFactory.create());
        map.addAttribute("allDegrees", Stream.of(Degree.values()).collect(Collectors.toList()));
        return "employer";
    }

    @PostMapping(value = "/employerForm")
    public String employerForm(@ModelAttribute(name = "employer") Employer employer) {
        Optional.of(employer).ifPresent(employerRepository::save);
        return "redirect:/home";
    }

    @RequestMapping(value = "updateEmployersData")
    public String updateEmployersData(@RequestParam(value = "employerId") int id) {
        Optional<Employer> byId = employerRepository.findById(id);
        return Optional.ofNullable("redirect:/addTask?employerId=" + byId.get().getId()).orElse("redirect:/error");


    }

    @RequestMapping(value = "updateEmployer")
    public String updateEmployer(@ModelAttribute("employer") Employer employer,
                                 @RequestParam(name = "employerId", required = false) int id) {
        Optional<Employer> one = employerRepository.findById(id);
        one.ifPresent(
                empl -> {
                    empl.setName(employer.getName());
                    empl.setSurname(employer.getSurname());
                    empl.setDegree(employer.getDegree());
                    employerRepository.save(empl);
                });
        return Optional.ofNullable("redirect:/addTask?employerId=" + one.get().getId()).orElse("redirect:/error");
    }

    @RequestMapping(value = "/deleteEmployer")
    public String deleteEmployer(@RequestParam("employerId") int id) {
        employerRepository.findById(id).ifPresent(employerRepository::delete);
        return "redirect:/home";
    }


    @RequestMapping(value = "/result", method = RequestMethod.GET)
    public String search(@RequestParam(value = "search", required = false) String search, ModelMap model) {
        List<Employer> searchResults = null;
        if (!StringUtils.isEmpty(search)) {
            try {
//                searchResults = employeeSearchDao.searchEmployerNameByKeywordQuery(search);
                searchResults = employeeSearchDao.searchEmployerNameByFuzzyQuery(search);
//                searchResults = employeeSearchDao.searchEmployerNameByWildcardQuery(search);
//                searchResults = employeeSearchDao.searchEmployerDescriptionByPhraseQuery(search);
//                searchResults = employeeSearchDao.searchEmployerNameAndDescriptionBySimpleQueryStringQuery(search);
//                searchResults = employeeSearchDao.searchEmployerNameAndDescriptionByKeywordQuery(search);


            } catch (EmptyQueryException ex) {
                // here you should handle unexpected errors
                // ...
                // throw ex;
            }
        }
        model.addAttribute("search", searchResults);
        return "search";


    }


}
