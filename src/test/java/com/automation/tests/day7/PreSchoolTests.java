package com.automation.tests.day7;

import com.automation.pojos.Job;
import com.automation.pojos.Location;
import com.automation.pojos.school.Student;
import com.automation.utilities.ConfigurationReader;
import com.google.gson.Gson;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static com.automation.utilities.utils.*;
import static org.junit.jupiter.api.Assertions.*;

public class PreSchoolTests {

    @BeforeAll
    public static void setup() {
        baseURI = ConfigurationReader.getValue("school.uri");
    }

    @Test
    @DisplayName("Get student with id 19982 and convert payload into POJO")
    public void test1() {
        Response response = given().
                accept(ContentType.JSON).
                pathParam("id", 19982).
                when().
                get("/student/{id}").prettyPeek();

        // deserialization
        // from JSON to POJO
        // student - is a POJO
        Student student = response.jsonPath().getObject("students[0]", Student.class);

        System.out.println(student);

        assertEquals(19982, student.getStudentId());
        assertEquals(14, student.getBatch());
        assertEquals("string", student.getAdmissionNo());
        assertEquals("123 main str", student.getContact().getPermanentAddress());
        assertEquals("aaa@gmail.com", student.getContact().getEmailAddress());
    }


}
