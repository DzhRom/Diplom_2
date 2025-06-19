package pojo;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Ingredients {
    private List<String> ingredients;

    public Ingredients() {
        ingredients = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "ingredients: " + ingredients;
    }

}
