package com.program.course_service.repositories;


import com.program.course_service.entities.Course;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    boolean existsByTitle(String title);
    boolean existsById(@NonNull Long id);
    void deleteByTitle(String title);
    Optional<Course> findByTitle(String title);
    List<Course> findByTeacherId(Long teacherId);
}
