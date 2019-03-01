package de.hhu.sharing.data;

import com.github.javafaker.Faker;
import de.hhu.sharing.model.*;

import de.hhu.sharing.services.ProPayService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StreamUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import java.io.*;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class DatabaseInitializer implements ServletContextInitializer {

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserRepository users;

    @Autowired
    private LendableItemRepository lendableItems;

    @Autowired
    private RequestRepository requests;

    @Autowired
    private SellableItemRepository sellableItems;

    @Autowired
    private ImageRepository images;
    
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException{
        if(users.findAll().isEmpty()) {
            final Faker faker = new Faker(Locale.GERMAN);
            initUsers(faker);
            initLendableItems(faker);
            initRequests(faker);
            initSellableItems(faker);
            initAdmin(faker);
        }
    }
    
    private void initUsers(Faker faker){
        Image image = initImage();
        for(int i = 1; i < 21; i++){
            Address address = new Address(
                    faker.address().streetAddress(),
                    faker.lordOfTheRings().location(),
                    Integer.parseInt(faker.address().zipCode()));
            User user = new User("user" + i, encoder.encode("password" + i), "ROLE_USER",
                    faker.lordOfTheRings().character(),
                    faker.pokemon().name(),
                    faker.internet().emailAddress(),
                    faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                    address, image);
            users.save(user);
        }
    }

    private void initLendableItems(Faker faker){
        Image image = initImage();
        for(User user : users.findAll()){
            for(int j = 0; j < 3; j++){
                LendableItem lendableItem = new LendableItem(faker.pokemon().name(),
                        String.join("\n", faker.lorem().paragraphs(3)),
                        faker.number().numberBetween(1,1000),
                        faker.number().numberBetween(1,1000),
                        user, image);
                lendableItems.save(lendableItem);
            }
        }
    }

    private void initRequests(Faker faker){
        for(User user : users.findAll()){
            List<LendableItem> lendableItemList = lendableItems.findFirst2ByOwnerNot(user);
            LocalDate startdate = faker.date().future(10, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            Request request1 = new Request(
                    new Period(startdate,
                            faker.date().future(10,TimeUnit.DAYS, Date.from(startdate.atStartOfDay(ZoneId.systemDefault()).toInstant())).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()),
                            user);
            Request request2 = new Request(
                    new Period(startdate,
                            faker.date().future(10,TimeUnit.DAYS, Date.from(startdate.atStartOfDay(ZoneId.systemDefault()).toInstant())).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()),
                    user);
            requests.save(request1);
            requests.save(request2);
            lendableItemList.get(0).addToRequests(request1);
            lendableItemList.get(1).addToRequests(request2);
            lendableItems.saveAll(lendableItemList);
        }
    }

    private void initSellableItems(Faker faker) {
        Image image = initImage();
        for(User user : users.findAll()){
            SellableItem sellableItem = new SellableItem(faker.hacker().abbreviation(),
                    String.join("\n", faker.lorem().paragraphs(3)),
                    faker.number().numberBetween(1,1000),
                    user, image);
            sellableItems.save(sellableItem);
        }
    }

    private void initAdmin(Faker faker) {
        Image image = initImage();
        Address address = new Address(
                faker.address().streetAddress(),
                faker.pokemon().location(),
                Integer.parseInt(faker.address().zipCode()));
        User admin = new User("admin", encoder.encode("admin"), "ROLE_ADMIN",
                faker.gameOfThrones().house(),
                faker.lordOfTheRings().character(),
                faker.internet().emailAddress(),
                faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                address, image);
        users.save(admin);
    }

    private Image initImage(){
        byte[] byteArr = new byte[1];
        try {
        	InputStream in = this.getClass().getResourceAsStream("/nyan_cat.gif");
           byteArr = StreamUtils.copyToByteArray(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Image image = new Image();
        String contentType = "image/gif";
        image.setMimeType(contentType);
        image.setImageData(byteArr);
        images.save(image);
        return image;
    }
}
