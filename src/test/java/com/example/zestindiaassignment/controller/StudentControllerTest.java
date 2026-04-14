package com.example.zestindiaassignment.controller;

import com.example.zestindiaassignment.dto.StudentDTO;
import com.example.zestindiaassignment.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    private StudentDTO testStudentDTO;

    @BeforeEach
    void setUp() {
        testStudentDTO = StudentDTO.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .age(20)
                .course("Computer Science")
                .createdDate(LocalDateTime.now())
                .build();
    }

    @Test
    void testHealth() throws Exception {
        mockMvc.perform(get("/api/students/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Service is running"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testGetAllStudents() throws Exception {
        // Arrange
        List<StudentDTO> students = Arrays.asList(testStudentDTO);
        when(studentService.getAllStudents()).thenReturn(students);

        // Act & Assert
        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].email").value("john@example.com"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testGetStudentById() throws Exception {
        // Arrange
        when(studentService.getStudentById(1L)).thenReturn(testStudentDTO);

        // Act & Assert
        mockMvc.perform(get("/api/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.email").value("john@example.com"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateStudent() throws Exception {
        // Arrange
        when(studentService.createStudent(any(StudentDTO.class))).thenReturn(testStudentDTO);

        // Act & Assert
        mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testStudentDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.email").value("john@example.com"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateStudent() throws Exception {
        // Arrange
        when(studentService.updateStudent(eq(1L), any(StudentDTO.class))).thenReturn(testStudentDTO);

        // Act & Assert
        mockMvc.perform(put("/api/students/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testStudentDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.email").value("john@example.com"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteStudent() throws Exception {
        // Arrange
        doNothing().when(studentService).deleteStudent(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Student deleted successfully"));
    }

    @Test
    void testGetAllStudents_Unauthorized() throws Exception {
        mockMvc.perform(get("/api/students"))
                .andExpect(status().isUnauthorized());
    }
}

