package com.automation.tests.day7;

import com.automation.utilities.ConfigurationReader;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static com.automation.utilities.utils.*;
import static org.junit.jupiter.api.Assertions.*;

public class SpartanTestsDay7 {

    @BeforeAll
    public static void setup() {
        baseURI = ConfigurationReader.getValue("spartan.uri");
    }

    // add new Spartan from the external JSON file
    @Test
    @DisplayName("Add new user by using external JSON file")
    public void test1() {
        File file = new File(System.getProperty("user.dir") + "/spartan.json");
        given().
                contentType(ContentType.JSON).
                accept(ContentType.JSON).
                body(file).
                when().
                post("/spartans").prettyPeek().
                then().
                assertThat().
                statusCode(201).
                body("success", is("A Spartan is Born!"));

    }

    @Test
    @DisplayName("Add new user by using map")
    public void test2() {
        Map<String, Object> spartan = new HashMap<>();
        spartan.put("phone", 3025678970L);
        spartan.put("gender", "Male");
        spartan.put("name", "Johnny Depp");

        // we must specify content type, whenever we POST
        // contentType(ContentType.JSON)
        given().
                contentType(ContentType.JSON).
                accept(ContentType.JSON).
                body(spartan).
                when().
                post("/spartans").prettyPeek().
                then().assertThat().
                statusCode(201).
                body("success", is("A Spartan is Born!")).
                body("data.name", is("Johnny Depp")).
                body("data.gender", is("Male"));
        // in the response, we have spartan object inside data variable
        // to get properties we need to specify name of that object data
        // put "."-dot and parameter that we want to read
        // data.id , data.gender , data.name
        // success - property, string variable
        // data - object that represents spartan
    }

    @Test
    @DisplayName("update spartan, only name PATCH")
    public void test3() {
        Map<String, Object> update = new HashMap<>();
        update.put("name", "Demi Moore");
        update.put("gender", "Female");

        given().
                contentType(ContentType.JSON).
                body(update).
                pathParam("id", 338).
                when().
                patch("/spartans/{id}").prettyPeek().
                then().assertThat().
                statusCode(204);
        // since response doesn't contain body, after PATCH request,
        // we don't need accept(ContentType.JSON)
        // PUT - all parameters
        // PATCH - 1 + parameters
    }

}
