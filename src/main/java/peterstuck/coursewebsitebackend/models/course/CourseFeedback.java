package peterstuck.coursewebsitebackend.models.course;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@JsonFilter("JsonFilter")
@Getter
@Setter
@Entity
@Table(name = "course_feedback")
public class CourseFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnoreProperties(value = {"course_feedback", "hibernateLazyInitializer"})
    @OneToMany(mappedBy = "courseFeedback", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @Schema(description = "Average rate from all rates for course")
    private double avgRate;

    private int ratesCount;

    public CourseFeedback() {
        comments = new ArrayList<>();
        avgRate = 0.0;
        ratesCount = 0;
    }
}
