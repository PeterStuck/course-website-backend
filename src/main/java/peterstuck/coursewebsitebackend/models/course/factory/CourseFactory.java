package peterstuck.coursewebsitebackend.models.course.factory;

import peterstuck.coursewebsitebackend.models.course.builder.course.CourseBuilder;
import peterstuck.coursewebsitebackend.models.course.builder.course.StandardCourseBuilder;
import peterstuck.coursewebsitebackend.models.course.*;

import java.util.List;
import java.util.Set;

public class CourseFactory {

    private static CourseBuilder courseBuilder = new StandardCourseBuilder();

    public static Course createCourse(String title, Double price, CourseDescription description) {
        Course course = courseBuilder
                .buildTitle(title)
                .buildPrice(price)
                .buildCourseDescription(description)
                .getResult();
        courseBuilder.restore();

        return course;
    }

    public static Course createCourse(String title, Double price, CourseDescription description, List<Category> categories) {
        Course course = courseBuilder
                .buildTitle(title)
                .buildPrice(price)
                .buildCourseDescription(description)
                .buildCategories(categories)
                .getResult();
        courseBuilder.restore();

        return course;
    }

    public static Course createCourse(String title,
                                      Double price,
                                      CourseDescription description,
                                      Set<Language> languages,
                                      Set<Language> subtitles,
                                      List<Category> categories,
                                      CourseFeedback courseFeedback) {
        Course course = courseBuilder
                .buildTitle(title)
                .buildPrice(price)
                .buildCourseDescription(description)
                .buildLanguages(languages)
                .buildSubtitles(subtitles)
                .buildCategories(categories)
                .buildCourseFeedback(courseFeedback)
                .getResult();
        courseBuilder.restore();

        return course;
    }

    public static void setCourseBuilder(CourseBuilder builder) {
        courseBuilder = builder;
    }

}
