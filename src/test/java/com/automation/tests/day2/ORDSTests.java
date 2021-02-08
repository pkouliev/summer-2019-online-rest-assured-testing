package com.automation.tests.day2;

import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;

public class ORDSTests {

    // address of ORDS web service, that is running on AWS ec2
    // data is coming from SQL Oracle data base to this web service
    // during back-end testing with SQL developer and JDBC API
    // we were accessing data base directly
    // now, we gonna access web service
    // according to OOP convention, all instance variables should be private
    // but, if we will make it public, it will not make any difference for us
    // it's just good practice, so later we will not hesitate which keyword to use when it's gonna be important.
    private String baseURI = "http://ec2-3-84-218-226.compute-1.amazonaws.com:1000/ords/hr";


    // we start from given()
    // then we can sepcify type of request like: get(), put(), delete(), post()
    // and as parameter, we enter resource location (URI)
    // then we are getting response back that response we can put into Response object
    // from response object, we can retrieve: body, header, status code
    // it works without RestAssured.given() because of static import
    // verify that status code is 200
    @Test
    public void test1() {
        Response response = given().get(baseURI + "/employees");
        // System.out.println(response.getBody().asString());
        assertEquals(200, response.getStatusCode());
        System.out.println(response.prettyPrint());
    }

    // get employee id 100 and verify that status code is 200
    // also, print body
    @Test
    public void test2() {
        // header stands for meta data
        // usually it's used to include cookies
        // in this example, we are specifying what kind of response we need
        // because web service can return let's say json or xml
        // when we put header info "Accept", "application/json", we are saying that we need only json as response
        Response response = given().
                header("Accept", "application/json").
                get(baseURI + "/employees/100");
        int actualStatusCode = response.statusCode();
        assertEquals(200, actualStatusCode);
        System.out.println(response.prettyPrint());

        // get information about response content type, you can retrieve from response object
        System.out.println("What kind of content server sends to you, in this response: " +
                            response.getHeader("Content-Type"));
    }

    // Task: perform GET request to /regions, print body and all headers.
    @Test
    public void test3() {
        Response response = given().get(baseURI + "/regions");
        int actualStatusCode = response.statusCode();
        assertEquals(200, actualStatusCode);
        System.out.println(response.prettyPrint());
        System.out.println(response.getHeaders());

        System.out.println("###############################################################");

        // to get specific header
        Header header = response.getHeaders().get("Content-type");
        System.out.println(header);

        System.out.println("###############################################################");

        // print all headers one by one
        Headers headers = response.getHeaders();
        for (Header h : headers) {
            System.out.println(h);
        }

        System.out.println("###############################################################");


    }
}
