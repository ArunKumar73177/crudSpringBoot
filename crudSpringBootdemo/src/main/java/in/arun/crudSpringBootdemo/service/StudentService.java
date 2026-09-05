package in.arun.crudSpringBootdemo.service;

import in.arun.crudSpringBootdemo.entity.Student;
import in.arun.crudSpringBootdemo.repositry.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private StudentRepository studentRepositry;

    public StudentService (StudentRepository studentRepositry){
        this.studentRepositry = studentRepositry;
    }

    public Student createStudent(Student studentReq){
        studentReq.setDeleted(false);

        Student studentResp = studentRepositry.save(studentReq);
        return studentResp;

    }

    public Student getStudent(Long id){
        Optional<Student> studentResp = studentRepositry.findByIdAndDeletedIsFalse(id);
        if(studentResp.isPresent()){
            return studentResp.get();
        }
        return null;
    }

    public List<Student> getAllStudent(){
        List<Student> studentList = studentRepositry.findByDeletedIsFalse();
        return studentList;
    }

    public Student updateStudent(Long id, Student studentReq){
        Optional<Student> existingStudent = studentRepositry.findByIdAndDeletedIsFalse(id);
        if(existingStudent.isEmpty()){
            return null;
        }
        Student studentToSave = existingStudent.get();

        studentToSave.setName(studentReq.getName());
        studentToSave.setAge(studentReq.getAge());
        studentToSave.setRollNo(studentReq.getRollNo());
        studentToSave.setEmail(studentReq.getEmail());
        studentToSave.setSubject(studentReq.getSubject());

        studentToSave.setDeleted(false);

        return studentRepositry.save(studentToSave);
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
}
