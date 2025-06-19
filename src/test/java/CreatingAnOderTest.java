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

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

@DisplayName("Creating An Oder Test ( Тест создание заказа)")
public class CreatingAnOderTest {

    FakerUser fakerUser = new FakerUser();
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
    }


    @Test
    @DisplayName("creating An Order Authorization User Test (создание заказа авторизованным пользователем)")
    @Description("Тест: получаем код 200 при создание заказа авторизованным пользователем")
    public void creatingAnOrderAuthorizationUserTest() {
        response = stepOrderTest.creatingAnOrderSteps(token , 1);
        response.assertThat().statusCode(SC_OK)
                .and()
                .body("success", equalTo(true))
                .body("name", notNullValue())
                .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("creating An Order Unauthorized User Test (создание заказа неавторизованным пользователем)")
    @Description("Тест: получаем код 401 при создание заказа неавторизованным пользователем")
    public void creatingAnOrderUnauthorizedUserTest() {
        response = stepOrderTest.creatingAnOrderUnauthorizedUserTest(2);
        response.assertThat().statusCode(SC_UNAUTHORIZED)
                .and()
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("creating An Order With Ingredients Test (создание заказа с ингредиентами)")
    @Description("Тест: получаем код 200 при создание заказа с ингредиентами")
    public void creatingAnOrderWithIngredientsTest() {
        response = stepOrderTest.creatingAnOrderSteps(token , 8);
       System.out.println( stepOrderTest.getIngredientsJson(3));
         response.assertThat().statusCode(SC_OK)
                .and()
                .body("success", equalTo(true))
                .body("name", notNullValue())
                .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("creating An Order Without Ingredients Test (создание заказа без ингредиентов)")
    @Description("Тест: получаем код 400 при создание заказа без ингредиентов")
    public void creatingAnOrderWithoutIngredientsTest() {
        response = stepOrderTest.creatingAnOrderSteps(token , 0);
        response.assertThat().statusCode(SC_BAD_REQUEST)
                .and()
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("creating An Order Invalid Hash Test (создание заказа с неверным хешем ингредиентов)")
    @Description("Тест: получаем код 500 при создание заказа с неверным хешем ингредиентов")
    public void creatingAnOrderInvalidHashTest() {
        response = stepOrderTest.invalidHashSteps(token);
        response.assertThat().statusCode(SC_INTERNAL_SERVER_ERROR);
    }

    @After
    public void tearDown() {
        if (token != null) {
            stepUserTest.deleteUserStep(token);
        }
    }
}
