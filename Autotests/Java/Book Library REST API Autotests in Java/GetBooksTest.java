package suites;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import net.minidev.json.JSONObject;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class GetBooksTest {

    public Integer id = postNewBook_getValidId();

    public Integer postNewBook_getValidId() {

        RestAssured.baseURI ="http://localhost:5000/api";
        RequestSpecification postRequest = RestAssured.given();

        JSONObject postRequestParams = new JSONObject();
            postRequestParams.put("name", "Test book");
            postRequest.header("Content-Type", "application/json");
            postRequest.body(postRequestParams.toJSONString());

        Response response = postRequest.post("/books");

        return (response.jsonPath().getInt("book.id"));
    }

    @Test
    @DisplayName("BL-1 Получение списка всех книг в библиотеке")
    public void requestAllBooks_expectHttp200_ResponseNotEmpty() {

        given().
        when().
                get("http://localhost:5000/api/books").
        then().
        assertThat().
                statusCode(200).
                contentType("application/json").
                body("isEmpty()", Matchers.is(false));
    }

    @Test
    @DisplayName("BL-3 Получение информации о конкретной книге")
    public void requestBookId_expectHttp200_ResponseNotEmpty() {

        given().
        when().
                get("http://localhost:5000/api/books/" + id).
        then().
        assertThat().
                statusCode(200).
                contentType("application/json").
                body("isEmpty()", Matchers.is(false)).
                body("book.id", equalTo(id));
    }

    @Test
    @DisplayName("BL-2 Информация о книге соответствует структуре")
    public void requestBookId_checkFieldsInResponseBody_expect5() {

        given().
        when().
                get("http://localhost:5000/api/books/" + id).
        then().
        assertThat().
                statusCode(200).
                contentType("application/json").
                body("book", hasKey("author")).
                body("book", hasKey("id")).
                body("book", hasKey("isElectronicBook")).
                body("book", hasKey("name")).
                body("book", hasKey("year"));
    }

    @Test
    @DisplayName("BL-5 Получение информации о несуществующей книге")
    public void requestInvalidBookId_expectHttp404_errorMessage() {

        given().
        when().
                get("http://localhost:5000/api/books/0").
        then().
        assertThat().
                statusCode(404).
                body("error", equalTo("Book with id 0 not found"));
    }
}
