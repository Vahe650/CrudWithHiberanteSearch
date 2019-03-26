package com.springBoot2.controller;


import com.springBoot2.model.Degree;
import com.springBoot2.model.Employer;
import com.springBoot2.model.Task;
import com.springBoot2.model.TaskStatus;
import com.springBoot2.repository.EmployerRepository;
import com.springBoot2.repository.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class TaskController {

    private TaskRepository taskRepository;
    private EmployerRepository employerRepository;

    @RequestMapping(value = "addTask")
    public String addTask(ModelMap map, @RequestParam(name = "employerId", required = false) int id) {
        Optional<Employer> byId = employerRepository.findById(id);
        byId.ifPresent(employer -> map.addAttribute("employer", employer));
        map.addAttribute("allStatus", Arrays.asList(TaskStatus.values()));
        map.addAttribute("allDegrees", Arrays.asList(Degree.values()));
        map.addAttribute("task", new Task());
        return "task";
    }

    @RequestMapping(value = "taskForm")
    public String tasskForm(@ModelAttribute("task") Task task, @RequestParam(value = "employerId", required = false) int id) {
        Optional<Employer> byId = employerRepository.findById(id);
        byId.ifPresent(task::setEmployer);
        taskRepository.save(task);
        return "redirect:/addTask?employerId=" + task.getEmployer().getId();
    }

    @RequestMapping(value = "/taskDetails")
    public String updateTaskData(ModelMap map, @RequestParam(value = "taskId", required = false) int id) {
        boolean isFinished = false;
        Optional<Task> one = taskRepository.findById(id);
        one.ifPresent(task-> map.addAttribute("task", task));
        if (one.isPresent()) {
            boolean status = Optional.ofNullable(one.get().getStatus()).isPresent();
            Optional<TaskStatus> newStat = Optional.of(TaskStatus.NEW);
            Optional<TaskStatus> finishedStat = Optional.of(TaskStatus.FINISHED);
            if (status && one.get().getStatus().equals(finishedStat.get())) {
                isFinished = true;
            }
            map.addAttribute("isFinished", isFinished);
            map.addAttribute("allStatus", Arrays.asList(TaskStatus.values()));

            if (status && one.get().getStatus().equals(newStat.get()))
                one.get().setStatus(TaskStatus.INPROGRESS);
            one.ifPresent(taskRepository::save);
        }
        return "updateTask";
    }

    @RequestMapping(value = "/updateTask")
    public String updateTask(@ModelAttribute("task") Task task, @RequestParam(name = "taskId", required = false) int taskId) {
        Optional<Task> byId = taskRepository.findById(taskId);
        byId.ifPresent(taskEmploye -> task.setEmployer(taskEmploye.getEmployer()));
        task.setId(taskId);
        taskRepository.save(task);
        return "redirect:/taskDetails?taskId=" + taskId;
    }

    @RequestMapping(value = "/finishedTask")
    public String finishedTask(@ModelAttribute("task") Task task,
                               @RequestParam(name = "taskId", required = false) int taskId) {
        Optional<Task> one = taskRepository.findById(taskId);
        one.ifPresent(stat -> stat.setStatus(TaskStatus.FINISHED));
        one.ifPresent(taskRepository::save);
        return "redirect:/taskDetails?taskId=" + taskId;
    }

    @RequestMapping(value = "/deleteTask")
    public String deleteTask(@RequestParam(name = "taskId", required = false) int taskId) {
        Optional<Task> one = taskRepository.findById(taskId);
        one.ifPresent(taskRepository::delete);
        return "redirect:/addTask?employerId=" + one.get().getEmployer().getId();

    }
}
