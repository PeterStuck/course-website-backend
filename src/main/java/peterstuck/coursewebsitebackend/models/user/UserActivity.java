package peterstuck.coursewebsitebackend.models.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import peterstuck.coursewebsitebackend.models.course.Comment;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "user_activity")
public class UserActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @OneToMany(mappedBy = "author")
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private List<Comment> comments;

    public UserActivity() {
        comments = new ArrayList<>();
    }
}
