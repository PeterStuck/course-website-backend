package peterstuck.coursewebsitebackend.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import java.util.Arrays;

public class JsonFilter {

    private static ObjectMapper mapper = new ObjectMapper();

    public static Object filterFields(Object obj, String filterName, String[] exceptFields) throws JsonProcessingException {
        String objAsString = castObjectToJsonString(obj, filterName, exceptFields);

        return mapper.readValue(objAsString, obj.getClass());
    }

    public static String castObjectToJsonString(Object obj, String filterName, String[] exceptFields) throws JsonProcessingException {
        SimpleBeanPropertyFilter simpleBeanPropertyFilter =
                SimpleBeanPropertyFilter.serializeAllExcept(
                        (exceptFields != null ? String.join(" ", exceptFields) : "")
                );

        FilterProvider filterProvider = new SimpleFilterProvider()
                .addFilter(filterName, simpleBeanPropertyFilter);

        return mapper.writer(filterProvider).writeValueAsString(obj);
    }

}
