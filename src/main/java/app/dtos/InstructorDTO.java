package app.dtos;

import app.entities.Instructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class InstructorDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Integer yearsOfExperience;
    private Set<SkiLessonDTO> skiLessons;

    public InstructorDTO(Instructor instructor){
        this.firstName = instructor.getFirstName();
        this.lastName = instructor.getLastName();
        this.email = instructor.getEmail();
        this.phone = instructor.getPhone();
        this.yearsOfExperience = instructor.getYearsOfExperience();
        if (instructor.getSkiLessons() != null) {
            this.skiLessons = instructor.getSkiLessons()
                    .stream()
                    .map(SkiLessonDTO::new)  // Mapper SkiLesson til SkiLessonDTO
                    .collect(Collectors.toSet());
        }
    }
}
