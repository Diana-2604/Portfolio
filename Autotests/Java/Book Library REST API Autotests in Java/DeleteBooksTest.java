package suites;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import net.minidev.json.JSONObject;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class DeleteBooksTest {
    @Test
    @DisplayName("BL-16 Удаление книги по существующему ID")
    public void deleteBookById_checkStatusCode_expectHttp200() {

        RestAssured.baseURI ="http://localhost:5000/api";
        RequestSpecification request = RestAssured.given();

        JSONObject requestParams = new JSONObject();
        requestParams.put("name", "another VERY clever book");
        request.header("Content-Type", "application/json");
        request.body(requestParams.toJSONString());

        Response response = request.post("/books");
        Assert.assertEquals(201, response.getStatusCode());

        int id = response.jsonPath().getInt("book.id");

        given().
        when().
                delete("http://localhost:5000/api/books/" + id).
        then().
                assertThat().
                statusCode(200).
                body("result", equalTo(true));

        given().
                when().
                get("http://localhost:5000/api/books/" + id).
                then().
                assertThat().
                statusCode(404);
    }

    @Test
    @DisplayName("BL-17 Удаление книги по несуществующему ID")
    public void deleteBookByInvalidId_checkStatusCode_expectHttp404() {

        given().
        when().
                delete("http://localhost:5000/api/books/0").
        then().
                assertThat().
                statusCode(404).
                body("error", equalTo("Book with id 0 not found"));
    }

    @Test
    @DisplayName("BL-18 Удаление всех книг в библиотеке")
    public void deleteAllBook_checkStatusCode_expectHttp405() {

        given().
                when().
                delete("http://localhost:5000/api/books").
                then().
                assertThat().
                statusCode(405);
    }
}
