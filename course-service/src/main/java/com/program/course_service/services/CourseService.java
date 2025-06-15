package com.program.course_service.services;

import com.program.course_service.entities.*;
import com.program.course_service.repositories.CourseRepository;
import com.program.course_service.feign_config.UserServiceClient;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CourseService {

    @NonNull
    private UserServiceClient userServiceClient;

    @NonNull
    private CourseRepository courseRepository;

    public CourseResponse getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course with id: " +
                        id + " not found"));
        return createCourseResponse(course);
    }

    public CourseResponse getCourseByTitle(String title) {
        log.info("Searching by title={}", title);
        Course course = courseRepository.findByTitle(title)
                .orElseThrow(() -> new EntityNotFoundException("Course with title: " +
                        title + " not found"));
        log.info("Course found");
        return createCourseResponse(course);
    }

    public List<CourseResponse> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        List<CourseResponse> responses = new ArrayList<>();
        courses.forEach(course -> responses.add(createCourseResponse(course)));
        if (courses.isEmpty()) throw new EntityNotFoundException("Not yet courses");
        return responses;
    }

    @Transactional
    public CourseResponse createCourse(CreateCourseRequest request) {
        log.info("Creating course {}", request);
        if (courseRepository.existsByTitle(request.getTitle()))
            throw new DataIntegrityViolationException("The course with title: " +
                    request.getTitle() + " already exist");
        log.info("Course with title not exists {}", request);
        Course course = new Course();
        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setCategory(request.getCategory());
        course.setPrice(request.getPrice());
        log.info("The main fields are set {}", request);
        course.setTeacherId(validateTeacherAccessibility(request.getTeacherId()));
        courseRepository.save(course);
        log.info("The course has been created {}", request);
        return createCourseResponse(course);
    }

    private Course getCourseByIdFromDB(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course with id: " +
                        id + " not found"));
    }

    private CourseResponse createCourseResponse(Course course) {
        return new CourseResponse(course.getTitle(),
                course.getDescription(),
                course.getCategory(),
                course.getPrice(),
                course.getTeacherId());
    }

    @Transactional
    public CourseResponse updateCourse(Long id, UpdateCourseRequest request) {
        Course course = getCourseByIdFromDB(id);
        if (request.getTitle() != null) {
            if (courseRepository.existsByTitle(request.getTitle()))
                throw new DataIntegrityViolationException("The course with title: " +
                    request.getTitle() + " already exist");
            course.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) course.setDescription(request.getDescription());
        if (request.getCategory() != null) course.setCategory(request.getCategory());
        if (request.getPrice() != null) course.setPrice(request.getPrice());
        if (request.getTeacherId() != null) {
            course.setTeacherId(validateTeacherAccessibility(request.getTeacherId()));
        }
        courseRepository.save(course);
        return createCourseResponse(course);
    }

    private Long validateTeacherAccessibility(Long teacherId) {
        log.info("Getting user");
        Set<String> roles = userServiceClient.getUser(teacherId)
                .orElseThrow(() -> new EntityNotFoundException("User with id: " + teacherId + " not found"))
                .getRoles();
        log.info("The user has been found");
        if (!roles.contains("TEACHER") && !roles.contains("ADMIN")) {
            throw new IllegalArgumentException("it is not possible to add a course," +
                    " the user must have the TEACHER or ADMIN role.");
        }
        log.info("The user is valid");
        return teacherId;
    }

    @Transactional
    public void deleteCourseById(Long id) {
        if (!courseRepository.existsById(id))
            throw new EntityNotFoundException("Course with id: " +
                    id + " not found");
        courseRepository.deleteById(id);
    }

    @Transactional
    public void deleteCourseByTitle(String title) {
        if (!courseRepository.existsByTitle(title))
            throw new EntityNotFoundException("Course with title: " +
                    title + " not found");
        courseRepository.deleteByTitle(title);
    }

    @Transactional
    public List<CourseResponse> getCoursesByTeacher(Long teacherId) {
        List<CourseResponse> courses = courseRepository.findByTeacherId(teacherId).stream()
                .map(this::createCourseResponse)
                .toList();
        if (courses.isEmpty()) throw new EntityNotFoundException("This author doesn't have any courses yet");
        return courses;
    }
}
