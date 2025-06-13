package com.program.course_service.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.program.course_service.CourseServiceApplication;
import com.program.course_service.entities.CourseResponse;
import com.program.course_service.entities.CreateCourseRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = CourseServiceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "spring.cloud.discovery.enable=false",
        "spring.cloud.config.enable=false"
})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CourseServiceIntegrationTest {

    @NonNull
    private MockMvc mockMvc;

    @NonNull
    private ObjectMapper objectMapper;

    @Test
    void createCourseWithExistingTeacherShouldReturnCreatedCourse() throws Exception {
        // Arrange (Предварительная настройка)
        CreateCourseRequest createCourseRequest = new CreateCourseRequest();
        initializeCreateCourseRequest(createCourseRequest);
        // Act (Выполнение действия)
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createCourseRequest))) // contextPath == context или нет?
                .andExpect(status().isCreated()) // этот объект должен возвращаться StatusResultMatchers?
                .andReturn();
        // Assert (Проверка)
        String responseContext = result.getResponse().getContentAsString();
        CourseResponse response = objectMapper.readValue(responseContext, CourseResponse.class);
        assertThat(response).isNotNull(); // Откуда должен импортироваться assertThat?
        assertThat(response.getTitle()).isEqualTo("Integration Test Course");
        assertThat(response.getTeacherId()).isEqualTo(1L);
    }

    private void initializeCreateCourseRequest(CreateCourseRequest createCourseRequest) {
        createCourseRequest.setTitle("Integration Test Course");
        createCourseRequest.setDescription("Integration Test Description");
        createCourseRequest.setCategory("Integration Test Category");
        createCourseRequest.setPrice(100.0);
        createCourseRequest.setTeacherId(1L);  // Assuming user-service has user with ID 1 and role TEACHER
    }
}
