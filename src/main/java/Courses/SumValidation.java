package Courses;

import GoogleAPI.Payload;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SumValidation {
    @Test
    public void sumOfCourses(){
        JsonPath js = new JsonPath(Payload.coursePrice());
        int count = js.getInt("courses.size()");
        int sum = 0;
        int actualPurchaseAmount = js.getInt("dashboard.purchaseAmount");

        for (int i = 0; i < count; i++) {
            int price = js.getInt("courses["+i+"].price");
            int copies = js.getInt("courses["+i+"].copies");
            int amount = price * copies;
            System.out.println(amount);
            sum = sum + amount;
        }

        System.out.println(sum);
        Assert.assertEquals(sum, actualPurchaseAmount);
    }
}