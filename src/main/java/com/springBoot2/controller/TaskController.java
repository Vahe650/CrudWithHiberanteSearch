package com.springBoot2.controller;


import com.springBoot2.model.Degree;
import lombok.AllArgsConstructor;
import com.springBoot2.model.Task;
import com.springBoot2.model.TaskStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.springBoot2.repository.EmployerRepository;
import com.springBoot2.repository.TaskRepository;

import java.util.Arrays;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class TaskController {

    private   TaskRepository taskRepository;
    private   EmployerRepository employerRepository;



    @RequestMapping(value = "addTask")
    public String addTask(ModelMap map, @RequestParam(name = "employerId",required = false) int id) {
        map.addAttribute("employer", employerRepository.findById(id).get());
        map.addAttribute("allStatus",Arrays.asList(TaskStatus.values()));
        map.addAttribute("allDegrees",Arrays.asList(Degree.values()));
        map.addAttribute("task", new Task());
        return "task";
    }

    @RequestMapping(value = "taskForm")
    public String tasskForm(@ModelAttribute("task") Task task,@RequestParam(value = "employerId" ,required = false) int id) {
        task.setEmployer(employerRepository.getOne(id));
        taskRepository.save(task);
        return "redirect:/addTask?employerId=" + task.getEmployer().getId();

    }

    @RequestMapping(value = "/taskDetails")
    public String updateTaskData(ModelMap map, @RequestParam("taskId") int id) {
        Optional<Task> one = taskRepository.findById(id);
        map.addAttribute("task", one.get());
        map.addAttribute("allStatus",Arrays.asList(TaskStatus.values()));
        if (one.get().getStatus().equals(TaskStatus.NEW))
            one.get().setStatus(TaskStatus.INPROGRESS);
        taskRepository.save(one.get());
        return "updateTask";
    }

    @RequestMapping(value = "/updateTask")
    public String updateTask(@ModelAttribute("task") Task task,@RequestParam(name = "taskId",required = false) int taskId) {
        Optional<Task> one = taskRepository.findById(taskId);
        one.get().setTitle(task.getTitle());
        one.get().setDescription(task.getDescription());
        one.get().setEndTime(task.getEndTime());
        one.get().setStatus(task.getStatus());
        taskRepository.save(one.get());
        return "redirect:/taskDetails?taskId=" + one.get().getId();
    }

    @RequestMapping(value = "/finishedTask")
    public String finishedTask(@ModelAttribute("task") Task task,
                               @RequestParam(name = "taskId",required = false) int taskId) {
        Optional<Task> one = taskRepository.findById(taskId);
        if (one.isPresent()) {
            one.get().setStatus(TaskStatus.FINISHED);
            taskRepository.save(one.get());
        }
            return "redirect:/taskDetails?taskId=" + one.get().getId();

    }


}
