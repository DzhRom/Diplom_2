import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.FakerUser;
import pojo.User;
import step.StepOrderTest;
import step.StepUserTest;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

@DisplayName("Receiving The Order Test(Тест получения заказов пользователя )")
public class ReceivingTheOrderTest {
    private FakerUser fakerUser = new FakerUser();
    private StepOrderTest stepOrderTest;
    private ValidatableResponse response;
    private StepUserTest stepUserTest;
    private User loginUser;
    private User user;
    private String token;

    @Before
    public void setUp() {
        stepOrderTest = new StepOrderTest();
        stepUserTest = new StepUserTest();
        user = fakerUser.fakerUser();
        stepUserTest.stepCreatUser(user);
        loginUser = new User( user.getEmail(), user.getPassword());
        token = stepUserTest.token(loginUser);
        stepOrderTest.creatingAnOrderSteps(token, 2);
    }

    @Test
    @DisplayName("receiving The Order Authorized User Test (получение заказов авторизованного пользователя)")
    @Description("Тест: получаем код 200 при запросе на получение заказов авторизованного пользователя ")
    public void receivingTheOrderAuthorizedUserTest(){
        response = stepOrderTest.getOrderAuthorizedUserTest(token);

        response.assertThat().statusCode(SC_OK)
                .and()
                .body("success", equalTo(true))
                .body("orders", notNullValue());
    }

    @Test
    @DisplayName("receiving The Order Unauthorized User Test (получение заказов не авторизованного пользователя)")
    @Description("Тест: получаем код 401 при запросе на получение заказов не авторизованного пользователя")
    public void receivingTheOrderUnauthorizedUserTest(){
        stepUserTest.stepLogoutUser(token, stepUserTest.refreshTokenStep(loginUser));
        response = stepOrderTest.getOrderUnauthorizedUserTest();

        response.assertThat().statusCode(SC_UNAUTHORIZED)
                .and()
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }


    @After
    public void tearDown() {
        if (token != null) {
            stepUserTest.deleteUserStep(token);
        }
    }
}
