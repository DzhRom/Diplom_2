import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.FakerUser;
import pojo.User;
import step.StepUserTest;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

@DisplayName("Creating User Test ( Тест создание пользователя) ")
public class CreatingUserTest {

    FakerUser fakerUser = new FakerUser();
    private User creatUser;
    private User loginUser;
    private StepUserTest step;
    private String token;
    private ValidatableResponse response;

    @Before
    public void setUp() {
        step = new StepUserTest();
        creatUser =  fakerUser.fakerUser();
        loginUser = new User(creatUser.getEmail(), creatUser.getPassword());
    }


   @Test
   @DisplayName("test Create User (создать пользователя)")
   @Description("Тест: Получаем код 200 при создании пользователя")
   public void testCreateUser() {
        response = step.stepCreatUser(creatUser);
        token = step.token(loginUser);

        response.assertThat().statusCode(SC_OK)
               .and()
               .body("success", equalTo(true))
               .body("accessToken", notNullValue())
               .body("refreshToken", notNullValue());
   }

   @Test
   @DisplayName("test Creating Already Registered User (создать зарегистрированного пользователя)")
   @Description("Тест: Получаем код 403 при создании пользователя, который уже зарегистрирован")
   public void testCreatingAlreadyRegisteredUser() {
        response = step.stepCreatUser(creatUser);
        token = step.token(loginUser);

        response = step.stepCreatUser(creatUser);
        response.assertThat().statusCode(SC_FORBIDDEN)
                .and()
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
   }


   @After
    public void deleteData() {
        if (token != null) {
            step.deleteUserStep(token);
        }
   }
}
