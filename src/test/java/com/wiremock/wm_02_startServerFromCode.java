package com.wiremock;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;

public class wm_02_startServerFromCode {

    private static final String HOST = "localhost";
    private static final int PORT = 8080;
    private static WireMockServer server = new WireMockServer(PORT);

    @BeforeClass
    public void initializeServer() {
        System.out.println("Initializing server...");

        //Start Wiremock server via code and configure it with required host and port
        server.start();
        WireMock.configureFor(HOST, PORT);

        //Mock Response
        ResponseDefinitionBuilder mockResponse = new ResponseDefinitionBuilder();
        mockResponse.withStatus(200);
        mockResponse.withStatusMessage("OK");
        mockResponse.withHeader("Content-Type", "text/plain");
        mockResponse.withHeader("token", "1234567");
        mockResponse.withHeader("Set-Cookie", "session_id=91837492837");
        mockResponse.withHeader("Set-Cookie", "split_test_group=B");
        mockResponse.withBody("This is mock response");

        //Mock-Request: both format will work
        //  WireMock.stubFor(WireMock.get("/emps/1").willReturn(mockResponse));
        WireMock.givenThat(WireMock.get("/emps/1").willReturn(mockResponse));
    }
    @AfterClass
    public void shutdownServer() {
        if (server.isRunning() && server != null) {
            System.out.println("Shutting down server...");
            server.shutdown();
        }
    }

    @Test
    public void test_1(){

    String testApi = "http://" + HOST + ":" + PORT + "/emps/1";

        Response response = given()
                .get(testApi)
                .then()
                .statusCode(200)
                .extract().response();

        response.prettyPrint();
        response.headers().forEach(System.out::println);

        Assert.assertEquals(response.header("token"), "1234567");
        Assert.assertEquals(response.statusLine(), "HTTP/1.1 200 OK");
        Assert.assertEquals(response.getCookie("session_id"), "91837492837");
        Assert.assertEquals(response.getCookie("split_test_group"), "B");
        Assert.assertEquals(response.getBody().asString(), "This is mock response");


    }


}
