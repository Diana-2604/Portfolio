package suites;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import net.minidev.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

public class PutBooksTest {

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
    @DisplayName("BL-10 Изменение книги по существующему ID с предоставлением полной информации")
    public void updateBook_provideFullInfo_expectHttp200() {

        RequestSpecification request = RestAssured.given();

        JSONObject requestParams = new JSONObject();
            requestParams.put("author", "Адитья Бхаргава");
            requestParams.put("isElectronicBook", false);
            requestParams.put("name", "Грокаем алгоритмы");
            requestParams.put("year", 2016);
        request.header("Content-Type", "application/json");
        request.body(requestParams.toJSONString());

        Response response = request.put("/books/" + id);

        requestParams.put("id", id);

        Assert.assertEquals(200, response.getStatusCode());

        Assert.assertEquals(requestParams.get("author"), response.jsonPath().get("book.author"));
        Assert.assertEquals(requestParams.get("id"), response.jsonPath().get("book.id"));
        Assert.assertEquals(requestParams.get("isElectronicBook"), response.jsonPath().get("book.isElectronicBook"));
        Assert.assertEquals(requestParams.get("name"), response.jsonPath().get("book.name"));
        Assert.assertEquals(requestParams.get("year"), response.jsonPath().get("book.year"));
    }

    @Test
    @DisplayName("BL-11 Изменение книги по существующему ID с отсутствием названия")
    public void updateBook_provideInfoMissingName_expectHttp400() {

        RequestSpecification request = RestAssured.given();

        JSONObject requestParams = new JSONObject();
            requestParams.put("author", "Адитья Бхаргава");
            requestParams.put("isElectronicBook", false);
            requestParams.put("year", 2016);
            request.header("Content-Type", "application/json");
            request.body(requestParams.toJSONString());

        Response response = request.put("/books/" + id);

        Assert.assertEquals(400, response.getStatusCode());
        Assert.assertEquals("Name is required", response.jsonPath().getString("error"));
    }

    @Test
    @DisplayName("BL-12 Изменение книги по существующему ID с отсутствием автора")
    public void updateBook_provideInfoMissingAuthor_expectHttp400() {

        RequestSpecification request = RestAssured.given();

        JSONObject requestParams = new JSONObject();
        requestParams.put("isElectronicBook", false);
        requestParams.put("name", "Грокаем алгоритмы");
        requestParams.put("year", 2016);
        request.header("Content-Type", "application/json");
        request.body(requestParams.toJSONString());

        Response response = request.put("/books/" + id);

        Assert.assertEquals(400, response.getStatusCode());
        Assert.assertEquals("Author is required", response.jsonPath().getString("error"));
    }

    @Test
    @DisplayName("BL-13 Изменение книги по существующему ID с отсутствием года издания")
    public void updateBook_provideInfoMissingYear_expectHttp400() {

        RequestSpecification request = RestAssured.given();

        JSONObject requestParams = new JSONObject();
            requestParams.put("author", "Адитья Бхаргава");
            requestParams.put("isElectronicBook", false);
            requestParams.put("name", "Грокаем алгоритмы");
            request.header("Content-Type", "application/json");
            request.body(requestParams.toJSONString());

        Response response = request.put("/books/" + id);

        Assert.assertEquals(400, response.getStatusCode());
        Assert.assertEquals("Year is required", response.jsonPath().getString("error"));
    }

    @Test
    @DisplayName("BL-14 Изменение книги по существующему ID с отсутствием типа издания")
    public void updateBook_provideInfoMissingBookType_expectHttp400() {

        RequestSpecification request = RestAssured.given();

        JSONObject requestParams = new JSONObject();
            requestParams.put("author", "Адитья Бхаргава");
            requestParams.put("name", "Грокаем алгоритмы");
            requestParams.put("year", 2016);
            request.header("Content-Type", "application/json");
            request.body(requestParams.toJSONString());

        Response response = request.put("/books/" + id);

        Assert.assertEquals(400, response.getStatusCode());
        Assert.assertEquals("IsElectronicBook is required", response.jsonPath().getString("error"));
    }

    @Test
    @DisplayName("BL-23 Изменение книги по существующему ID с невалидным значением типа издания")
    public void updateBook_provideWrongBookType_expectHttp400() {

        RequestSpecification request = RestAssured.given();

        JSONObject requestParams = new JSONObject();
        requestParams.put("author", "Адитья Бхаргава");
        requestParams.put("isElectronicBook", null);
        requestParams.put("name", "Грокаем алгоритмы");
        requestParams.put("year", 2016);
        request.header("Content-Type", "application/json");
        request.body(requestParams.toJSONString());

        Response response = request.put("/books/" + id);

        Assert.assertEquals(400, response.getStatusCode());
        Assert.assertEquals("IsElectronicBook must be Bool type", response.jsonPath().getString("error"));
    }

    @Test
    @DisplayName("BL-24 Изменение книги по существующему ID с невалидным значением автора")
    public void updateBook_provideWrongAuthor_expectHttp400() {

        RequestSpecification request = RestAssured.given();

        JSONObject requestParams = new JSONObject();
        requestParams.put("author", null);
        requestParams.put("isElectronicBook", false);
        requestParams.put("name", "Грокаем алгоритмы");
        requestParams.put("year", 2016);
        request.header("Content-Type", "application/json");
        request.body(requestParams.toJSONString());

        Response response = request.put("/books/" + id);

        Assert.assertEquals(400, response.getStatusCode());
        Assert.assertEquals("Author must be String type (Unicode)", response.jsonPath().getString("error"));
    }

    @Test
    @DisplayName("BL-25 Изменение книги по существующему ID с невалидным значением названия")
    public void updateBook_provideWrongName_expectHttp400() {

        RequestSpecification request = RestAssured.given();

        JSONObject requestParams = new JSONObject();
        requestParams.put("author", "Адитья Бхаргава");
        requestParams.put("isElectronicBook", false);
        requestParams.put("name", null);
        requestParams.put("year", 2016);
        request.header("Content-Type", "application/json");
        request.body(requestParams.toJSONString());

        Response response = request.put("/books/" + id);

        Assert.assertEquals(400, response.getStatusCode());
        Assert.assertEquals("Name must be String type (Unicode)", response.jsonPath().getString("error"));
    }

    @Test
    @DisplayName("BL-26 Изменение книги по существующему ID с невалидным значением года издания")
    public void updateBook_provideWrongYear_expectHttp400() {

        RequestSpecification request = RestAssured.given();

        JSONObject requestParams = new JSONObject();
        requestParams.put("author", "Адитья Бхаргава");
        requestParams.put("isElectronicBook", false);
        requestParams.put("name", "Грокаем алгоритмы");
        requestParams.put("year", null);
        request.header("Content-Type", "application/json");
        request.body(requestParams.toJSONString());

        Response response = request.put("/books/" + id);

        Assert.assertEquals(400, response.getStatusCode());
        Assert.assertEquals("Year must be Int type", response.jsonPath().getString("error"));
    }

    @Test
    @DisplayName("BL-15 Изменение книги по несуществующему ID")
    public void updateBook_provideInvalidId_expectHttp404() {

        RequestSpecification request = RestAssured.given();

        JSONObject requestParams = new JSONObject();
            requestParams.put("author", "Адитья Бхаргава");
            requestParams.put("isElectronicBook", false);
            requestParams.put("name", "Грокаем алгоритмы");
            requestParams.put("year", 2016);
            request.header("Content-Type", "application/json");
            request.body(requestParams.toJSONString());

        Response response = request.put("/books/0");

        Assert.assertEquals(404, response.getStatusCode());
        Assert.assertEquals("Book with id 0 not found", response.jsonPath().getString("error"));
    }
}
