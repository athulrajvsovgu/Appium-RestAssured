package justSample_ToBeRemoved_Later;


import org.testng.annotations.Test;
import io.restassured.response.Response;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import java.io.IOException;
import java.util.HashMap;

/*
 * given()
 * 		content type, set cookies, add authentication, add params, set header info etc.
 * 
 * when()
 * 		get, post , put, delete, actions
 * 
 * then ()
 * 		validate status code, extract response, extract headers, cookies, response body etc.
 */

public class HTTPRequests {

	/*
	 * Using - A hosted REST-API ready to respond to your AJAX requests -
	 * https://reqres.in/
	 */
	
	private int userId;

	@Test(priority = 0)
	void getUsers() throws IOException {
		
		given()
		
		.when()
			.get("https://reqres.in/api/users?page=2")
			
		.then()
			.statusCode(200)
			.body("page", equalTo(2))
			.log()
			.all();
		
	    /*
	     * TODO:-
	     * Add logs to TestNG report
	     */

	}
	
	@Test(priority = 1)
	void  createUser() {
		HashMap<String, String> newUserData = new HashMap<String, String>();
		newUserData.put("name", "Athul");
		newUserData.put("job", "Software-Tester");
		
		/* The issue in the code in the comments, is that the .jsonPath().getInt("id") method is used incorrectly within the request-building chain. 
		 * This method is designed to extract data from the response after the server returns it. 
		 * In your code, it appears you're attempting to extract the "id" field before reaching the .then() block, 
		 * but REST Assured doesn't allow this because response extraction must be separate from assertions.
		 * To make this code work, you need to extract the response first and then access its JSON fields using jsonPath. The fix is given after code:
		 
			given()
				.contentType("application/json")
				.body(newUserData)
					
			.when()
				.post("https://reqres.in/api/users")
				.jsonPath()
				.getInt("id")
					
			.then()
				.statusCode(201)
				.log()
				.all();
		*/
	    // Step 1: Send POST request and capture the response
	    Response response = given()
						        .contentType("application/json")
						        .body(newUserData)
						        
						    .when()
						    	.post("https://reqres.in/api/users");

	    // Step 2: Extract the "id" from the response using jsonPath
	    userId = response.jsonPath().getInt("id");
	    System.out.println("User ID: " + userId); // Print or use the ID in further logic

	    // Step 3: Validate the response and log details
	    response.then()
	        		.statusCode(201)
			        .log()
			        .all();
		
	}
	
	@Test(priority = 2, dependsOnMethods = "createUser")
	void  updateUser() {
		HashMap<String, String> newUserData = new HashMap<String, String>();
		newUserData.put("name", "Raj");
		newUserData.put("job", "QA-Tester");
		
	    given()
			.contentType("application/json")
			.body(newUserData)
			
		.when()
			.put("https://reqres.in/api/users/"+userId)
			
		.then()
    		.statusCode(200)
	        .log()
	        .all();
	}
	
	@Test(priority = 3, dependsOnMethods = "createUser")
	void  deleteUser() {
		
		/*
		 * If nothing is there to be specified as content-type, given() could be removed. But .when() should be changed to when() 
		 */
			
		when()
			.delete("https://reqres.in/api/users/"+userId)
			
		.then()
    		.statusCode(204)
	        .log()
	        .all();
	}

}
