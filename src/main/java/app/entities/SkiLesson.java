package app.entities;

import app.dtos.SkiLessonDTO;
import app.enums.Levels;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SkiLesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private LocalTime startTime;
    private LocalTime endTime;
    private Double price;
    private Levels level;
    @Embedded
    @ToString.Exclude
    private Location location;

    @ManyToOne
    @JoinColumn(name = "instructor_id")
    private Instructor instructor;


    public SkiLesson(String name, LocalTime startTime, LocalTime endTime, Double price, Levels level, Location location, Instructor instructor) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.price = price;
        this.level = level;
        this.location = location;
        this.instructor = instructor;
    }

    public SkiLesson(SkiLessonDTO skiLessonDTO){
        this.name = skiLessonDTO.getName();
        this.startTime = LocalTime.parse(skiLessonDTO.getStartTime());
        this.endTime = LocalTime.parse(skiLessonDTO.getEndTime());
        this.price = skiLessonDTO.getPrice();
        this.level = skiLessonDTO.getLevel();
        this.location = new Location(skiLessonDTO.getLocation().getLatitude(), skiLessonDTO.getLocation().getLongitude());

    }

    @Embeddable
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @EqualsAndHashCode
    public static class Location{
        private double latitude;
        private double longitude;
    }
}