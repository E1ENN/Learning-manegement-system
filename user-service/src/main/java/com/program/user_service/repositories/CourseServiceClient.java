package com.program.user_service.repositories;

import com.program.user_service.entities.support_entities.response_enitites.CourseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "course-service")
public interface CourseServiceClient {
    @GetMapping("/courses/teacher/{teacherId}")
    List<CourseResponse> findCoursesByTeacher(@PathVariable("teacherId") Long teacherId);
}
