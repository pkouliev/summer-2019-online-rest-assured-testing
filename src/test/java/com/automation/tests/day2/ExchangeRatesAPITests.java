package com.automation.tests.day2;

import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExchangeRatesAPITests {

    private String baseURI = "http://api.openrates.io/";

    @Test
    public void test1() {
        Response response = given().baseUri(baseURI + "latest").get();

        // verify status code
        assertEquals(200, response.getStatusCode());
        System.out.println(response.prettyPrint());
    }


    @Test
    @DisplayName("Verify Header Content Type is json")
    public void test2() {
        Response response = given().get(baseURI + "latest");
        assertEquals(200, response.getStatusCode());
        // verify that content type is json in header
        String actualHeaderContentType = response.getHeader("Content-Type");
        assertEquals("application/json", actualHeaderContentType);
        System.out.println("Actual Header Content Type: " + actualHeaderContentType);
        // or like this
        assertEquals("application/json", response.getContentType());
    }

    // GET https://api.exchangeratesapi.io/latest?base=USD HTTP/1.1
    // base it's a query parameter that will ask web service to change currency from eu to something else
    @Test
    public void test3() {
        // task: get currency exchange rate for dollar. By default it's euro.
        //Response response = given().get(baseURI + "latest?base=USD");
        Response response = given().
                baseUri(baseURI).
                basePath("latest").
                queryParam("base", "USD").
                get();
        assertEquals(200, response.getStatusCode());
        System.out.println(response.prettyPrint());
    }

    // #Task: verify that response body, for latest currency rates, contains today's date (yyyy-MM-dd)
    @Test
    public void test4() {
        Response response = given().
                baseUri(baseURI).
                basePath("latest").
                queryParam("base", "GBP").
                get();
        assertEquals(200, response.getStatusCode());
        System.out.println(response.prettyPrint());

        String todaysDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        System.out.println("Today's date: " + todaysDate);
        assertTrue(response.getBody().asString().contains(todaysDate));
    }

    //let's get currency exchange rate for year 2000
    // GET https://api.exchangeratesapi.io/history?start_at=2018-01-01&end_at=2018-09-01&symbols=ILS,JPY
    @Test
    public void test5() {
        Response response = given().
                baseUri(baseURI).
                basePath("history").
                queryParam("start_at", "2000-01-01").
                queryParam("end_at", "2000-12-31").
                queryParam("base", "USD").
                queryParam("symbols", "CAD", "JPY", "EUR", "GBP").
                get().prettyPeek();

        // System.out.println(response.prettyPrint());

    }

    /**
     * Given request parameter "base" is "USD"
     * When user sends request to "api.openrates.io"
     * Then response code should be 200
     * And response body must contain "base": "USD"
     */
    @Test
    public void test6() {
        Response response = given().
                baseUri(baseURI).
                basePath("latest").
                queryParam("base", "USD").
                get();

        System.out.println(response.prettyPrint());
        assertEquals(200, response.getStatusCode());

        String body = response.getBody().asString();
        assertTrue(body.contains("\"base\": \"USD\""));
    }
}
