package ru.samokhin.labCheck.domain.test;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.samokhin.labCheck.domain.task.Task;

@Entity
@Table(name = "TEST")
@NoArgsConstructor
@Getter
@Setter
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String script;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    public Test(String name, String script, Task task) {
        this.name = name;
        this.script = script;
        this.task = task;
    }
}
