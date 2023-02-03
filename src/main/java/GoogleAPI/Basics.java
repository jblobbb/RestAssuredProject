package GoogleAPI;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class Basics {
    public static void main(String[] args) throws IOException {

        RestAssured.baseURI = "https://rahulshettyacademy.com";
        RestAssured.useRelaxedHTTPSValidation();

        //Validate if add place api is working

        //given - all input details
        //when - submit api - resource, http method
        //then - validate the response
        //content of the file to string -> content of file can convert into byte -> convert byte data to string

        String response = given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
                .body(new String(Files.readAllBytes(Paths.get("src/main/resources/mocks/addPlace.json"))))
                .when().post("/maps/api/place/add/json")
                .then().assertThat().statusCode(200).body("scope", equalTo("APP")).header("Server", "Apache/2.4.41 (Ubuntu)").extract().response().asString();

        System.out.println(response);

        JsonPath js = new JsonPath(response); //for parsing Json
        String placeId = js.getString("place_id");
        System.out.println(placeId);

        //update place
        String newAddress = "70 Summer walk,Africa";

        given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json").body("{\n" +
                        "\"place_id\":\""+placeId+"\",\n" +
                        "\"address\":\""+newAddress+"\",\n" +
                        "\"key\":\"qaclick123\"\n" +
                        "}")
                .when().put("/maps/api/place/update/json")
                .then().assertThat().log().all().statusCode(200).body("msg", equalTo("Address successfully updated"));

        //get place
        String getPlaceResponse = given().log().all().queryParam("place_id", placeId).queryParam("key", "qaclick123").header("Content-Type", "application/json")
                .when().get("/maps/api/place/get/json")
                .then().assertThat().log().all().statusCode(200).extract().response().asString();

        JsonPath js1 = ReusableMethods.rawToJson(getPlaceResponse);
        String actualAddress = js1.getString("address");
        System.out.println(actualAddress);
        //testng assertion
        Assert.assertEquals(actualAddress, newAddress);
        //Cucumber Junit, Testng


    }

}
