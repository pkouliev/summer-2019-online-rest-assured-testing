package com.automation.tests.day5;

import com.automation.utilities.ConfigurationReader;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static com.automation.utilities.utils.*;
import static org.junit.jupiter.api.Assertions.*;

public class ORDSTestsDay5 {

    @BeforeAll
    public static void setup() {
        baseURI = ConfigurationReader.getValue("ords.uri");
    }

    /**
     * WARMUP
     * given path parameter is "/employees"
     * when user makes get request
     * then user verifies that status code is 200
     * and user verifies that average salary is greater than 5000
     */
    @Test
    @DisplayName("verify that average salary is greater than 5000")
    public void test1() {
        space();
        Response response = given().
                accept(ContentType.JSON).
                when().
                get("/employees");

        JsonPath jsonPath = response.jsonPath();

        List<Integer> salaries = jsonPath.getList("items.salary");
        System.out.println(salaries);
        space();

        int sum = 0;
        // we are finding a sum of all salaries
        for (int salary : salaries) {
            sum += salary;
        }
        // we are calculating average salary sum/count
        int avg = sum / salaries.size();
        // we are asserting that average salary is greater 5000
        assertTrue(avg > 5000, "ERROR: actually average salary is lower than 5000: " + avg);
    }

}
