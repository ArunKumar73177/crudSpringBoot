package in.arun.crudSpringBootdemo.service;

import in.arun.crudSpringBootdemo.dto.CreateStudentRequestDTO;
import in.arun.crudSpringBootdemo.dto.CreateStudentResponseDTO;
import in.arun.crudSpringBootdemo.dto.UpdateStudentRequestDTO;
import in.arun.crudSpringBootdemo.dto.UpdateStudentResponseDTO;
import in.arun.crudSpringBootdemo.entity.Student;
import in.arun.crudSpringBootdemo.exception.DuplicateResourceException;
import in.arun.crudSpringBootdemo.exception.ResourceNotFoundException;
import in.arun.crudSpringBootdemo.repositry.StudentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private StudentRepository studentRepositry;

    public StudentService (StudentRepository studentRepositry){
        this.studentRepositry = studentRepositry;
    }

    public CreateStudentResponseDTO createStudent(CreateStudentRequestDTO studentReqDto){
        Student student = mapToEntity(studentReqDto);
        if(emailExists(student)){
            throw new DuplicateResourceException("Student with email " + student.getEmail() + " already exists");
        }

        Student studentResp = studentRepositry.save(student);
        return mapToDto(studentResp);

    }

    public CreateStudentResponseDTO getStudent(Long id){
        Student studentResp = studentRepositry
                .findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student with id " + id + " not found"));
        return mapToDto(studentResp);

    }

    public List<CreateStudentResponseDTO> getAllStudent(){
        List<Student> studentList = studentRepositry.findByDeletedIsFalse();
        return studentList.stream().map(this::mapToDto).toList();
    }

    public UpdateStudentResponseDTO updateStudent(Long id, UpdateStudentRequestDTO studentReq){
        Student existingStudent = studentRepositry
                .findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student with id " + id + " not found"));


        existingStudent.setName(studentReq.getName());
        existingStudent.setAge(studentReq.getAge());
        existingStudent.setRollNo(studentReq.getRollNo());
        existingStudent.setSubject(studentReq.getSubject());
        existingStudent.setUpdatedAt(LocalDateTime.now());

        existingStudent.setDeleted(false);

        Student savedStudent = studentRepositry.save(existingStudent);
        return mapToUpdateDTO(savedStudent);
    }

    public void deleteStudent(Long id){
        Student studentToBeDeleted = studentRepositry
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student with id " + id + " not found"));
        studentRepositry.delete(studentToBeDeleted);
    }

    public void deleteStudentSoftly(Long id){
        Student studentToBeDeleted = studentRepositry
                .findByIdAndDeletedIsFalse(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student with id " + id + " not found"));

        studentToBeDeleted.setDeleted(true);

        studentRepositry.save(studentToBeDeleted);

    }
    private Student mapToEntity(CreateStudentRequestDTO studentReqDTO){
        Student student = new Student();
        student.setName(studentReqDTO.getName());
        student.setAge(studentReqDTO.getAge());
        student.setRollNo(studentReqDTO.getRollNo());
        student.setSubject(studentReqDTO.getSubject());
        student.setEmail(studentReqDTO.getEmail());
        student.setCreatedAt(LocalDateTime.now());
        student.setUpdatedAt(LocalDateTime.now());

        student.setDeleted(false);
        return student;
    }

    private CreateStudentResponseDTO mapToDto(Student student){
        CreateStudentResponseDTO responseDTO = new CreateStudentResponseDTO();
        responseDTO.setId(student.getId());
        responseDTO.setName(student.getName());
        responseDTO.setAge(student.getAge());
        responseDTO.setEmail(student.getEmail());
        responseDTO.setRollNo(student.getRollNo());
        responseDTO.setSubject(student.getSubject());
        responseDTO.setMessage("Student saved Successfully");
        responseDTO.setCreatedAt(student.getCreatedAt());
        responseDTO.setUpdatedAt(student.getUpdatedAt());

        return responseDTO;
    }

    private UpdateStudentResponseDTO mapToUpdateDTO(Student student){
        UpdateStudentResponseDTO responseDTO = new UpdateStudentResponseDTO();

        responseDTO.setId(student.getId());
        responseDTO.setName(student.getName());
        responseDTO.setAge(student.getAge());
        responseDTO.setEmail(student.getEmail());
        responseDTO.setRollNo(student.getRollNo());
        responseDTO.setSubject(student.getSubject());
        responseDTO.setMessage("Student updated Successfully");
        responseDTO.setUpdatedAt(student.getUpdatedAt());

        return responseDTO;
    }

    private boolean emailExists(Student student){
        return studentRepositry.existsByEmail(student.getEmail());
    }
}
