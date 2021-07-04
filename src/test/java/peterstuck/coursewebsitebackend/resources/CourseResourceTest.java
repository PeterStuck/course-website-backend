package peterstuck.coursewebsitebackend.resources;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import peterstuck.coursewebsitebackend.factory.CourseFactory;
import peterstuck.coursewebsitebackend.models.Category;
import peterstuck.coursewebsitebackend.models.Course;
import peterstuck.coursewebsitebackend.repositories.CourseRepository;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CourseResourceTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CourseRepository repository;

    private List<Course> testCourses;
    private List<Category> testCategories;
    private Course testCourse;

    private static final String BASE_PATH = "/api/courses";

    @BeforeEach
    void setUp() {
        testCategories = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            var cat = new Category("CATEGORY " + i, 0);
            cat.setId(i + 1);
            testCategories.add(cat);
        }

        testCourses = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            var course = CourseFactory.createCourse("TEST " + i, Math.max(i, 1.0));
            course.setCategories(testCategories);
            testCourses.add(course);
        }

        testCourse = CourseFactory.createCourse("TEST", 10.0);
    }

    @Test
    void givenCoursesWhenGetCoursesThenStatus200AndListOfExistingCourses() throws Exception {
        when(repository.findAll()).thenReturn(testCourses);

        List<Course> courses = makeRequestToGetCourses(BASE_PATH);

        verify(repository).findAll();

        assertThat(courses, hasSize(3));
        assertThat(courses.get(0).getTitle(), equalTo("TEST 0"));
        assertThat(courses.get(0).getPrice(), equalTo(1.0));
    }

    @Test
    void whenNoCoursesPresentReturnEmptyListAndStatus200() throws Exception {
        List<Course> courses = makeRequestToGetCourses(BASE_PATH);

        assertThat(courses, hasSize(0));
    }

    @Test
    void whenKeywordIsPassedShouldReturnFilteredCourses() throws Exception {
        testCourses.add(CourseFactory.createCourse("TEST WITH KEYWORD", 5.0));
        testCourses.add(CourseFactory.createCourse("TEST WITH KEYWORD 2", 4.5));
        when(repository.findAll()).thenReturn(testCourses);

        String keyword = "KEYWORD";
        List<Course> filteredCourses = makeRequestToGetCourses(BASE_PATH + "?keyword=" + keyword);

        assertThat(filteredCourses, hasSize(2));
        assertThat(filteredCourses.get(0).getTitle(), equalTo("TEST WITH KEYWORD"));
        assertThat(filteredCourses.get(0).getPrice(), equalTo(5.0));
    }

    @Test
    void whenCourseWithGivenIdExistsThenReturnCourse() throws Exception {
        int id = 1;
        when(repository.findById(id)).thenReturn(Optional.ofNullable(testCourse));

        var response = makeRequestToGetSingleCourse(BASE_PATH + "/" + id, status().isOk());
        Course course = objectMapper().readValue(response.getContentAsString(), Course.class);

        verify(repository).findById(id);
        assertThat(course.getTitle(), equalTo(testCourse.getTitle()));
        assertThat(course.getPrice(), equalTo(testCourse.getPrice()));
    }

    @Test
    void whenCourseWithGivenIdNotExistsThenStatus404() throws Exception {
        var response = makeRequestToGetSingleCourse(BASE_PATH + "/999", status().isNotFound());

        assertThat(response.getContentAsString(), containsString("message"));
        assertThat(response.getContentAsString(), containsString("not found"));
    }

    @Test
    void whenNoCoursesWithCategoryReturnEmptyListWithStatus200() throws Exception {
        when(repository.findAll()).thenReturn(testCourses);
        List<Course> courses = makeRequestToGetCourses(BASE_PATH + "/category/999");

        verify(repository).findAll();
        assertThat(courses, hasSize(0));
    }

    @Test
    void shouldReturnFilteredListOfCoursesWithGivenCategoryIdAndStatus200() throws Exception {
        testCourses.get(0).setCategories(Collections.emptyList());
        when(repository.findAll()).thenReturn(testCourses);

        List<Course> courses = makeRequestToGetCourses(BASE_PATH + "/category/1");

        assertThat(courses, hasSize(2));
        assertThat(courses.get(0).getTitle(), not(equalTo(testCourses.get(0).getTitle())));
    }

    @Test
    void shouldReturnListOfCoursesFilteredByCategoryAndKeywordWhenKeywordProvided() throws Exception {
        testCourses.add(CourseFactory.createCourse("Course with keyword 1", 1.0, testCategories));
        testCourses.add(CourseFactory.createCourse("Course with keyword 2", 2.0, testCategories));
        when(repository.findAll()).thenReturn(testCourses);

        String keyword = "KeYwoRD";
        List<Course> courses = makeRequestToGetCourses(BASE_PATH + "/category/1?keyword=" + keyword);

        assertThat(courses, hasSize(2));
        assertThat(courses.get(0).getTitle(), equalTo("Course with keyword 1"));
    }

    @Test
    void shouldAddNewCourseAndReturnNewObjectWithStatus201() throws Exception {
        when(repository.findAll()).thenReturn(testCourses);
        when(repository.save(any())).then(invocationOnMock -> {
            testCourses.add(testCourse);
            return testCourse;
        });

        var response = makePostCourseRequest(testCourse, status().isCreated());
        Course course = objectMapper().readValue(response.getContentAsString(), Course.class);

        verify(repository).save(any());
        assertThat(course.getTitle(), equalTo(testCourse.getTitle()));
        assertThat(repository.findAll(), hasSize(4));
    }

    @Test
    void shouldDeleteCourseWhenExists() throws Exception {
        testCourses.add(testCourse);
        when(repository.findAll()).thenReturn(testCourses);
        when(repository.findById(1)).then(invocationOnMock -> {
                testCourses.remove(testCourse);
                return Optional.ofNullable(testCourse);
        });

        mvc.perform(delete(BASE_PATH + "/1"))
                .andExpect(status().isOk());

        verify(repository).delete(testCourse);
        assertThat(repository.findAll(), hasSize(3));
    }

    @Test
    void shouldReturnStatus404WhenCourseNotFound() throws Exception {
        mvc.perform(delete(BASE_PATH + "/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdateWhenCourseDataIsValid() throws Exception {
        testCourse.setId(1);
        when(repository.findById(1)).thenReturn(Optional.ofNullable(testCourse));
        var updatedTestCourse = cloneCourse(testCourse);
        updatedTestCourse.setTitle("NEW TITLE");

        makePostPutCourseRequest(BASE_PATH + "/1", updatedTestCourse, status().isOk());

        verify(repository).findById(1);
        verify(repository).save(testCourse);
        assertThat(repository.findById(1).get().getTitle(), equalTo("NEW TITLE"));
    }

    private List<Course> makeRequestToGetCourses(String path) throws Exception {
        MockHttpServletResponse response =  mvc.perform(get(path).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        return objectMapper().readValue(response.getContentAsString(), new TypeReference<>(){});
    }

    private MockHttpServletResponse makeRequestToGetSingleCourse(String path, ResultMatcher expectedStatus) throws Exception {
        return mvc.perform(get(path).contentType(MediaType.APPLICATION_JSON))
                .andExpect(expectedStatus)
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
    }

    private void makePostPutCourseRequest(String path, Course content, ResultMatcher expectedStatus) throws Exception {
        mvc.perform(
                put(path)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toJson(content)
                    ))
                .andExpect(expectedStatus);
    }

    private MockHttpServletResponse makePostCourseRequest(Course content, ResultMatcher expectedStatus) throws Exception {
        return mvc.perform(
                post(CourseResourceTest.BASE_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(content)
                        ))
                .andExpect(expectedStatus)
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
    }

    private ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    // TODO fix FilterProvider exception with Jackson
    private String toJson(Course course) {
        return "{\n" +
                "    \"id\": \""+ course.getId() +"\",\n" +
                "    \"title\": \""+ course.getTitle() +"\",\n" +
                "    \"languages\":" + course.getLanguages() + ",\n" +
                "    \"subtitles\": " + course.getSubtitles() + ",\n" +
                "    \"categories\": " + course.getCategories() + ",\n" +
                "    \"lastUpdate\": " + course.getLastUpdate() + ",\n" +
                "    \"price\": " + course.getPrice() + "\n" +
                "}";
    }
    
    private Course cloneCourse(Course original) {
        Course cloned = new Course();
        cloned.setId(original.getId());
        cloned.setTitle(original.getTitle());
        cloned.setLastUpdate(original.getLastUpdate());
        cloned.setComments(original.getComments());
        cloned.setCourseDescription(original.getCourseDescription());
        cloned.setSubtitles(original.getSubtitles());
        cloned.setCategories(original.getCategories());
        cloned.setPrice(original.getPrice());
        cloned.setLanguages(original.getLanguages());
        cloned.setRates(original.getRates());
        return cloned;
    }

}