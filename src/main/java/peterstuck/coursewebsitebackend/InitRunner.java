package peterstuck.coursewebsitebackend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import peterstuck.coursewebsitebackend.factory.course.CourseFactory;
import peterstuck.coursewebsitebackend.factory.course_description.CourseDescriptionFactory;
import peterstuck.coursewebsitebackend.models.course.Category;
import peterstuck.coursewebsitebackend.repositories.CategoryRepository;
import peterstuck.coursewebsitebackend.repositories.CourseRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

@Component
public class InitRunner implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(InitRunner.class);

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Override
    public void run(String... args) throws Exception {
        initializeCategories();
        initializeCourses();
    }

    @Transactional
    void initializeCategories() {
        categoryRepository.save(new Category("Programowanie", 0));
        categoryRepository.save(new Category("Biznes", 0));
        categoryRepository.save(new Category("Projektowanie", 0));
        categoryRepository.save(new Category("Tworzenie stron internetowych", 1));
        categoryRepository.save(new Category("Tworzenie gier", 1));
        categoryRepository.save(new Category("Prowadzenie firmy", 2));
        categoryRepository.save(new Category("Komunikacja i zarządzanie", 2));
        categoryRepository.save(new Category("Narzędzia projektowe", 3));

        categoryRepository.findAll().forEach(category -> logger.info("CREATED CATEGORY {}", category.getName()));
    }

    @Transactional
    void initializeCourses() {
        var course = CourseFactory.createCourse(
                "Kurs Tworzenia Stron WWW w HTML i CSS",
                59.99,
                CourseDescriptionFactory.createCourseDescription(
                        48.5,
                        "Naucz się HTML 5, CSS 3, JS, XML, jQuery, AJAX, Responsive web design od podstaw. Wyjaśnię Ci wszystko od A do Z!",
                        """
                                Naucz się wszystkiego od zera. Obal mit, że tworzenie stron internetowych jest trudne. Twórz własne strony WWW od A do Z.\s                         
                                Będziesz znał i rozumiał HTML 5, CSS 3, JS, XML, jQuery oraz AJAX.\s                           
                                Co więcej nauczysz się Responsive Web Design czyli techniki, która sprawi, że Twoja strona będzie wyglądać idealnie na smartfonach jak i na komputerach stacjonarnych.\s                     
                                """,
                        Arrays.asList(
                                "tworzyć od zera stronę WWW w HTML z rozwijanym menu w CSS",
                                "czym są, jakie są oraz jak korzystać z tagów HTML/selektorów CSS",
                                "nowości związane z HTML 5: tagi semantyczne, eventy, atrybuty",
                                "różnice między HTML, XHTML, HTML 5, CSS i CSS 3"
                        ),
                        Collections.singletonList("Wszystko wyjaśnione jest w kursie. Nie musisz posiadać żadnych wiadomości. Wystarczą dobre chęci :)")),
                new ArrayList<>(Arrays.asList(categoryRepository.findById(1).get()))
        );

        var course2 = CourseFactory.createCourse(
                "Kurs Tworzenia Stron WWW w HTML i CSS",
                59.99,
                CourseDescriptionFactory.createCourseDescription(
                        48.5,
                        "Naucz się HTML 5, CSS 3, JS, XML, jQuery, AJAX, Responsive web design od podstaw. Wyjaśnię Ci wszystko od A do Z!",
                        """
                                Naucz się wszystkiego od zera. Obal mit, że tworzenie stron internetowych jest trudne. Twórz własne strony WWW od A do Z.\s                         
                                Będziesz znał i rozumiał HTML 5, CSS 3, JS, XML, jQuery oraz AJAX.\s                           
                                Co więcej nauczysz się Responsive Web Design czyli techniki, która sprawi, że Twoja strona będzie wyglądać idealnie na smartfonach jak i na komputerach stacjonarnych.\s                     
                                """,
                        Arrays.asList(
                                "tworzyć od zera stronę WWW w HTML z rozwijanym menu w CSS",
                                "czym są, jakie są oraz jak korzystać z tagów HTML/selektorów CSS",
                                "nowości związane z HTML 5: tagi semantyczne, eventy, atrybuty",
                                "różnice między HTML, XHTML, HTML 5, CSS i CSS 3"
                        ),
                        Collections.singletonList("Wszystko wyjaśnione jest w kursie. Nie musisz posiadać żadnych wiadomości. Wystarczą dobre chęci :)")),
                new ArrayList<>(Arrays.asList(categoryRepository.findById(1).get()))
        );

        courseRepository.save(course);
        courseRepository.save(course2);

        courseRepository.findAll().forEach(c -> logger.info("CREATED COURSE {}", c.getTitle()));
    }

}
