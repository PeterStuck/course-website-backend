package peterstuck.coursewebsitebackend.models.course.builder.course;

import peterstuck.coursewebsitebackend.models.course.*;

import java.util.List;
import java.util.Set;

public interface CourseBuilder {

    CourseBuilder buildTitle(String title);

    CourseBuilder buildLanguages(Set<Language> languages);

    CourseBuilder buildSubtitles(Set<Language> subtitles);

    CourseBuilder buildCategories(List<Category> categories);

    CourseBuilder buildComments(List<Comment> comments);

    CourseBuilder buildPrice(Double price);

    CourseBuilder buildCourseDescription(CourseDescription description);

    CourseBuilder buildCourseFeedback(CourseFeedback courseFeedback);

    Course getResult();

    void restore();

}
