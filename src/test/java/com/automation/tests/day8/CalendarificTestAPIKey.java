package com.automation.tests.day8;

import com.automation.utilities.ConfigurationReader;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static com.automation.utilities.utils.*;
import static org.junit.jupiter.api.Assertions.*;

public class CalendarificTestAPIKey {

    String api_key = ConfigurationReader.getValue("calendarific.api.key");

    @BeforeAll
    public static void setup() {
        baseURI = ConfigurationReader.getValue("calendarific.uri");
    }

    /*
     * API key is a secret that the API generates and gives to the developer
     * API key looks like long string: 487c0ef84240c0292ef162c3756517edff4a7586
     * API key can go as query parameter or inside header, it depends on web service how you must pass API key
     * How it gets created? You go to web site, register, and service gives you API key
     * Then you have you have to pass API key alongside with every request
     * API key is easy to implement for developer and client
     * But, non-technical people have no idea about this
     * So it's mostly used by developers only
     */

    /**
     * Given accept content type as JSON
     * When user sends GET request to "/countries"
     * Then user verifies that status code is 401
     * And user verifies that status line contains "Unauthorized" message
     * And user verifies that meta.error_detail contains "Missing or invalid api credentials." message
     */
    @Test
    @DisplayName("Verify that user cannot access web service without valid API key")
    public void test1() {
        given().
                accept(ContentType.JSON).
                when().
                get("/countries").prettyPeek().
                then().
                assertThat().
                statusCode(401).
                statusLine(containsString("Unauthorized")).
                body("meta.error_detail", containsString("Missing or invalid api credentials."));
    }

    /**
     * Given accept content type as JSON
     * And query parameter api_key with valid API key
     * When user sends GET request to "/countries"
     * Then user verifies that status code is 200
     * And user verifies that status line contains "OK" message
     * And user verifies that countries array is not empty
     */
    @Test
    @DisplayName("Verify that can access with valid API key and countries array is not empty")
    public void test2() {
        given().
                accept(ContentType.JSON).
                queryParam("api_key", api_key).
                when().
                get("/countries").prettyPeek().
                then().
                assertThat().
                statusCode(200).
                statusLine(containsString("OK")).
                body("response.countries", not(empty()));
    }

    /**
     * Given accept content type as JSON
     * And query parameter api_key with valid API key
     * And query parameter country is "US"
     * And query parameter type is "national"
     * And query parameter year is 2019
     * When user sends GET request to "/holidays"
     * Then user verifies that status code is 200
     * And user verifies that number of national holidays is 11
     */
    @Test
    @DisplayName("Verify that number of national holidays is 11 in the US")
    public void test3() {

        Response response =
                given().
                        accept(ContentType.JSON).
                        queryParam("api_key", api_key).
                        queryParam("country", "US").
                        queryParam("type", "national").
                        queryParam("year", 2019).
                        when().
                        get("/holidays").prettyPeek();

        // shorter way, syntax sugar using hamcrest matcher library
        response.
                then().assertThat().statusCode(200).
                body("response.holidays", hasSize(11));

        // alternative regular way
        // List<Map<String, ?>> - list of objects, since there are nested objects, we cannot specify some value type
        List<Map<String, ?>> holidays = response.jsonPath().get("response.holidays");
        for (Map<String, ?> holiday : holidays) {
            System.out.println(holiday);
        }
        assertEquals(11, holidays.size(), "Wrong number of holidays");
    }

}
