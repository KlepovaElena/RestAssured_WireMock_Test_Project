package data;

import net.datafaker.Faker;

public class GenerateData {
    Faker faker = new Faker();

    public int generateUserId() {
        return faker.number().randomDigitNotZero();
    }

    public String generateUserName() {
        return faker.name().name();
    }

    public String generateUserPassword() {
        return faker.internet().domainWord();
    }

    public String generateUserEmail() {
        return faker.internet().emailAddress();
    }
}
