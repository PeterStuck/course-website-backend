package peterstuck.coursewebsitebackend.models.course.builder.course_description;

import peterstuck.coursewebsitebackend.models.course.CourseDescription;

import java.util.List;

public interface CourseDescriptionBuilder {

    CourseDescriptionBuilder buildDuration(Double duration);

    CourseDescriptionBuilder buildShortDescription(String shortDesc);

    CourseDescriptionBuilder buildLongDescription(String longDesc);

    CourseDescriptionBuilder buildMainTopics(List<String> topics);

    CourseDescriptionBuilder buildRequirements(List<String> requirements);

    CourseDescription getResult();

    void restore();

}
