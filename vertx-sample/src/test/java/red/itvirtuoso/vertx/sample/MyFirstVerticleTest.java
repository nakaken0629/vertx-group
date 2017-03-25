package red.itvirtuoso.vertx.sample;

import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import red.itvirtuoso.vertx.sample.first.Whisky;

import static com.jayway.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;

@RunWith(VertxUnitRunner.class)
public class MyFirstVerticleTest {

    @Test
    public void checkThatWeCanRetrieveIndividualProduct() {
        // Get the list of bottles, ensure it's a success and extract the first id.
        final int id = get("/api/whiskies").then()
                .assertThat()
                .statusCode(200)
                .extract()
                .jsonPath().getInt("find { it.name=='Bowmore 15 Years Laimrig' }.id");
        // Now get the individual resource and check the content
        get("/api/whiskies/" + id).then()
                .assertThat()
                .statusCode(200)
                .body("name", equalTo("Bowmore 15 Years Laimrig"))
                .body("origin", equalTo("Scotland, Islay"))
                .body("id", equalTo(id));
    }

    @Test
    public void checkWeCanAddAndDeleteAProduct() {
        // Create a new bottle and retrieve the result (as a Whisky instance).
        Whisky whisky = given()
                .body("{\"name\":\"Jameson\", \"origin\":\"Ireland\"}")
                .request().post("/api/whiskies")
                .thenReturn()
                .as(Whisky.class);
        assertThat(whisky.getName()).isEqualToIgnoringCase("Jameson");
        assertThat(whisky.getOrigin()).isEqualToIgnoringCase("Ireland");
        assertThat(whisky.getId()).isNotZero();
        // Check that it has created an individual resource, and check the content.
        get("/api/whiskies/" + whisky.getId()).then()
                .assertThat()
                .statusCode(200)
                .body("name", equalTo("Jameson"))
                .body("origin", equalTo("Ireland"))
                .body("id", equalTo(whisky.getId()));
        // Delete the bottle
        delete("/api/whiskies/" + whisky.getId()).then().assertThat().statusCode(204);
        // Check that the resource is not available anymore
        get("/api/whiskies/" + whisky.getId())
                .then()
                .assertThat()
                .statusCode(404);
    }
}
