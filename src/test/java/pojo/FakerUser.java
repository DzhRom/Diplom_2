package pojo;

import net.datafaker.Faker;

public class FakerUser {

     Faker faker = new Faker();

    public User fakerUser(){
        return new User (faker.name().firstName(), faker.internet().password(4,6), faker.internet().emailAddress());
    }
}
