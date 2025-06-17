import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import net.datafaker.Faker;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import pojo.User;
import step.StepUserTest;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.hamcrest.CoreMatchers.equalTo;


@DisplayName("Parameterized Creating User Test (Параметризированный тест создания пользователя без заполнения обязательного поля )")
@RunWith(Parameterized.class)
public class ParameterizedCreatingUserTest {
    private String name;
    private String password;
    private String email;
    private String expected = "Email, password and name are required fields";
    private ValidatableResponse response;
    private StepUserTest step;
    private User user;

    public ParameterizedCreatingUserTest(String name, String password, String email) {

        this.name = name;
        this.password = password;
        this.email = email;
    }

    @Before
    public void setUp() {
        step = new StepUserTest();
        user = new User(name, password, email);
    }

    @Parameterized.Parameters(name = "{index}: данные name = {0}, password = {1}, email = {2}")
    public static Object[][] data(){
        return new Object[][]{
                {null, "password", "email@yahoo.com"},
                {"name", null, "email@yahoo.com"},
                {"name", "password", null},
        };
    }

    @Test
    @DisplayName("Parameterized Creating User Test")
    @Description("Тест: Получаем код 403, если создать пользователя и не заполнить одно из обязательных полей")
    public void ParameterizedCreatingUserTest() {
        response = step.stepCreatUser(user);
        response.assertThat().statusCode(SC_FORBIDDEN)
                .and().body("message", equalTo(expected));
    }
}
