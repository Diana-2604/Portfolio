package suites;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import net.minidev.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

public class PostBooksTest {
    @Test
    @DisplayName("BL-6 Добавление новой книги с полной информацией")
    public void createNewBook_provideFullInfo_expectHttp201() {

        RestAssured.baseURI ="http://localhost:5000/api";
        RequestSpecification request = RestAssured.given();

        JSONObject requestParams = new JSONObject();
            requestParams.put("author", "Барри Бёрд");
            requestParams.put("isElectronicBook", true);
            requestParams.put("name", "JAVA для чайников");
            requestParams.put("year", 2011);
            request.header("Content-Type", "application/json");
            request.body(requestParams.toJSONString());

        Response response = request.post("/books");

        Assert.assertEquals(201, response.getStatusCode());
        Assert.assertNotNull(response.jsonPath().get("book.id"));
    }

    @Test
    @DisplayName("BL-19 Добавление новой книги только с названием")
    public void createNewBook_provideNameOnly_expectHttp201() {

        RestAssured.baseURI ="http://localhost:5000/api";
        RequestSpecification request = RestAssured.given();

        JSONObject requestParams = new JSONObject();
            requestParams.put("name", "Тестирование черного ящика");
            request.header("Content-Type", "application/json");
            request.body(requestParams.toJSONString());

        Response response = request.post("/books");

        Assert.assertEquals(201, response.getStatusCode());
        Assert.assertNotNull(response.jsonPath().get("book.id"));
    }

    @Test
    @DisplayName("BL-9 Добавление книги без названия")
    public void createNewBook_provideNoName_expectHttp400() {

        RestAssured.baseURI ="http://localhost:5000/api";
        RequestSpecification request = RestAssured.given();

        JSONObject requestParams = new JSONObject();
            requestParams.put("author", "Рекс Блэк");
            requestParams.put("isElectronicBook", false);
            requestParams.put("year", 2006);
            request.header("Content-Type", "application/json");
            request.body(requestParams.toJSONString());

        Response response = request.post("/books");

        Assert.assertEquals(400, response.getStatusCode());
        Assert.assertEquals("Name is required", response.jsonPath().getString("error"));
    }

    @Test
    @DisplayName("BL-7 Добавление книги с пустым названием")
    public void createNewBook_provideEmptyName_expectHttp400() {

        RestAssured.baseURI ="http://localhost:5000/api";
        RequestSpecification request = RestAssured.given();

        JSONObject requestParams = new JSONObject();
            requestParams.put("author", "Борис Бейзер");
            requestParams.put("isElectronicBook", false);
            requestParams.put("name", "");
            requestParams.put("year", 2004);
            request.header("Content-Type", "application/json");
            request.body(requestParams.toJSONString());

        Response response = request.post("/books");

        Assert.assertEquals(400, response.getStatusCode());
        Assert.assertEquals("Name is required", response.jsonPath().getString("error"));
    }

    @Test
    @DisplayName("BL-8 Добавление книги с названием null")
    public void createNewBook_provideNullName_expectHttp400() {

        RestAssured.baseURI ="http://localhost:5000/api";
        RequestSpecification request = RestAssured.given();

        JSONObject requestParams = new JSONObject();
            requestParams.put("author", "Элфрид Дастин, Джефф Рэшка, Джон Пол");
            requestParams.put("isElectronicBook", false);
            requestParams.put("name", null);
            requestParams.put("year", 2003);
            request.header("Content-Type", "application/json");
            request.body(requestParams.toJSONString());

        Response response = request.post("/books");

        Assert.assertEquals(400, response.getStatusCode());
        Assert.assertEquals("Name must be String type (Unicode)", response.jsonPath().getString("error"));
    }

    @Test
    @DisplayName("BL-20 Добавление книги с типом издания null")
    public void createNewBook_provideNullBookType_expectHttp400() {

        RestAssured.baseURI ="http://localhost:5000/api";
        RequestSpecification request = RestAssured.given();

        JSONObject requestParams = new JSONObject();
        requestParams.put("author", "Элфрид Дастин, Джефф Рэшка, Джон Пол");
        requestParams.put("isElectronicBook", null);
        requestParams.put("name", "Автоматизированное тестирование программного обеспечения");
        requestParams.put("year", 2003);
        request.header("Content-Type", "application/json");
        request.body(requestParams.toJSONString());

        Response response = request.post("/books");

        Assert.assertEquals(400, response.getStatusCode());
        Assert.assertEquals("IsElectronicBook must be Bool type", response.jsonPath().getString("error"));
    }

    @Test
    @DisplayName("BL-21 Добавление книги с автором null")
    public void createNewBook_provideNullAuthor_expectHttp400() {

        RestAssured.baseURI ="http://localhost:5000/api";
        RequestSpecification request = RestAssured.given();

        JSONObject requestParams = new JSONObject();
        requestParams.put("author", null);
        requestParams.put("isElectronicBook", false);
        requestParams.put("name", "Автоматизированное тестирование программного обеспечения");
        requestParams.put("year", 2003);
        request.header("Content-Type", "application/json");
        request.body(requestParams.toJSONString());

        Response response = request.post("/books");

        Assert.assertEquals(400, response.getStatusCode());
        Assert.assertEquals("Author must be String type (Unicode)", response.jsonPath().getString("error"));
    }

    @Test
    @DisplayName("BL-22 Добавление книги с годом издания null")
    public void createNewBook_provideNullYear_expectHttp400() {

        RestAssured.baseURI ="http://localhost:5000/api";
        RequestSpecification request = RestAssured.given();

        JSONObject requestParams = new JSONObject();
        requestParams.put("author", "Элфрид Дастин, Джефф Рэшка, Джон Пол");
        requestParams.put("isElectronicBook", false);
        requestParams.put("name", "Автоматизированное тестирование программного обеспечения");
        requestParams.put("year", null);
        request.header("Content-Type", "application/json");
        request.body(requestParams.toJSONString());

        Response response = request.post("/books");

        Assert.assertEquals(400, response.getStatusCode());
        Assert.assertEquals("Year must be Int type", response.jsonPath().getString("error"));
    }
}
