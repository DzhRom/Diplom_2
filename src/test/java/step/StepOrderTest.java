package step;

import constants.Constants;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import java.util.ArrayList;

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

   @Step("формирование json c N количеством ингредиентов")
   public String getIngredientsJson(Integer count) {
       response = getIngredients();
       String ingredient = "";
       ArrayList<String> ingredients = response.extract().path("data._id");
       if (count > ingredients.size()) { count = ingredients.size();}
       if (count <= 0) { return "{ \"ingredients\": [] }"; }
           for (int i = 0; i < count; i++) {
               ingredient += " \"" + ingredients.get(i) + "\",";
           }
       ingredient = ingredient.substring(0, ingredient.length() - 1);
       String ingredientJSON = "{ \"ingredients\": [" + ingredient + "] }";
       return ingredientJSON;
   }

   @Step("Создание заказа")
    public ValidatableResponse creatingAnOrderSteps(String token, Integer count) {
       return requestSpecification()
               .auth().oauth2(token)
               .body(getIngredientsJson(count))
               .post(Constants.API_POST_CREATING_AN_ORDER)
               .then();
   }

   @Step("Создание заказа неавторизованным пользователем")
    public ValidatableResponse creatingAnOrderUnauthorizedUserTest(Integer count) {
       return requestSpecification()
               .body(getIngredientsJson(count))
               .post(Constants.API_POST_CREATING_AN_ORDER)
               .then();
   }

   @Step("использование невалидного хеша ингредиента")
    public ValidatableResponse invalidHashSteps(String token) {        ;
       return requestSpecification()
               .auth().oauth2(token)
               .body("{ \"ingredients\": [\"000\"] }")
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
