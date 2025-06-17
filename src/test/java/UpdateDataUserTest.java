import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import net.datafaker.Faker;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.FakerUser;
import pojo.User;
import step.StepUserTest;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;

@DisplayName("Update Data User Test (Тест обновления данных пользователя)")
public class UpdateDataUserTest {

    private Faker faker = new Faker();
    private  FakerUser fakerUser = new FakerUser();
    private User user;
    private String token;
    private String token2;
    private StepUserTest step;
    private ValidatableResponse response;

    private String setName = faker.name().name();
    private String setPassword = faker.internet().password(6,8);
    private String setEmail = faker.internet().emailAddress();

    @Before
    public void setUp() {
        step = new StepUserTest();
        user =  fakerUser.fakerUser();
        step.stepCreatUser( user);
        token = step.token(new User (user.getEmail(), user.getPassword()));
    }

    @Test
    @DisplayName("update Name User Test (изменение имени пользователя)")
    @Description("Тест: получаем код 200 при запросе на изменение имени пользователя: с авторизацией")
    public void updateNameUserTest() {
        user.setName(setName);
        response = step.userUpdateStep(token, user);

        response.assertThat().statusCode(SC_OK)
                .and()
                .body("success", equalTo(true))
                .body("user.name", equalTo(setName));
    }

    @Test
    @DisplayName("update Email User Test (изменение email пользователя)")
    @Description("Тест: получаем код 200 при запросе на изменение email пользователя: с авторизацией")
    public void updateEmailUserTest() {
        user.setEmail(setEmail);
        response = step.userUpdateStep(token, user);

        response.assertThat().statusCode(SC_OK)
                .and()
                .body("success", equalTo(true))
                .body("user.email", equalTo(setEmail.toLowerCase()));
    }

    //переделать СБРОС пароля
    @Test
    @DisplayName("update Password User Test (изменение пароля пользователя)")
    @Description("Тест: получаем код 200 при запросе на изменение пароля пользователя: с авторизацией")
    public void updatePasswordUserTest() {
        user.setPassword(setPassword);
        response = step.userUpdateStep(token, user);
        response.assertThat().statusCode(SC_OK)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("update Busy Email User Test (изменения email на уже занятый)")
    @Description("Тест: получаем код 403 при запросе на изменение email пользователя на уже занятый email")
    public void updateBusyEmailUserTest() {
        User user2 = fakerUser.fakerUser();
        step.stepCreatUser(user2);
        token2 = step.token(user2);
        user.setEmail(user2.getEmail());
        response = step.userUpdateStep(token, user);

        response.assertThat().statusCode(SC_FORBIDDEN)
                .and()
                .body("success", equalTo(false))
                .body("message", equalTo("User with such email already exists"));
    }


    @Test
    @DisplayName("update Name Unauthorized User Test (изменение имени неавторизованного пользователя)")
    @Description("Тест: получаем код 401 при запросе на изменение имени пользователя: без авторизации")
    public void updateNameUnauthorizedUserTest() {
        user.setName(setName);
        response = step.UnauthorizedUserUpdateStep(user);

        response.assertThat().statusCode(SC_UNAUTHORIZED)
                .and()
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("update Email Unauthorized User Test (изменение email неавторизованного пользователя)")
    @Description("Тест: получаем код 401 при запросе на изменение email пользователя: без авторизации")
    public void updateEmailUnauthorizedUserTest() {
        user.setEmail(setEmail);
        response = step.UnauthorizedUserUpdateStep(user);

        response.assertThat().statusCode(SC_UNAUTHORIZED)
                .and()
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("update Password Unauthorized User Test (изменение пароля неавторизованного пользователя)")
    @Description("Тест: получаем код 401 при запросе на изменение пароля пользователя: без авторизации")
    public void updatePasswordUnauthorizedUserTest() {
        user.setPassword(setPassword);
        response = step.UnauthorizedUserUpdateStep(user);

        response.assertThat().statusCode(SC_UNAUTHORIZED)
                .and()
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }

    @After
    public void tearDown() {
        if (token != null) {
            step.deleteUserStep(token);
        }
        if (token2 != null) {
            step.deleteUserStep(token2);
        }
    }

}
