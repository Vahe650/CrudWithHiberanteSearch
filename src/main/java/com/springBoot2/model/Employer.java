package com.springBoot2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;

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
    @Fields({
            @Field,
            @Field(name = "sortName", analyze = Analyze.NO, store = Store.NO, index = Index.NO)
    })
    @SortableField(forField = "sortName")
    private String name;
    @Field
    @Column
    private String surname;
    @Column
    @Field
    @Enumerated(EnumType.STRING)
    private Degree degree;
    @OneToMany(mappedBy = "employer", cascade = CascadeType.ALL)
    @ContainedIn
    @IndexedEmbedded(depth = 2)
    private List<Task> tasks;
}
