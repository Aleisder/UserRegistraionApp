package com.tsarenko.demo.api;

import com.tsarenko.demo.mapper.UserDTORowMapper;
import com.tsarenko.demo.model.User;
import com.tsarenko.demo.model.UserDTO;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserApiTest {

    private final static String BASE_URL = "http://localhost:8080/api";

    @Autowired
    private NamedParameterJdbcTemplate template;

    @Test
    public void checkAddUser() {
        var user = new User(
                "TestLastName",
                "TestFirstName",
                "TestMiddleName",
                LocalDate.now()
        );

        given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post(BASE_URL + "/profile")
                .then()
                .log()
                .all();

        UserDTO actualUser = template.queryForObject(
                "SELECT * FROM public.user ORDER BY id DESK LIMIT 1",
                new MapSqlParameterSource(),
                new UserDTORowMapper()
        );

        assert actualUser != null;

        var expectedLastName = user.getLastName();
        var actualLastName = actualUser.lastName();

        assertEquals(expectedLastName, actualLastName);
    }

    @Test
    public void checkIdIsNotFound() {
        given()
                .baseUri(BASE_URL)
                .when()
                .get(BASE_URL + "/profile?id=-1")
                .then()
                .statusCode(404);
    }

    @Test
    public void checkUserById() {
        User user = given()
                .pathParams("id", 17)
                .when()
                .contentType(ContentType.JSON)
                .get(BASE_URL + "/profile?id={id}")
                .then()
                .extract()
                .body()
                .as(User.class);

        assertEquals(17, user.getId());
    }

}
