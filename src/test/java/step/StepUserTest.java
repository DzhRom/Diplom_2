package step;

import constants.Constants;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.StringUtils;
import pojo.User;
import pojo.UserGetJSON;

import static io.restassured.RestAssured.given;

public class StepUserTest {

    public static RequestSpecification requestSpecification(){
       return given()
               .header("Content-type", "application/json")
               .baseUri(Constants.BASE_URI);
    }

    @Step("Создание курьера")
    public ValidatableResponse stepCreatUser(User user){
        return requestSpecification()
                .body(user)
                .when()
                .post(Constants.API_POST_CREATING_USER)
                .then();
    }

    @Step("получение accessToken при создании пользователя")
    public String accessTokenStep(User user ){
        UserGetJSON userAccess = requestSpecification()
                .body(user)
                .when()
                .post(Constants.API_POST_LOGIN_USER)
                .body().as(UserGetJSON.class);
        return userAccess.getAccessToken();
    }

    @Step("получение refreshToken при создании пользователя")
    public String refreshTokenStep(User user ){
        UserGetJSON userAccess = requestSpecification()
                .body(user)
                .when()
                .post(Constants.API_POST_LOGIN_USER)
                .body().as(UserGetJSON.class);
        return "{ \"token\": \"{{" + userAccess.getAccessToken() + "}}\"  }";

    }

    @Step("Выход пользователя")
    public void stepLogoutUser( String accessToken, String refreshToken){
        requestSpecification()
                .auth().oauth2(accessToken)
                .body(refreshToken)
                .post(Constants.API_POST_LOGOUT_USER);
    }


    @Step("Удаление пользователя")
    public void deleteUserStep(String accessToken){
        requestSpecification()
                .auth().oauth2(accessToken)
                .delete(Constants.API_PATH_DELETE_USER)
                .then();
    }

    @Step("получения токена для удаления пользователя")
    public String token ( User userLogin){
        String token = accessTokenStep(userLogin);
        return StringUtils.substringAfter(token, " ");
    }

    @Step("Авторизация пользователя")
    public ValidatableResponse userLoginStep(User userLogin) {
        return requestSpecification()
                .given()
                .body(userLogin)
                .post(Constants.API_POST_LOGIN_USER)
                .then();
    }

   @Step("Обновление данных авторизованного пользователя")
    public ValidatableResponse userUpdateStep (String token, User updateDataUser){
        return requestSpecification()
                .given()
                .auth().oauth2(token)
                .body(updateDataUser)
                .patch(Constants.API_PATH_UPDATE_USER)
                .then();
    }

    @Step("Обновление данных неавторизованного пользователя")
    public ValidatableResponse UnauthorizedUserUpdateStep (User updateDataUser){
        return requestSpecification()
                .given()
                .body(updateDataUser)
                .patch(Constants.API_PATH_UPDATE_USER)
                .then();
    }

}
