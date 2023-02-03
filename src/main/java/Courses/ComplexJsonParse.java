package Courses;

import GoogleAPI.Payload;
import io.restassured.path.json.JsonPath;

public class ComplexJsonParse {
    public static void main(String[] args) {
        JsonPath js = new JsonPath(Payload.coursePrice());

        //print number of courses returned by API
        int count = js.getInt("courses.size()");
        System.out.println(count);
        //print purchase amount
        int totalAmount = js.getInt("dashboard.purchaseAmount");
        System.out.println(totalAmount);
        //print title of first course
        String titleFirstCourse = js.get("courses[0].title");
        System.out.println(titleFirstCourse);
        //print course titles and prices
        for (int i = 0; i < count; i++) {
            String courseTitles = js.get("courses["+i+"].title");
            int prices = js.getInt("courses["+i+"].price");
            System.out.println(courseTitles + prices);
        }
        //print number of copies sold by rpa course
        System.out.println("number of rpa copies sold");
        for (int i = 0; i < count; i++) {
            String courseTitles = js.get("courses["+i+"].title");
            if (courseTitles.equalsIgnoreCase("RPA"))
            {
                int copies = js.get("courses["+i+"].copies");
                System.out.println(copies);
                break;
            }
        }

        //verify if sum of all course prices matches with purchase amount
        int actualPurchaseAmount = js.getInt("dashboard.purchaseAmount");


    }
}
