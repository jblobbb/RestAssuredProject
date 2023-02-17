package Jira;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;

public class JiraTest {
    public static void main(String[] args) throws IOException {

        RestAssured.baseURI="http://localhost:8080";
        String expectedMessage = "This is the expected message";

        //add comment request
        String addCommentResponse = given().pathParam("key", "10100").log().all()
                                            .header("Authorization","Bearer OTk4MTIwNTM0MTU0OjDu6kwD2I0n1bicjTgM1JQuuE/B").header("Content-Type", "application/json")
                                            .body(Files.readAllBytes(Paths.get("src/main/resources/mocks/addComment.json")))
                                            .when().post("/rest/api/2/issue/{key}/comment")
                                            .then().log().all().assertThat().statusCode(201).extract().response().asString();
        JsonPath js = new JsonPath(addCommentResponse);
        String commentID = js.getString("id");

        //add attachment
        given().header("X-Atlassian-Token", "no-check")
                .header("Authorization","Bearer OTk4MTIwNTM0MTU0OjDu6kwD2I0n1bicjTgM1JQuuE/B").header("Content-Type", "multipart/form-data")
                .pathParam("key", "10100").multiPart("file", new File("src/main/resources/Files/jira.txt"))
                .when().post("/rest/api/2/issue/{key}/attachments")
                .then().log().all().assertThat().statusCode(200);

        //get issues
        String issueDetails = given().pathParam("key", "10100").queryParam("fields", "comment").log().all().header("Authorization","Bearer OTk4MTIwNTM0MTU0OjDu6kwD2I0n1bicjTgM1JQuuE/B")
                                    .when().get("/rest/api/2/issue/{key}")
                                    .then().log().all().extract().response().asString();

        System.out.println(issueDetails);

        JsonPath js1 = new JsonPath(issueDetails);
        int commentsCount = js1.getInt("fields.comment.comments.size()");
        for (int i = 0; i < commentsCount; i++) {
            String commentIdIssue = js1.get("fields.comment.comments["+i+"].id").toString();
            if (commentIdIssue.equalsIgnoreCase(commentID)) {
               String message = js1.get("fields.comment.comments["+i+"].body").toString();
                System.out.println(message);
                Assert.assertEquals(message, expectedMessage);
            }
        }



    }
}
