package app.dtos;

import app.entities.SkiLesson;
import app.enums.Levels;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SkiLessonDTO {
    private String name;
    private String startTime;
    private String endTime;
    private Double price;
    private Levels level;
    private LocationDTO location;
    private InstructorDTO instructor;

    public SkiLessonDTO(SkiLesson entity){
        this.name = entity.getName();
        this.price = entity.getPrice();
        this.level = entity.getLevel();
        this.startTime = entity.getStartTime().toString();
        this.endTime = entity.getEndTime().toString();
        this.location = new LocationDTO(entity.getLocation().getLatitude(), entity.getLocation().getLongitude());
        this.instructor = entity.getInstructor()==null ? null : new InstructorDTO(entity.getInstructor());
    }
}
