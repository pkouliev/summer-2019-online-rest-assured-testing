package com.automation.tests.day2;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MetaWeatherTests {

    /**
     * /api/location/search/?query=san
     * /api/location/search/?query=london
     * /api/location/search/?lattlong=36.96,-122.02
     * /api/location/search/?lattlong=50.068,-5.316
     * /api/location/{woeid}/
     */

    private String baseURI = "https://www.metaweather.com/api/";

    @Test
    public void test1() {
        Response response = given()
                .baseUri(baseURI)
                .basePath("location/search/")
                .queryParam("query", "washington")
                .get();

        assertEquals(200, response.getStatusCode());
        System.out.println(response.prettyPrint());
    }

    // /users/100/ - 100 it's a path parameter
    // /users/255/ - 155 it's a path parameter -> fetching specific resource, like: some city info, user
    // /users/255?name=James name - query parameter key=value, key it's a query parameter -> stands for filtering purpose:
    // out of all users, we provide specific one like name or user's degree, etc....
    // "woeid": 2514815, this whoeid stands for Where On Earth ID, based on this value we can get weather info info in specific place
    @Test
    public void test2() {
        Response response = given()
                .baseUri(baseURI)
                .basePath("location/{woeid}/")
                .pathParam("woeid", "2514815")
                .get();

        System.out.println(response.prettyPrint());
    }


}
