package com.wiremock;/*
1. Start wiremock server: java -jar wiremock-standalone-3.13.1.jar
2. Add req response mapping: C:\Users\julia\Documents\API\mapping
3. Run the test
 */


import org.testng.Assert;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;

public class wm_01_Basic_standalone {

    @Test
    public void test_1() {
                given()
                .get("http://localhost:8080/users/1")
                .then()
                .assertThat()
                .statusCode(200);
    }

    @Test
    public void test_2() {
                given()
                .get("http://localhost:8080/users/2")
                .then()
                .assertThat()
                .statusCode(201);
    }

    @Test
    public void test_3() {

       String contentType =
                given()
                .get("http://localhost:8080/users/3")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .header("Content-Type");

       System.out.println("Test response header Content-Type: " + contentType);

       Assert.assertEquals(contentType, "text/plain");
    }
}
