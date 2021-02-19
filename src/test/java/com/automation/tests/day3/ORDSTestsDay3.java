package com.automation.tests.day3;

import com.automation.utilities.ConfigurationReader;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static com.automation.utilities.utils.*;

public class ORDSTestsDay3 {

    @BeforeAll
    public static void setup() {
        baseURI = ConfigurationReader.getValue("ords.uri");
    }

    // accept("application/json") shortcut for header("Accept", "application/json")
    // we are asking for json as a response
    @Test
    public void test1() {
        given().
                accept("application/json").
                get("/employees").
                then().
                assertThat().statusCode(200).
                and().
                assertThat().contentType("application/json").
                log().all(true);
    }

    // path parameter - to point on specific resource /employee/:id it's a path parameter
    // query parameter - to filter results, or describe new resource:
    // POST /users?name=James&age=60&job-title=SDET
    // or to filter GET /employee?name=Jamal get all employees with name Jamal
    @Test
    public void test2() {
        given().
                accept("application/json").
                pathParam("id", 100).
                when(). // when() --> optional to look readable and fluent - logical syntax connector
                get("/employees/{id}").
                then().
                assertThat().statusCode(200).
                and(). // and() --> optional to look readable and fluent - logical syntax connector
                assertThat().body("employee_id", is(100),
                "department_id", is(90),
                "last_name", is("King")).
                // is - coming from import static org.hamcrest.Matchers.*
                        log().all(); // all = header + body + status code
        // body ("phone_number") --> 515.123.4567
    }

    /**
     * given path parameter is "/regions/{id}"
     * when user makes get request
     * and region id equals to 1
     * then assert that status code is 200
     * and assert that region name is Europe
     */
    @Test
    public void test3() {
        given().
                accept("application/json").
                pathParam("id", 1).
                when().
                get("/regions/{id}").
                then().
                assertThat().statusCode(200).
                assertThat().body("region_name", is("Europe")).
                time(lessThan(10L), TimeUnit.SECONDS). // verify that response is less than 10 seconds
                log().body(); // log body in pretty format
    }

    @Test
    public void test4() {

        space();

        JsonPath json = given().
                accept("application/json").
                when().
                get("/employees").
                thenReturn().jsonPath();
        // items[employee1, employee2, employee3] | items[0] = employee1

        String firstNameOfFirstEmployee = json.getString("items[0].first_name");
        String firstNameOfLastEmployee = json.getString("items[-1].first_name");

        System.out.println("First employee first name: " + firstNameOfFirstEmployee);
        System.out.println("Last employee first name: " + firstNameOfLastEmployee);

        space();

        // in JSON, employee looks like object that consists of params and their values
        // we can parse that json and store in the map
        Map<String, ?> firstEmployee = json.get("items[0]");
        // we put ? because it can be also not String
        System.out.println(firstEmployee);

        space();

        // since firstEmployee - it's a map (key-value pair,
        // we can iterate through it by using Entry, entry represent one key=value pair
        // put ? as value (Map<String, ?>),
        // because there are values of different data type: String, integer, etc.
        // if you put String as value, you might get some casting exception
        // that cannot convert from integer (or something else) to String
        for (Map.Entry<String, ?> entry : firstEmployee.entrySet()) {
            System.out.println("key: " + entry.getKey() + ", value: " + entry.getValue());
        }

        space();

        // get and print all last names
        // items - it's an object.
        // Whenever you need to read some property from the object, you put object.property
        // but, if response has multiple objects, we can get property from every object
        List<String> lastNames = json.get("items.last_name");
        System.out.println("Last Names: " + lastNames);

        space();

        int count = 1;
        for (String lastName : lastNames) {
            System.out.println("Last name of Employee " + count + ": " + lastName);
            count++;
        }

        space();
    }

    // get info from countries as List<Map<String, ?>>
    // prettyPrint() - print json/xml/html in nice format and returns string,
    // thus we cannot retrieve jsonPath without extraction...
    // prettyPeek() - does same job, but return Response object,
    // and from that object we can get json path.
    @Test
    public void test5() {

        space();

        JsonPath json = given().
                accept("application/json").
                //when().
                        get("/countries").
                        prettyPeek().jsonPath();
        // exclude .prettyPeek() and you will not see detailed info about response

        List<Map<String, ?>> allCountries = json.get("items");
        System.out.println(allCountries);

        space();

        // when we read data from json response, values are not only strings
        // so if we are not sure that all values will have same data type we can put ?
        int count = 1;
        for (Map<String, ?> country : allCountries) {
            System.out.println("Country " + count + ": " + country + "\n");
            count++;
        }

        space();

    }

    // get collection of employee's salaries
    // then sort it
    // and print
    @Test
    public void test6() {
        space();

        JsonPath json = given().
                accept("application/json").
                get("/employees").
                jsonPath();

        List<Integer> salaries = given().
                accept("application/json").
                get("/employees").
                jsonPath().get("items.salary");

        Collections.sort(salaries);
        Collections.reverse(salaries);
        System.out.println("Employees' salaries sorted: " + salaries);

        space();

        List<Integer> salaries2 = json.get("items.salary");
        System.out.println("Employees' salaries: " + salaries2);

        space();

        int count = 1;
        for (Integer salary : salaries) {
            System.out.println("Employee " + count + ": $" + salary);
            count++;
        }

        space();

    }

    // get collection of phone numbers from employees
    // and replace all dots "." in every phone number with dash "-"
    @Test
    public void test7() {
        space();
        List<String> phoneNumbers = given().
                accept("application/json").
                get("/employees").
                jsonPath().get("items.phone_number");

        System.out.println(phoneNumbers);

        space();

        // Replaces each element of this list with the result of applying the operator to that element
        // replace "." with "-" in every value
        phoneNumbers.replaceAll(p -> p.replace(".", "-"));
        System.out.println(phoneNumbers);

        space();

        for (String phoneNumber : phoneNumbers) {
            System.out.println(phoneNumber.replace(".", "-"));
        }

        space();

        List<Object> phoneNumbers2 = given().
                accept("application/json").
                get("/employees").
                jsonPath().get("items.phone_number"); // it calls J path (Groovy), like Xpath(XML path)

        phoneNumbers2.replaceAll(p -> p.toString().replace(".", "-"));
        System.out.println(phoneNumbers2);

        space();
    }

    /**
     * Given accept type as JSON
     * And path parameter is id with value 1700
     * When user sends get request to /locations
     * Then user verifies that status code is 200
     * And user verifies following json path information:
     * | location_id | postal_code | city    | state_province |
     * | 1700        | 98199       | Seattle | Washington     |
     * And user verifies that location_id is 1700
     * And user verifies that postal_code is 98199
     * And user verifies that city is Seattle
     * And user verifies that state_province is Washington
     */
    @Test
    public void test8() {

        space();

        given().
                accept(ContentType.JSON).
                pathParam("id", 1700).
                get("/locations/{id}").
                then().
                assertThat().statusCode(200).
                assertThat().body("location_id", is(1700),
                "postal_code", is("98199"),
                "city", is("Seattle"),
                "state_province", is("Washington")).
                log().all();

        space();


        Response response = given().
                accept(ContentType.JSON).
                pathParam("id", 1700).
                get("/locations/{id}");

        response.
                then().assertThat().statusCode(200).
                body("location_id", is(1700),
                        "postal_code", is("98199"),
                        "city", is("Seattle"),
                        "state_province", is("Washington")).
                log().body();

        space();


    }
}
