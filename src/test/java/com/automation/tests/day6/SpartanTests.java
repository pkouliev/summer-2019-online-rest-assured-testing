package com.automation.tests.day6;

import com.automation.pojos.Job;
import com.automation.pojos.Location;
import com.automation.pojos.Spartan;
import com.automation.utilities.ConfigurationReader;
import com.github.javafaker.Faker;
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

public class SpartanTests {

    @BeforeAll
    public static void setup() {
        baseURI = ConfigurationReader.getValue("spartan.uri");
    }

    /**
     * given accept content type as JSON
     * when user send GET request to /spartans
     * then user verifies that status code is 200
     * and user verifies that content type is JSON
     */
    @Test
    @DisplayName("Verify that /spartans end-point returns 200 and content type as JSON")
    public void test1() {
        // web service may return different content type
        // and to request JSON, we can put in the given part ContentType.JSON
        // if we want to ask for XML, we can put ContentType.XML
        // but, if web service is configured for JSON
        // it will not give anything else
        // GET, PUT, POST, DELETE, etc. - HTTP verbs, or methods
        // GET - to get the data from web service
        // PUT - update existing record
        // DELETE - delete something, like delete spartan
        // PATCH - partial update of existing record
        given().accept(ContentType.JSON).
                when().get("/spartans").prettyPeek().
                then().assertThat().
                statusCode(200).
                contentType(ContentType.JSON);
    }

    /**
     * given accept content type as XML
     * when user send GET request to /spartans
     * then user verifies that status code is 200
     * and user verifies that content type is XML
     */
    @Test
    @DisplayName("Verify that /spartans end-point returns and content type as XML")
    public void test2() {
        // accept(ContentType.XML) <- you are asking for body format
        // contentType(ContentType.XML) <- you are verifying that body format is XML
        given().
                accept(ContentType.XML).
                when().
                get("/spartans").prettyPeek().
                then().
                assertThat().
                statusCode(200).
                contentType(ContentType.XML);

    }

    /**
     * Task: 3
     * given accept content type as JSON
     * when user send GET request to /spartans
     * then user saves payload in collection
     * <p>
     * #########################################
     * We can convert payload (JSON body for example) into collection.
     * if it's a single variable: name: "James", we can store in String or List<String>
     * if there are multiple names in the payload, we cannot use single String as a storage
     * instead, we use List<String>
     * if payload returns object:
     * {
     * "name": "James"
     * "age": 25
     * }
     * Then, we can store this object (on our, java side, as POJO or Map<String, ?}
     * If it's a POJO, we need to create corresponding POJO class in order to map properties
     * from json and java object:
     * Java class           JSON file
     * private String name  |    "name"
     * private int age      |    "age"
     * <p>
     * if we want to use different variable name in Java class, we use @SerializedName annotation
     * <p>
     * Java class           JSON file
     *
     * @SerializedName("name") private String firstName  |  "name"
     * private int age           |   "age"
     * <p>
     * otherwise, Gson, jackson, or any other Json parser will not be able to map properties correctly
     * Serialization - from POJO (java object) to stream bytes, let's say JSON
     * Deserialization - from stream of bytes, let's say from JSON into POJO (java object)
     * <p>
     * if payload returns array of objects:
     * [
     * {
     * "id": 338,
     * "name": "Dale",
     * "gender": "Male",
     * "phone": 5526566355
     * },
     * {
     * "id": 339,
     * "name": "Jackson",
     * "gender": "Male",
     * "phone": 7683699325
     * }
     * ]
     * <p>
     * Then we can store this payload as List<Map<?, ?>
     * or like list of POJO's List<Spartan>
     */
    @Test
    @DisplayName("Save payload into java collection")
    public void test3() {

        space();

        Response response = given().
                accept(ContentType.JSON).
                when().
                get("/spartans");

        JsonPath jsonPath = response.jsonPath();

        List<Object> collection = jsonPath.get();
        System.out.println(collection);

        space();

        for (Object map : collection) {
            System.out.println(map);
        }

        space();

        // alternative list with map
        List<Map<String, ?>> collection2 = jsonPath.get();
        System.out.println(collection2);

        space();

        for (Map<String, ?> map : collection2) {
            System.out.println(map);
        }

        space();

        // if we want to print only phone numbers
        for (Map<String, ?> map : collection2) {
            System.out.println(map.get("phone"));
        }
        space();
    }

    /**
     * given accept content type as JSON
     * when user send GET request to /spartans
     * then user saves payload into colection of Spartan
     */
    @Test
    @DisplayName("Save payload into java collection of Spartan")
    public void test4() {
        Response response = given().
                contentType(ContentType.JSON).
                when().
                get("/spartans");

        // whenever you see: Class object = response.jsonPath().getObject() | deserialization
        List<Spartan> spartans = response.jsonPath().getList("", Spartan.class);

        for (Spartan spartan : spartans) {
            System.out.println(spartan);
        }
    }

    /**
     * Task
     * given accept content type as JSON
     * when user sends POST request to /spartans
     * then user should be able to create new spartan
     * |gender|name           |phone         |
     * |male  |Mister Twister | 123-456-7890 |
     * then user verifies that status code is 201
     * <p>
     * 201 - means created. Whenever you POST something, you should get back 201 status code
     * in case of created record
     */
    @Test
    @DisplayName("create a new spartan and verify status code is 201")
    public void test5() {

        // builder pattern, one of design patterns in OOP
        // instead of having too many different constructors
        // we can use builder pattern and chain with (propertyName) methods to specify properties of an object
        Spartan spartan1 = new Spartan().
                withGender("Halo").
                withName("Some user").
                withPhone(1233215678);

        System.out.println(spartan1);
        Spartan spartan = new Spartan();
        spartan.setGender("Male"); // Male or Female
        spartan.setName("Mister Twister");
        spartan.setPhone(1234567890L); // at least 10 digits

        System.out.println(spartan);

        Response response = given().
                contentType(ContentType.JSON).
                body(spartan).
                post("/spartans");

        assertEquals(201, response.getStatusCode(), "Status code is wrong!");
        assertEquals("application/json", response.getContentType(), "Content type is invalid!");
        assertEquals(response.jsonPath().getString("success"), "A Spartan is Born!");
        response.prettyPrint();

        Spartan spartan_from_response = response.jsonPath().getObject("data", Spartan.class);

        System.out.println("Spartan id: " + spartan_from_response.getId());

//        // delete spartan that we just created
//        delete("/spartans/{id}", spartan_from_response.getId()).prettyPeek().
//                                      then().assertThat().statusCode(204);
    }

    @Test
    @DisplayName("Delete user")
    public void test6() {
        int id = 125;
        Response response = delete("/spartans/{id}", id);
        response.prettyPeek();
    }

    @Test
    @DisplayName("Delete half of the records")
    public void test7() {
        space();

        Response response = get("/spartans");

        // Collected all user id's
        List<Integer> userIDs = response.jsonPath().getList("id");

        // Sorted user id's in descending order
        userIDs.sort(Collections.reverseOrder());
        System.out.println("Before: " + userIDs);

        space();

        // iterated through half of the collection, and deleted half of the users
        // userIDs.size()/2 - represents half of
        for (int n = 0; n < userIDs.size() / 2; n++) {
            delete("/spartans/{id}", userIDs.get(n));
        }
    }

    @Test
    @DisplayName("Add 100 test users to Spartan app")
    public void test8() {
        Faker faker = new Faker();
        for (int i = 0; i < 10; i++) {
            Spartan spartan = new Spartan();
            spartan.setName(faker.name().firstName());
            // remove all non-digits
            // replaceAll() < takes regex (regular expression)
            // regex - it's a pattern, means that 1 character can represent group of chars/symbols.digits
            // \\D - everything that is not a digit (0-9)
            String phone = faker.phoneNumber().subscriberNumber(12).replaceAll("\\D", "");
//            phone.matches("\\d"); check if this string contain only digits
//            phone.matches("[a-x"); check if this string contains letters in the range from a to x
            // convert from String to Long
            spartan.setPhone(Long.parseLong(phone));
            spartan.setGender("Male");

            Response response = given().
                    contentType(ContentType.JSON).
                    body(spartan).
                    when().
                    post("/spartans");

            // whenever new spartan added, this message will print: "A Spartan is Born!"
            System.out.println(response.jsonPath().getString("success"));

            // verify that response status code is 201,
            // 201 means that post request executed
            assertEquals(201, response.getStatusCode());
        }
    }

    @Test
    @DisplayName("Get all spartan id's and print it as list")
    public void test9() {

        space();

        List<Integer> ids = get("/spartans").jsonPath().getList("id");
        System.out.println(ids);

        space();

        ids.sort(Collections.reverseOrder());
        System.out.println(ids);

        space();
    }

    @Test
    @DisplayName("Update spartan")
    public void test10() {
        Spartan spartan = new Spartan().
                withGender("Male").
                withName("Mister Twister").
                withPhone(1234567890L);

        Response response = given().
                accept(ContentType.JSON).
                contentType(ContentType.JSON).
                body(spartan).
                pathParam("id", 339).
                when().
                put("/spartans/{id}").prettyPeek();
        // put - update existing record
        // also when we PUT request, we need to specify entire body
        // post - create new one
        // whenever we POST/PUT id, it must be auto generated
        // if it's not like this - it's a bug

        // if contentType(ContentType.JSON) is in the given()
        // we tell the web service, what data we are sending
    }

    @Test
    @DisplayName("Update only name with PATCH")
    public void test11() {
        Map<String, String> update = new HashMap<>();
        update.put("name", "New SDET");
        Response response = given().
                accept(ContentType.JSON).
                contentType(ContentType.JSON).
                body(update).
                pathParam("id", 17).
                patch("/spartans/{id}");

        response.prettyPrint();
        // POST - add new spartan
        // PUT - update existing one, but we have to specify all properties
        // PATCH - update existing one, but we may specify one or more properties to update

    }
}



