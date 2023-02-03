package Jira;

import io.restassured.RestAssured;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;

public class JiraTest {
    public static void main(String[] args) throws IOException {

        RestAssured.baseURI="http://localhost:8080";

        //add comment request
        given().pathParam("key", "10100").log().all()
                .header("Authorization","Bearer OTk4MTIwNTM0MTU0OjDu6kwD2I0n1bicjTgM1JQuuE/B").header("Content-Type", "application/json")
                .body(Files.readAllBytes(Paths.get("src/main/resources/mocks/addComment.json")))
                .when().post("/rest/api/2/issue/{key}/comment")
                .then().log().all().assertThat().statusCode(201);

        //add attachment
        given().header("X-Atlassian-Token", "no-check")
                .header("Authorization","Bearer OTk4MTIwNTM0MTU0OjDu6kwD2I0n1bicjTgM1JQuuE/B").header("Content-Type", "multipart/form-data")
                .pathParam("key", "10100").multiPart("file", new File("src/main/resources/Files/jira.txt"))
                .when().post("/rest/api/2/issue/{key}/attachments")
                .then().log().all().assertThat().statusCode(200);

    }
}
