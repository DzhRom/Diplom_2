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

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.equalTo;

@DisplayName("Login User Test ( Тест авторизация пользователя)")
public class LoginUserTest {

    private Faker faker = new Faker();
    private FakerUser fakerUser = new FakerUser();
    private User user;
    private User userLogin;
    private StepUserTest step;
    private String token;
    private ValidatableResponse response;


    @Before
    public void setUp() {
        step = new StepUserTest();
        user = fakerUser.fakerUser();
        response = step.stepCreatUser(user);
        token = step.token(new User(user.getEmail(), user.getPassword()) );
        userLogin = new User(user.getEmail(), user.getPassword());
    }

    @Test
    @DisplayName("login Test (авторизация пользователя)")
    @Description("Тест: Получаем код 200 при авторизации пользователя")
    public void loginTest(){
        response = step.userLoginStep(userLogin);
        response.assertThat().statusCode(SC_OK)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("login Incorrect Email Test (авторизация с неверным Email)")
    @Description("Тест: получаем код 401 при авторизации с неверным Email")
    public void loginIncorrectEmailTest(){
        userLogin.setEmail(faker.internet().emailAddress());
        response = step.userLoginStep(userLogin);
        response.assertThat().statusCode(SC_UNAUTHORIZED)
                .and()
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("login Incorrect Password Test (авторизация с неверным паролем)")
    @Description("Тест: Получаем код 401 при авторизации с неверным паролем")
    public void loginIncorrectPasswordTest() {
        userLogin.setPassword(faker.internet().password());
        response = step.userLoginStep(userLogin);
        response.assertThat().statusCode(SC_UNAUTHORIZED)
                .and()
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }


    @After
    public void tearDown() {
        if (token != null) {
            step.deleteUserStep(token);
        }
    }

}
