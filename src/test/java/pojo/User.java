package pojo;

import lombok.Getter;
import lombok.Setter;
import net.datafaker.Faker;

@Getter @Setter
public class User {

    Faker faker = new Faker();
    private String name;
    private String password;
    private String email;

    public User(String name, String password, String email) {
        this.name = name;
        this.password = password;
        this.email = email;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
