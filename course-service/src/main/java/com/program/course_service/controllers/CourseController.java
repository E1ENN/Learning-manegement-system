package com.program.course_service.controllers;

import com.program.course_service.entities.CourseResponse;
import com.program.course_service.entities.CreateCourseRequest;
import com.program.course_service.entities.UpdateCourseRequest;
import com.program.course_service.services.CourseService;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CourseController {

    @NonNull
    private CourseService courseService;

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponse> getCourseById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    @GetMapping
    public ResponseEntity<CourseResponse> getCourseByName(@RequestParam("title") String title) {
        log.info("Request to get course by title={} starting ", title);
        return ResponseEntity.ok(courseService.getCourseByTitle(title));
    }

    @GetMapping("/all")
    public ResponseEntity<List<CourseResponse>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<CourseResponse>> getCoursesByTeacherId(@PathVariable("teacherId")
                                                                      Long teacherId) {
        return ResponseEntity.ok(courseService.getCoursesByTeacher(teacherId));
    }

    @PostMapping("/create")
    public ResponseEntity<CourseResponse> createCourse(@Valid @RequestBody CreateCourseRequest request) {
        log.info("Request to create course starting {}", request);
        return new ResponseEntity<>(courseService.createCourse(request), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CourseResponse> updateCourse(@PathVariable("id") Long id,
                                                       @RequestBody UpdateCourseRequest request) {
        return ResponseEntity.ok(courseService.updateCourse(id, request));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCourseById(@PathVariable("id") Long id) {
        courseService.deleteCourseById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteCourseByTitle(@RequestParam("title") String title) {
        courseService.deleteCourseByTitle(title);
        return ResponseEntity.noContent().build();
    }
}
