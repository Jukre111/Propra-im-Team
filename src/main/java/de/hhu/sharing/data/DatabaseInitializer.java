package de.hhu.sharing.data;

import com.github.javafaker.Faker;
import de.hhu.sharing.model.*;
import de.hhu.sharing.services.FileSystemStorageService;

import de.hhu.sharing.services.ProPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import java.io.*;
import java.nio.file.Files;
import java.time.ZoneId;
import java.util.*;

@Component
public class DatabaseInitializer implements ServletContextInitializer {

    @Autowired
    private UserRepository users;

    @Autowired
    private LendableItemRepository lendableItemRepository;

    @Autowired
    private RequestRepository requests;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private ConflictRepository conflicts;

    @Autowired
    private ProPayService proPayService;

    @Autowired
    ImageRepository imageRepo;
    
    @Autowired
    FileSystemStorageService fileService;
    
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException{
        final Faker faker = new Faker(Locale.GERMAN);
        initUsers(faker);
        initLendableItems(faker);
        //initRequests(faker);
    }

    private byte[] getDefaultUserImage(){
        byte[] byteArr = new byte[1];
        File file = new File("nyan_cat.gif");
        try {
            file = ResourceUtils.getFile(
                    "classpath:nyan_cat.gif");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
		try {
            byteArr = Files.readAllBytes(file.toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return byteArr;
    }
    
    private void initUsers(Faker faker){
        byte[] byteArr = getDefaultUserImage();
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
                    address);
            users.save(user);
        	fileService.storeUserInitalizer(byteArr, user);
        }
    }

    private void initLendableItems(Faker faker){
        byte[] byteArr = getDefaultUserImage();
        for(User user : users.findAll()){
            for(int j = 0; j < 3; j++){
                LendableItem lendableItem = new LendableItem(faker.pokemon().name(),
                        String.join("\n", faker.lorem().paragraphs(3)),
                        faker.number().numberBetween(1,1000),
                        faker.number().numberBetween(1,1000),
                        user);
                lendableItemRepository.save(lendableItem);
                fileService.storeItemInitalizer(byteArr, lendableItem);
            }
        }
    }

    /*private void initRequests(Faker faker){
        for(User user : users.findAll()){
            List<LendableItem> lendableItemList = LendableItemRepository.findFirst2ByOwnerNot(user);
            LocalDate startdate = faker.date().future(10,TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
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
            LendableItemRepository.saveAll(lendableItemList);
        }


        Address adminAddress = new Address(faker.address().streetAddress(),faker.pokemon().location(), Integer.parseInt(faker.address().zipCode()));
        User admin = new User("admin", encoder.encode("admin") ,"ROLE_ADMIN", faker.gameOfThrones().house(),
                faker.lordOfTheRings().character(), faker.internet().emailAddress(), faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), adminAddress);
        users.save(admin);

    }*/

}
