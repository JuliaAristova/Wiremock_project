package com.wiremock;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;

public class wm_03_startServerFromCode_mockFile {

    private static final String END_POINT = "/readfromfile/index";
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
        //src/test/resources/__files/json/index.json
        mockResponse.withBodyFile("json/index.json");

       //Mocking
       WireMock.stubFor(WireMock.get(END_POINT).willReturn(mockResponse));
    }

    @AfterClass
    public void shutdownServer() {
        if (server.isRunning() && server != null) {
            System.out.println("Shutting down server...");
            server.shutdown();
        }
    }

    @Test
    public void test_1() {

        String testApi = "http://localhost:"+PORT+END_POINT;
        System.out.println("Service to be hit: " + testApi);

        Response response = given()
                .get(testApi)
                .then()
                .statusCode(200)
                .extract().response();

        Assert.assertEquals(response.jsonPath().get("glossary.title"),"Lord of the Ring");
        Assert.assertEquals(response.jsonPath()
                .get("glossary.GlossaryDiv.GlossList.GlossEntry.GlossDef.GlossSeeAlso[0]"), "GML");
    }
}