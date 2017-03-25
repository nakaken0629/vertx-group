package red.itvirtuoso.vertx.sample;

import com.jayway.restassured.RestAssured;
import io.vertx.core.json.Json;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.io.InputStream;
import java.io.InputStreamReader;

public class MyRestIT {
    @BeforeClass
    public static void configureRestAssured() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = Integer.getInteger("http.port", 8080);
    }

    @AfterClass
    public static void unconfigureRestAssured() {
        RestAssured.reset();
    }
}
