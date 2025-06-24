package uk.gov.hmcts.reform.dev;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
class SampleFunctionalTest {
    protected static int taskId;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 4001;
        RestAssured.useRelaxedHTTPSValidation();
    }

    @Test
    @Order(1)
    public void testCreateTask() {
        Response response = given()
            .contentType(ContentType.JSON)
            .body("""
                          {
                            "title": "New Task",
                            "description": "Rest Assured test",
                            "status": "TODO",
                            "dueDate": "2025-06-30",
                            "caseworkerId": 1
                          }
                      """)
            .when()
            .post("/task");
        response.then().statusCode(201);
        taskId = response.jsonPath().getInt("id");
    }


    @Test
    @Order(2)
    void testGetTaskById() {
        given()
            .pathParam("id", taskId)
            .when()
            .get("task/{id}")
            .then()
            .statusCode(200)
            .body("id", equalTo(taskId));
    }

    @Test
    @Order(3)
    void testGetAllTasks() {
        given()
            .when()
            .get("/task/all")
            .then()
            .statusCode(200)
            .body("size()", greaterThan(0));
    }

    @Test
    @Order(4)
    void testUpdateTask() {
        String jsonBody = """
        {
          "title": "Updated Task Title",
          "status": "IN_PROGRESS",
          "caseworkerId": 2
        }
        """;

        given()
            .contentType(ContentType.JSON)
            .body(jsonBody)
            .pathParam("id", taskId)
            .when()
            .patch("/task/{id}")
            .then()
            .statusCode(200)
            .body("title", equalTo("Updated Task Title"))
            .body("status", equalTo("IN_PROGRESS"));
    }

    @Test
    @Order(5)
    void testDeleteTask() {
        given()
            .pathParam("id", taskId)
            .when()
            .delete("/task/{id}")
            .then()
            .statusCode(204);

        // Verify it's deleted
        given()
            .pathParam("id", taskId)
            .when()
            .get("/task/{id}")
            .then()
            .statusCode(404);
    }
}
