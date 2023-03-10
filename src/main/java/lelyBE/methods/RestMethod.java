package lelyBE.methods;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.Assert;
import lelyBE.Util;

import java.util.List;

import static io.restassured.RestAssured.given;

public class RestMethod {
    Util util = new Util();
    String baseURI = util.getURL();
    String token = util.getToken();
    Response getResponse = null;
    Response postResponse = null;
    public void listUserId() {

        RestAssured.baseURI = baseURI;

        getResponse = given()
                .contentType("application/json; charset=UTF-8")
                .when()
                .get("/users")
                .then()
                .log().body()
                .statusCode(200)
                .extract().response();
    }

    public void checkUserDigit() {

        List<Object> idList = getResponse.jsonPath().getList("data.id");
        for (Object id : idList) {
            Assert.assertTrue(id.toString().length()>4);
        }
    }

    public void postUser(JSONObject userData, int expectedStatusCode) {

        RestAssured.baseURI = baseURI;

        postResponse = given()
                .contentType("application/json; charset=UTF-8")
                .header("Authorization", "Bearer " + token)
                .body(userData.toString())
                .when()
                .post("/users")
                .then()
                .log().body()
                .statusCode(expectedStatusCode)
                .extract().response();
    }

    public void checkCreatedUser(JSONObject userData) {

        Assert.assertEquals(userData.get("name"), postResponse.getBody().jsonPath().get("data.name"));
        Assert.assertEquals(userData.get("email"), postResponse.getBody().jsonPath().get("data.email"));
        Assert.assertEquals(userData.get("gender"), postResponse.getBody().jsonPath().get("data.gender"));
        Assert.assertEquals(userData.get("status"), postResponse.getBody().jsonPath().get("data.status"));
    }

    public void checkResponseBody(String expectedMessage) {
        Assert.assertEquals(expectedMessage, postResponse.getBody().jsonPath().get("data[0].message"));
    }

}
