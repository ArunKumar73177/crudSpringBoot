package in.arun.crudSpringBootdemo.dto;

import jakarta.validation.constraints.*;
import org.springframework.beans.factory.annotation.Value;

public class CreateStudentRequestDTO {
    @NotBlank(message = "Name cannot be empty/null or blank")
    @Size(min = 2, max = 50, message = "Name must be greater then 2 characters and less then 50 characters")
    private String name;

    @NotNull(message = "Age is required")
    @Min(value = 18, message = "Age must be greater then 18")
    private Integer age;

    @NotNull(message = "RollNo is required")
    private Integer rollNo;

    @NotBlank(message = "Subject is required")
    private String subject;

    @NotNull(message = "Email cant be blank")
    @Email(message = "Email must be valid")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getRollNo() {
        return rollNo;
    }

    public void setRollNo(int rollNo) {
        this.rollNo = rollNo;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
