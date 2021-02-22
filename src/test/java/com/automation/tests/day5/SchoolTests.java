package com.automation.tests.day5;

import com.automation.pojos.Job;
import com.automation.pojos.Location;
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

public class SchoolTests {

    @BeforeAll
    public static void setup() {
        baseURI = ConfigurationReader.getValue("school.uri");
    }

    @Test
    @DisplayName("Create and Delete Student")
    public void test1() {

        String json = "{\n" +
                "  \"admissionNo\": \"1234\",\n" +
                "  \"batch\": 12,\n" +
                "  \"birthDate\": \"01/01/1890\",\n" +
                "  \"company\": {\n" +
                "    \"address\": {\n" +
                "      \"city\": \"McLean\",\n" +
                "      \"state\": \"Virginia\",\n" +
                "      \"street\": \"7925 Jones Branch Dr\",\n" +
                "      \"zipCode\": 22102\n" +
                "    },\n" +
                "    \"companyName\": \"Cybertek\",\n" +
                "    \"startDate\": \"02/02/2020\",\n" +
                "    \"title\": \"SDET\"\n" +
                "  },\n" +
                "  \"contact\": {\n" +
                "    \"emailAddress\": \"james_bond@gmail.com\",\n" +
                "    \"phone\": \"240-123-1231\",\n" +
                "    \"premanentAddress\": \"7925 Jones Branch Dr\"\n" +
                "  },\n" +
                "  \"firstName\": \"James\",\n" +
                "  \"gender\": \"Males\",\n" +
                "  \"joinDate\": \"01/01/3321\",\n" +
                "  \"lastName\": \"Bond\",\n" +
                "  \"major\": \"CS\",\n" +
                "  \"password\": \"1234\",\n" +
                "  \"section\": \"101010\",\n" +
                "  \"subject\": \"Math\"\n" +
                "}";


        //create student
        Response response = given().
                contentType(ContentType.JSON).
                body(json).
                post("student/create").prettyPeek();

        int studentId = response.jsonPath().getInt("studentId");

//        //delete student
        Response response2 = given().
                accept(ContentType.JSON).
                when().
                delete("student/delete/{id}", studentId).
                prettyPeek();


    }

    @Test
    @DisplayName("Delete student")
    public void test2() {
        Response response = given().
                accept(ContentType.JSON).
                when().
                delete("/student/delete/{id}", 19881).
                prettyPeek();
    }

    @Test
    @DisplayName("Create new student and read data from external JSON file")
    public void test3() {
        try {
            //read JSON file
            File file = new File(System.getProperty("user.dir") + "/student.json");

            Response response = given().
                    contentType(ContentType.JSON).
                    body(file).
                    post("student/create").prettyPeek();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Delete all students")
    public void test4() {

        space();

        Response response = given().
                accept(ContentType.JSON).
                when().
                get("/student/all");

        JsonPath jsonPath = response.jsonPath();

        List<Integer> studentIds = jsonPath.getList("students.studentId");

        System.out.println(studentIds);

        space();

        for (Integer studentId : studentIds) {
            delete("/student/delete/{id}", studentId);
        }

        System.out.println(studentIds);

        space();

    }
}


