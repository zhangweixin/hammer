package com.nathaniel.app.core.test.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nathaniel.app.core.test.jackson.model.User;
import org.junit.Test;

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
}
