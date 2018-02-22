package com.nathaniel.app.core.test.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nathaniel.app.core.test.jackson.model.User;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class JacksonTest {
    @Test
    public void testSerialize() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        User user = new User();
        user.setBirthday(new Date());
        user.setName("张亮");
        user.setAddress("深圳");
        System.out.println(mapper.writeValueAsString(user));
    }

    @Test
    public void testDate() {
        Date date = new Date();
        LocalDate from = LocalDate.now();
        System.out.println(from.toString());

        LocalTime time = LocalTime.now();
        System.out.println(time.toString());

        LocalDateTime dateTime = LocalDateTime.now();
        System.out.println(dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:ss:mm")));
    }
}
