package step;

import constants.Constants;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import pojo.Ingredients;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;


public class StepOrderTest {

    private ValidatableResponse response;

   public static RequestSpecification requestSpecification() {
       return given()
               .header("Content-type", "application/json")
               .baseUri(Constants.BASE_URI);
   }

   @Step("Получение списка ингредиентов")
    public ValidatableResponse getIngredients() {
       return requestSpecification()
                .get(Constants.API_GET_INGREDIENTS)
                .then();
   }

   @Step("формирование заказа с N количеством ингредиентов")
   public List getIngredientsJson(Integer count) {
       ArrayList<String> ingredientsList = new ArrayList<>();
       response = getIngredients();
       ArrayList<String> list = response.extract().path("data._id");
       if (count > list.size()) { count = list.size();}
       if (count <= 0) {
           return ingredientsList;
       }
       for (int i = 0; i < count; i++) {
           ingredientsList.add(list.get(i));
       }
       return ingredientsList;
   }

   @Step("Создание заказа")
    public ValidatableResponse creatingAnOrderSteps(String token, Integer count) {
       Ingredients ingredients = new Ingredients();
       ingredients.setIngredients(getIngredientsJson(count));

       return requestSpecification()
               .auth().oauth2(token)
               .body(ingredients)
               .post(Constants.API_POST_CREATING_AN_ORDER)
               .then();
   }

   @Step("Создание заказа неавторизованным пользователем")
    public ValidatableResponse creatingAnOrderUnauthorizedUserTest(Integer count) {
       Ingredients ingredients = new Ingredients();
       ingredients.setIngredients(getIngredientsJson(count));

       return requestSpecification()
               .body(ingredients)
               .post(Constants.API_POST_CREATING_AN_ORDER)
               .then();
   }

   @Step("использование невалидного хеша ингредиента")
    public ValidatableResponse invalidHashSteps(String token) {
       Ingredients ingredients = new Ingredients();
       ingredients.setIngredients(new ArrayList<>(List.of("0011")));
       return requestSpecification()
               .auth().oauth2(token)
               .body(ingredients)
               .post(Constants.API_POST_CREATING_AN_ORDER)
               .then();
   }

   @Step("Получение заказов авторизованного пользователя")
    public ValidatableResponse getOrderAuthorizedUserTest(String token) {
       return requestSpecification()
               .auth()
               .oauth2(token)
               .get(Constants.API_GET_ORDERS)
               .then();
   }

    @Step("Получение заказов неавторизованного пользователя")
    public ValidatableResponse getOrderUnauthorizedUserTest() {
        return requestSpecification()
                .get(Constants.API_GET_ORDERS)
                .then();
    }

}
