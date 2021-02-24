package com.automation.utilities;

import com.automation.pojos.Spartan;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.automation.utilities.utils.space;
import static io.restassured.RestAssured.*;

public class APIUtilities {
    private static String URI = ConfigurationReader.getValue("spartan.uri");

    /**
     * This method can POST new spartan
     *
     * @param spartan POJO
     * @return response object
     */
    public static Response postSpartan(Spartan spartan) {
        return given().
                contentType(ContentType.JSON).
                basePath(URI).
                body(spartan).
                post("/spartans");
    }

    /**
     * This method can POST new spartan
     *
     * @param spartan map
     * @return response object
     */
    public static Response postSpartan(Map<String, ?> spartan) {
        RestAssured.baseURI = ConfigurationReader.getValue("spartan.uri");
        Response response = given().
                contentType(ContentType.JSON).
                body(spartan).
                post("/spartans");
        return response;
    }

    /**
     * This method can POST new spartan
     *
     * @param filePath to the Spartan external JSON file
     * @return response object
     */
    public static Response postSpartan(String filePath) {
        RestAssured.baseURI = ConfigurationReader.getValue("spartan.uri");
        File file = new File(filePath);
        RestAssured.baseURI = ConfigurationReader.getValue("spartan.uri");
        Response response = given().
                contentType(ContentType.JSON).
                body(file).
                post("/spartans");
        return response;
    }

    /**
     * Method to delete spartan
     *
     * @param id of spartan that we would like to delete
     * @return response object that you can assert later
     */
    public static Response deleteSpartanById(int id) {
        RestAssured.baseURI = ConfigurationReader.getValue("spartan.uri");
        Response response = RestAssured.delete("/spartans/{id}", id);
        return response;
    }

    /**
     * Delete all spartans
     */
    public static void deleteAllSpartans() {
        Response response =
                given().basePath(URI).accept(ContentType.JSON).
                        when().get("/spartans");

        List<Integer> userIDs = response.jsonPath().getList("id");

        for (int n = 0; n < userIDs.size(); n++) {
            when().delete("/spartans/{id}", userIDs.get(n)).
                    then().assertThat().statusCode(204);
            System.out.println("Deleted spartan with id: " + userIDs.get(n));
        }
    }
}
