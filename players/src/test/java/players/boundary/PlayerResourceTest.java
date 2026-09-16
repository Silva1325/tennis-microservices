package players.boundary;

import java.util.UUID;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

@QuarkusTest
class PlayerResourceTest {

    private String uniqueEmail() {
        return "p" + UUID.randomUUID() + "@example.com";
    }

    private String playerJson(String email, int age) {
        return """
                {"email":"%s","password":"secret","firstname":"Rafael","lastname":"Nadal","country":"Spain","age":%d}
                """.formatted(email, age);
    }

    private String updatePlayerJson(String email, int age) {
        return """
                {"email":"%s","firstname":"Rafael","lastname":"Nadal","country":"Spain","age":%d}
                """.formatted(email, age);
    }

    @Test
    void createsAPlayer() {
        String email = uniqueEmail();
        int age = 37;

        given()
                .contentType("application/json")
                .body(playerJson(email, age))
                .when()
                .post("/players")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("email", is(email))
                .body("age", is(age));
    }

    @Test
    void neverReturnsThePassword() {
        given()
                .contentType("application/json")
                .body(playerJson(uniqueEmail(), 37))
                .when()
                .post("/players")
                .then()
                .statusCode(201)
                .body("password", nullValue());
    }

    @Test
    void rejectsDuplicateEmail() {
        String body = playerJson(uniqueEmail(), 37);

        given()
                .contentType("application/json")
                .body(body)
                .when()
                .post("/players")
                .then()
                .statusCode(201);

        given()
                .contentType("application/json")
                .body(body)
                .when()
                .post("/players")
                .then()
                .statusCode(409)
                .body("message", notNullValue());
    }

    @Test
    void returns404ForUnknownPlayer() {
        given()
                .when()
                .get("/players/" + UUID.randomUUID())
                .then()
                .statusCode(404)
                .body("message", notNullValue());
    }

    @Test
    void rejectsInvalidEmail() {
        String invalidEmail = "not-an-email";

        given()
                .contentType("application/json")
                .body(playerJson(invalidEmail, 37))
                .when()
                .post("/players")
                .then()
                .statusCode(400);
    }

    @Test
    void updatesAndDeletesAPlayer() {
        String email = uniqueEmail();
        int initialAge = 37;
        int updatedAge = 38;

        String id = given()
                .contentType("application/json")
                .body(playerJson(email, initialAge))
                .when()
                .post("/players")
                .then()
                .statusCode(201)
                .extract().path("id");

        given()
                .contentType("application/json")
                .body(updatePlayerJson(email, updatedAge))
                .when()
                .put("/players/" + id)
                .then()
                .statusCode(200)
                .body("age", is(updatedAge));

        given()
                .when()
                .delete("/players/" + id)
                .then()
                .statusCode(204);

        given()
                .when()
                .get("/players/" + id)
                .then()
                .statusCode(404);
    }
}