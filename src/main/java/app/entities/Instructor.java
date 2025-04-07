package app.entities;


import app.dtos.InstructorDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Instructor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Integer yearsOfExperience;

    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER, mappedBy = "instructor")
    private Set<SkiLesson> skiLessons = new HashSet<>();


    public Instructor(String firstName, String lastName, String email, String phone, Integer yearsOfExperience) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.yearsOfExperience = yearsOfExperience;
    }

    public Instructor(InstructorDTO dto) {
        this.firstName = dto.getFirstName();
        this.lastName = dto.getLastName();
        this.email = dto.getEmail();
        this.phone = dto.getPhone();
        this.yearsOfExperience = dto.getYearsOfExperience();
    }

    public void addLesson(SkiLesson skiLesson)
    {
        if (skiLesson != null)
        {
            skiLessons.add(skiLesson);
            skiLesson.setInstructor(this);
        }
    }

    public void removeLesson(SkiLesson skiLesson)
    {
        if (skiLesson != null)
        {
            skiLessons.remove(skiLesson);
            skiLesson.setInstructor(null);
        }
    }
}

