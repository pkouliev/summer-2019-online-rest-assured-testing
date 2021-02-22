package com.automation.tests.day4;

import com.automation.utilities.ConfigurationReader;
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

public class MetaWeatherJsonPathTests {

    @BeforeAll
    public static void setup() {
        baseURI = ConfigurationReader.getValue("meta.weather.uri");
    }

    /**
     * TASK 1:
     * Given accept type is JSON
     * When users sends a GET request to "/search"
     * And query parameter is 'New'
     * Then user verifies that payload contains 5 objects
     */
    @Test
    @DisplayName("user verifies that payload contains 5 objects that match with 'New'")
    public void test1() {
        List<Object> objects = given().
                accept(ContentType.JSON).
                queryParam("query", "New").
                when().
                get("/search").
                thenReturn().jsonPath().get();

        assertEquals(objects.size(), 5);
        System.out.println(objects.size());

        given().
                accept(ContentType.JSON).
                queryParam("query", "New").
                when().
                get("/search").
                then().
                assertThat().
                statusCode(200).
                body("", hasSize(5)).
                log().body(true);

    }

    /**
     * TASK 2:
     * Given accept type is JSON
     * When users sends a GET request to "/search"
     * And query parameter is New
     * Then user verifies that 1st object has following info:
     * |title   |location_type|woeid  |latt_long          |
     * |New York|City         |2459115|40.71455,-74.007118|
     */
    @Test
    @DisplayName("verify that 1st object's info")
    public void test2() {

        given().
                accept(ContentType.JSON).
                queryParam("query", "New").
                when().
                get("/search").
                then().
                assertThat().
                statusCode(200).
                body("title[0]", is("New York")).
                body("location_type[0]", is("City")).
                body("woeid[0]", is(2459115)).
                body("latt_long[0]", is("40.71455,-74.007118")).
                log().body(true);
    }

    @Test
    @DisplayName("verify that 1st object's info")
    public void test2_2() {

        space();

        Map<String, String> expected = new LinkedHashMap<>();
        expected.put("title", "New York");
        expected.put("location_type", "City");
        expected.put("woeid", "2459115");
        expected.put("latt_long", "40.71455,-74.007118");

        Response response = given().
                accept(ContentType.JSON).
                queryParam("query", "New").
                when().
                get("/search");

        JsonPath jsonPath = response.jsonPath();
        // String.class, String.class will force jsonpath to return map with String as key and value
        assertEquals(expected, jsonPath.getMap("[0]", String.class, String.class));
        // for first title. title[0], but for first object, we can just say [0]
        // if one object is key=value pair like map, collection of these objects can be represented as a list of map
        List<Map<String, ?>> objectValues = jsonPath.get();
        System.out.println(objectValues);

        space();

        for (Map<String, ?> values : objectValues) {
            System.out.println(values);
        }

        space();

        given().
                accept(ContentType.JSON).
                queryParam("query", "New").
                when().
                get("/search").
                then().
                assertThat().
                statusCode(200).
                body("title[0]", is("New York")).
                body("location_type[0]", is("City")).
                body("woeid[0]", is(2459115)).
                body("latt_long[0]", is("40.71455,-74.007118")).
                log().body(true);
    }

    /**
     * TASK 3:
     * Given accept type is JSON
     * When users sends a GET request to "/search"
     * And query parameter is 'Las'
     * Then user verifies that payload  contains following titles:
     * | Glasgow  |
     * | Dallas |
     * | Las Vegas|
     */
    @Test
    @DisplayName("verify titles")
    public void test3() {

        given().
                accept(ContentType.JSON).
                queryParam("query", "Las").
                when().
                get("/search").
                then().
                assertThat().
                statusCode(200).
                body("title", contains("Glasgow", "Dallas", "Las Vegas")). // contains - partial match
                /* or */
                        body("title", hasItems("Glasgow", "Dallas", "Las Vegas")). // hasItems - exact match
                log().body(true);
    }

    /**
     * TASK 4:
     * Given accept type is JSON
     * When users sends a GET request to "/search"
     * And query parameter is 'Las'
     * hen verify that every item in payload has location_type City
     */
    @Test
    @DisplayName("verify that every item in payload has location_type City ")
    public void test4() {
        given().
                accept(ContentType.JSON).
                queryParam("query", "Las").
                when().
                get("/search").
                then().
                assertThat().
                statusCode(200).
                body("location_type", everyItem(is("City"))).
                log().body(true);
    }

    /**
     * TASK 5:
     * Given accept type is JSON
     * When users sends a GET request to "/location/{woeid}"
     * And path parameter is '44418'
     * Then verify following that payload contains weather forecast sources titles
     * |BBC                 |
     * |Forecast.io         |
     * |HAMweather          |
     * |Met Office          |
     * |OpenWeatherMap      |
     * |Weather Underground |
     * |World Weather Online|
     */
    @Test
    @DisplayName("verify that payload contains weather forecast sources as titles")
    public void test5() {
        given().
                accept(ContentType.JSON).
                pathParam("woeid", 44418).
                when().
                get("/location/{woeid}").
                then().
                assertThat().
                statusCode(200).
                body("sources.title", contains("BBC", "Forecast.io", "HAMweather", "Met Office", "OpenWeatherMap",
                        "Weather Underground", "World Weather Online"));

        // alternative - without hamcrest
        List<String> expected = List.of("BBC", "Forecast.io", "HAMweather", "Met Office", "OpenWeatherMap",
                "Weather Underground", "World Weather Online");

        Response response = given().
                accept(ContentType.JSON).
                pathParam("woeid", 44418).
                when().
                get("/location/{woeid}").prettyPeek();

        List<String> actual = response.jsonPath().getList("sources.title");

        assertEquals(expected, actual);
    }


}
