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

public class BearerTokenTestsWithBookit {

    @BeforeAll
    public static void before() {

        baseURI = ConfigurationReader.getValue("bookit.qa1");
    }


    /**
     * Method that generates access token
     *
     * @return bearer token
     */
    public String getToken() {
        // https://cybertek-reservation-api-qa.herokuapp.com/sign?email={value}&password={value}
        Response response = given().
                queryParam("email", ConfigurationReader.getValue("team.leader.email")).
                queryParam("password", ConfigurationReader.getValue("team.leader.password")).
                when().
                get("/sign").prettyPeek();

        return response.jsonPath().getString("accessToken");
    }

    // let's get the list of all rooms and verify that status code is 200
    // /api/rooms
    @Test
    @DisplayName("Get list of rooms")
    public void test1() {
        Response response = given().
                header("Authorization", getToken()).
                when().
                get("/api/rooms").prettyPeek();
    }
}
