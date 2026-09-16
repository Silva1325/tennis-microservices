package courts.boundary;

import java.util.UUID;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
class CourtResourceTest {

    private String uniqueName() {
        return "Court " + UUID.randomUUID();
    }

    private String courtJson(String name, String surface) {
        return """
                {"name":"%s","country":"Portugal","city":"Porto","surface":"%s"}
                """.formatted(name, surface);
    }

    @Test
    void createsACourt(){
        String surface = "CLAY";
        given()
            .contentType("application/json")
            .body(courtJson(uniqueName(), surface))
        .when()
            .post("/courts")
        .then()
            .statusCode(201)
            .body("id", notNullValue())
            .body("surface", is(surface));

    }

    @Test
    void rejectsDuplicateNameInSameCity(){

        String body = courtJson(uniqueName(), "CLAY");

        given()
            .contentType("application/json")
            .body(body)
        .when()
            .post("/courts")
        .then()
            .statusCode(201);

        given()
            .contentType("application/json")
            .body(body)
        .when()
            .post("/courts")
        .then()
            .statusCode(409)
            .body("message", notNullValue());
    }

    @Test
    void returns404ForUnknownCourt(){
        given()
        .when()
            .get("/courts/" + UUID.randomUUID())
        .then()
            .statusCode(404);
    }

    @Test
    void rejectsInvalidSurface() {
        given()
            .contentType("application/json")
            .body(courtJson(uniqueName(), "MUD"))
        .when()
            .post("/courts")
        .then()
            .statusCode(400);
    }

    @Test
    void updatesAndDeletesACourt() {
        String name = uniqueName();

        String id = given()
            .contentType("application/json")
            .body(courtJson(name, "CLAY"))
        .when()
            .post("/courts")
        .then()
            .statusCode(201)
            .extract().path("id");

        given()
            .contentType("application/json")
            .body(courtJson(name, "HARD"))
        .when()
            .put("/courts/" + id)
        .then().statusCode(200)
            .body("surface", is("HARD"));

        given()
        .when()
        .delete("/courts/" + id)
            .then()
            .statusCode(204);

        given()
        .when()
        .get("/courts/" + id)
            .then()
            .statusCode(404);
    }

}