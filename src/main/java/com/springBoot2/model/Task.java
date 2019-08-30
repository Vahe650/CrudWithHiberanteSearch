package com.springBoot2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.search.annotations.*;


import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Indexed
@Table(name = "task")

public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;
    @Column
    @Field
    private String title;
    @Column
    @Field
    private String description;
    @Column(name = "assigned_time")
    private String assignedTime;
    @Column(name = "end_time")
    private String endTime;
    @Column(name = "task_status")
    @Enumerated(EnumType.STRING)
    private TaskStatus status;
    @ManyToOne
    @ContainedIn
    @IndexedEmbedded(depth = 2)
    private Employer employer;
}
