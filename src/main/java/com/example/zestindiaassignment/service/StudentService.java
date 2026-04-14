package com.example.zestindiaassignment.service;

import com.example.zestindiaassignment.dto.StudentDTO;
import com.example.zestindiaassignment.entity.Student;
import com.example.zestindiaassignment.exception.InvalidInputException;
import com.example.zestindiaassignment.exception.ResourceNotFoundException;
import com.example.zestindiaassignment.repository.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<StudentDTO> getAllStudents() {
        log.info("Fetching all students");
        return studentRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public StudentDTO getStudentById(Long id) {
        log.info("Fetching student with id: {}", id);
        if (id == null || id <= 0) {
            throw new InvalidInputException("Student ID must be a positive number");
        }
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
        return convertToDTO(student);
    }

    public StudentDTO createStudent(StudentDTO studentDTO) {
        log.info("Creating new student with email: {}", studentDTO.getEmail());

        if (studentRepository.existsByEmail(studentDTO.getEmail())) {
            throw new InvalidInputException("Email already exists: " + studentDTO.getEmail());
        }

        Student student = convertToEntity(studentDTO);
        Student savedStudent = studentRepository.save(student);
        log.info("Student created successfully with id: {}", savedStudent.getId());
        return convertToDTO(savedStudent);
    }

    public StudentDTO updateStudent(Long id, StudentDTO studentDTO) {
        log.info("Updating student with id: {}", id);

        if (id == null || id <= 0) {
            throw new InvalidInputException("Student ID must be a positive number");
        }

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));

        // Check if email is being changed and if new email already exists
        if (!student.getEmail().equals(studentDTO.getEmail())
                && studentRepository.existsByEmail(studentDTO.getEmail())) {
            throw new InvalidInputException("Email already exists: " + studentDTO.getEmail());
        }

        student.setName(studentDTO.getName());
        student.setEmail(studentDTO.getEmail());
        student.setAge(studentDTO.getAge());
        student.setCourse(studentDTO.getCourse());

        Student updatedStudent = studentRepository.save(student);
        log.info("Student updated successfully with id: {}", updatedStudent.getId());
        return convertToDTO(updatedStudent);
    }

    public void deleteStudent(Long id) {
        log.info("Deleting student with id: {}", id);

        if (id == null || id <= 0) {
            throw new InvalidInputException("Student ID must be a positive number");
        }

        if (!studentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Student not found with id: " + id);
        }

        studentRepository.deleteById(id);
        log.info("Student deleted successfully with id: {}", id);
    }

    private StudentDTO convertToDTO(Student student) {
        return StudentDTO.builder()
                .id(student.getId())
                .name(student.getName())
                .email(student.getEmail())
                .age(student.getAge())
                .course(student.getCourse())
                .createdDate(student.getCreatedDate())
                .build();
    }

    private Student convertToEntity(StudentDTO dto) {
        return Student.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .age(dto.getAge())
                .course(dto.getCourse())
                .build();
    }
}

