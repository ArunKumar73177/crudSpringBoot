package in.arun.crudSpringBootdemo.service;

import in.arun.crudSpringBootdemo.dto.CreateStudentRequestDTO;
import in.arun.crudSpringBootdemo.dto.CreateStudentResponseDTO;
import in.arun.crudSpringBootdemo.dto.UpdateStudentRequestDTO;
import in.arun.crudSpringBootdemo.dto.UpdateStudentResponseDTO;
import in.arun.crudSpringBootdemo.entity.Student;
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

        student.setCreatedAt(LocalDateTime.now());
        student.setUpdatedAt(LocalDateTime.now());

        Student studentResp = studentRepositry.save(student);
        return mapToDto(studentResp);

    }

    public CreateStudentResponseDTO getStudent(Long id){
        Optional<Student> studentResp = studentRepositry.findByIdAndDeletedIsFalse(id);
        if(studentResp.isPresent()){
            return mapToDto(studentResp.get());
        }
        return null;
    }

    public List<CreateStudentResponseDTO> getAllStudent(){
        List<Student> studentList = studentRepositry.findByDeletedIsFalse();
        return studentList.stream().map(this::mapToDto).toList();
    }

    public UpdateStudentResponseDTO updateStudent(Long id, UpdateStudentRequestDTO studentReq){
        Optional<Student> existingStudent = studentRepositry.findByIdAndDeletedIsFalse(id);
        if(existingStudent.isEmpty()){
            return null;
        }
        Student studentToSave = existingStudent.get();

        studentToSave.setName(studentReq.getName());
        studentToSave.setAge(studentReq.getAge());
        studentToSave.setRollNo(studentReq.getRollNo());
        studentToSave.setSubject(studentReq.getSubject());
        studentToSave.setUpdatedAt(LocalDateTime.now());

        studentToSave.setDeleted(false);

        Student savedStudent = studentRepositry.save(studentToSave);
        return mapToUpdateDTO(savedStudent);
    }

    public Boolean deleteStudent(Long id){
        Boolean isStudent = studentRepositry.existsById(id);
        if(!isStudent){
            return false;
        }
        studentRepositry.deleteById(id);
        return true;
    }

    public Boolean deleteStudentSoftly(Long id){
        Optional<Student> existingStudent = studentRepositry.findByIdAndDeletedIsFalse(id);
        if(existingStudent.isEmpty()){
            return false;
        }
        Student studentToSave = existingStudent.get();

        studentToSave.setDeleted(true);
        studentRepositry.save(studentToSave);
        return true;
    }
    private Student mapToEntity(CreateStudentRequestDTO studentReqDTO){
        Student student = new Student();
        student.setName(studentReqDTO.getName());
        student.setAge(studentReqDTO.getAge());
        student.setRollNo(studentReqDTO.getRollNo());
        student.setSubject(studentReqDTO.getSubject());
        student.setEmail(studentReqDTO.getEmail());
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
}
