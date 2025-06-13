package com.program.course_service.entities;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCourseRequest {

    @Size(min = 10, max = 100, message = "The course title should be in the range of 10 to 100 characters")
    private String title;

    @Size(min = 10, max = 1000, message = "The course description should be in the range of 10 to 1000 characters")
    private String description;

    @Size(min = 2, max = 20, message = "The course category should be in the range of 2 to 20 characters")
    private String category;

    @Min(value = 1, message = "Price must be greater than or equal to 1")
    @Max(value = 100000, message = "Price must be less than or equal to 100000")
    private Double price;

    private Long teacherId;
}
