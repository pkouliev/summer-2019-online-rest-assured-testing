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

public class OMDBTestsAPIKey {

    String api_key = ConfigurationReader.getValue("omdb.api.key");

    @BeforeAll
    public static void setup() {
        baseURI = ConfigurationReader.getValue("omdb.uri");
    }

    @Test
    @DisplayName("Verify that unauthorized user cannot get info about movies without OMDB api")
    public void test1() {
        given().
                accept(ContentType.JSON).
                queryParam("t", "Home Alone").
                when().
                get().prettyPeek().
                then().assertThat().statusCode(401).
                statusLine(containsString("Unauthorized")).
                body("Error", is("No API key provided."));
        // 401 Unauthorized - you are not allowed to access this web service

    }

    @Test
    @DisplayName("Verify that Macaulay Culkin appears in actors list for Home Alone movie")
    public void test2() {
        space();

        Response response = given().
                accept(ContentType.JSON).
                queryParam("apikey", api_key).
                queryParam("t", "Home Alone").
                when().
                get().prettyPeek();

        response.then().assertThat().
                statusCode(200).
                body("Actors", containsString("Macaulay Culkin"));

        Map<String, Object> payload = response.getBody().as(Map.class);

        System.out.println(payload);

        space();

        // entry - key=value pair
        // how to iterate a map
        for (Map.Entry<String, Object> entry : payload.entrySet()) {
            System.out.println("Key: " + entry.getKey() + ", value: " + entry.getValue());
        }

        space();
    }


}
