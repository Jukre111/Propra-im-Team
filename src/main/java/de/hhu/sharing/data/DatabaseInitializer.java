package de.hhu.sharing.data;

import com.github.javafaker.Faker;
import de.hhu.sharing.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class DatabaseInitializer implements ServletContextInitializer {

    @Autowired
    private UserRepository users;

    @Autowired
    private ItemRepository items;

    @Autowired
    private RequestRepository requests;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private DatabaseInitializerService service;

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException{
        final Faker faker = new Faker(Locale.GERMAN);
        initUsers(faker);
        initItems(faker);
        initRequests(faker);
        service.test();
    }

    private void initUsers(Faker faker){
        for(int i = 1; i < 21; i++){
            Address address = new Address(
                    faker.address().streetAddress(),
                    faker.gameOfThrones().city(),
                    Integer.parseInt(faker.address().zipCode()));
            User user = new User("user" + i, encoder.encode("password" + i), "ROLE_USER",
                    faker.gameOfThrones().house(),
                    faker.pokemon().name(),
                    faker.internet().emailAddress(),
                    faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                    address);
            users.save(user);
        }
    }

    private void initItems(Faker faker){
        for(User user : users.findAll()){
            for(int j = 0; j < 3; j++){
                Item item = new Item(faker.hipster().word(),
                        String.join("\n", faker.lorem().paragraphs(3)),
                        faker.number().numberBetween(1,1000),
                        faker.number().numberBetween(1,1000),
                        user);
                items.save(item);
            }
        }
    }

    private void initRequests(Faker faker){
        for(User user : users.findAll()){
            List<Item> itemList = items.findFirst2ByLenderNot(user);
            Request request1 = new Request(
                    faker.date().past(10,TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                    faker.date().future(10,TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                    user);
            Request request2 = new Request(
                    faker.date().past(10,TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                    faker.date().future(10,TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                    user);
            requests.save(request1);
            requests.save(request2);
            itemList.get(0).addToRequests(request1);
            itemList.get(1).addToRequests(request2);
            items.saveAll(itemList);
        }
    }

}
