package com.example.zestindiaassignment.service;

import com.example.zestindiaassignment.dto.StudentDTO;
import com.example.zestindiaassignment.entity.Student;
import com.example.zestindiaassignment.exception.InvalidInputException;
import com.example.zestindiaassignment.exception.ResourceNotFoundException;
import com.example.zestindiaassignment.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    private Student testStudent;
    private StudentDTO testStudentDTO;

    @BeforeEach
    void setUp() {
        testStudent = Student.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .age(20)
                .course("Computer Science")
                .createdDate(LocalDateTime.now())
                .build();

        testStudentDTO = StudentDTO.builder()
                .name("John Doe")
                .email("john@example.com")
                .age(20)
                .course("Computer Science")
                .build();
    }

    @Test
    void testGetAllStudents() {
        // Arrange
        List<Student> students = Arrays.asList(testStudent);
        when(studentRepository.findAll()).thenReturn(students);

        // Act
        List<StudentDTO> result = studentService.getAllStudents();

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getEmail()).isEqualTo("john@example.com");
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void testGetStudentById_Success() {
        // Arrange
        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));

        // Act
        StudentDTO result = studentService.getStudentById(1L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("john@example.com");
        verify(studentRepository, times(1)).findById(1L);
    }

    @Test
    void testGetStudentById_NotFound() {
        // Arrange
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> studentService.getStudentById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Student not found");
    }

    @Test
    void testGetStudentById_InvalidId() {
        // Act & Assert
        assertThatThrownBy(() -> studentService.getStudentById(-1L))
                .isInstanceOf(InvalidInputException.class)
                .hasMessageContaining("positive number");
    }

    @Test
    void testCreateStudent_Success() {
        // Arrange
        when(studentRepository.existsByEmail(testStudentDTO.getEmail())).thenReturn(false);
        when(studentRepository.save(any(Student.class))).thenReturn(testStudent);

        // Act
        StudentDTO result = studentService.createStudent(testStudentDTO);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("john@example.com");
        verify(studentRepository, times(1)).existsByEmail(testStudentDTO.getEmail());
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    void testCreateStudent_DuplicateEmail() {
        // Arrange
        when(studentRepository.existsByEmail(testStudentDTO.getEmail())).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> studentService.createStudent(testStudentDTO))
                .isInstanceOf(InvalidInputException.class)
                .hasMessageContaining("Email already exists");
    }

    @Test
    void testUpdateStudent_Success() {
        // Arrange
        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));
        when(studentRepository.existsByEmail(testStudentDTO.getEmail())).thenReturn(false);
        when(studentRepository.save(any(Student.class))).thenReturn(testStudent);

        // Act
        StudentDTO result = studentService.updateStudent(1L, testStudentDTO);

        // Assert
        assertThat(result).isNotNull();
        verify(studentRepository, times(1)).findById(1L);
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    void testUpdateStudent_NotFound() {
        // Arrange
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> studentService.updateStudent(99L, testStudentDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Student not found");
    }

    @Test
    void testDeleteStudent_Success() {
        // Arrange
        when(studentRepository.existsById(1L)).thenReturn(true);

        // Act
        studentService.deleteStudent(1L);

        // Assert
        verify(studentRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteStudent_NotFound() {
        // Arrange
        when(studentRepository.existsById(99L)).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> studentService.deleteStudent(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Student not found");
    }
}

