package com.program.course_service.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "courses", schema = "course_service")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Long id;

    @Column
    private String title;

    @Column
    private String description;

    @Column
    private String category;

    @Column
    private Double price;

    @Column(name = "teacher_id")
    private Long teacherId;
}
