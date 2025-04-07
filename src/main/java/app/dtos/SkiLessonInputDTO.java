package app.dtos;

import app.enums.Levels;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SkiLessonInputDTO {
    private String name;
    private String startTime;
    private String endTime;
    private Double price;
    private Levels level;
    private LocationDTO location;
    private Integer instructorId;
}
