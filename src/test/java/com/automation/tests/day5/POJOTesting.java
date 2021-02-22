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

import java.util.*;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static com.automation.utilities.utils.*;
import static org.junit.jupiter.api.Assertions.*;

public class POJOTesting {

    @BeforeAll
    public static void setup() {
        baseURI = ConfigurationReader.getValue("ords.uri");
    }

    @Test
    @DisplayName("Get job info from JSON and convert it into POJO")
    public void test1() {
        Response response = given().
                accept(ContentType.JSON).
                when().
                get("/jobs");

        JsonPath jsonPath = response.jsonPath();
        // this is deserialization from JSON to POJO (java object)
        Job job = jsonPath.getObject("items[0]", Job.class);
        // Job.class is type of POJO that we are gonna create from JSON

        System.out.println(job);

        System.out.println("Job id: " + job.getJobId());
    }

    @Test
    @DisplayName("Convert from POJO to JSON")
    public void test2() {

        space();

        Job sdet = new Job("SDET", "Software Development Engineer in Test", 5000, 20000);

        System.out.println("From toString(): " + sdet);

        space();

        Gson gson = new Gson();
        String json = gson.toJson(sdet); // convert POJO to JSON: serialization
        System.out.println("JSON file: " + json);

        space();
    }

    @Test
    @DisplayName("Convert JSON into collection of POJO's")
    public void test3() {

        space();

        Response response = given().
                accept(ContentType.JSON).
                when().
                get("/jobs");

        JsonPath jsonPath = response.jsonPath();

        List<Job> jobs = jsonPath.getList("items", Job.class);

        for (Job job : jobs) {
            System.out.println(job);
        }

        space();

        for (Job job : jobs) {
            System.out.println(job.getJobTitle());
        }

        space();

    }

    @Test
    @DisplayName("covert JSON locations object into your custom location POJO")
    public void test4() {

        space();

        Response response = given().
                accept(ContentType.JSON).
                when().
                get("/locations/{location_id}", 2500);

        JsonPath jsonPath = response.jsonPath();

        Location location = jsonPath.getObject("", Location.class);

        System.out.println(location);

        space();
    }

    @Test
    @DisplayName("convert JSON payload of locations into your custom collection of Location class. List<Location>")
    public void test5() {

        space();

        Response response = given().
                accept(ContentType.JSON).
                when().
                get("/locations");

        JsonPath jsonPath = response.jsonPath();

        List<Location> locations = jsonPath.getList("items", Location.class);

        for (Location location : locations) {
            System.out.println(location);
        }

        space();

    }


}
