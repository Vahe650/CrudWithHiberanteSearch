package com.springBoot2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "task")

public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;
    @Column
    private String title;
    @Column
    private String description;
    @Column(name = "assigned_time")
    private String assignedTime;
    @Column(name = "end_time")
    private String endTime;
    @Column(name = "task_status")
    @Enumerated(EnumType.STRING)
    private TaskStatus status;
    @ManyToOne
    private Employer employer;
}
