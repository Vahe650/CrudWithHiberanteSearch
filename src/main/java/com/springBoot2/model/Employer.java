package com.springBoot2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.TermVector;

import javax.persistence.*;
import java.util.List;
import java.util.function.Consumer;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Indexed
@Table(name = "employer")
public class Employer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;
    @Column
    @Field(termVector = TermVector.YES)
    private String name;
    @Field(termVector = TermVector.YES)
    @Column
    private String surname;
    @Column
    @Field(termVector = TermVector.YES)
    @Enumerated(EnumType.STRING)
    private Degree degree;
    @OneToMany(mappedBy = "employer", cascade = CascadeType.ALL)
    private List<Task> tasks;
}
