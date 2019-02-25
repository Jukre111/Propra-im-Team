package de.hhu.sharing.data;

import com.github.javafaker.Faker;
import de.hhu.sharing.model.*;
import de.hhu.sharing.services.FileSystemStorageService;

import de.hhu.sharing.services.ProPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletContextInitializer;
//import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
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
        initItems(faker);
        initRequests(faker);
    }

   /* private MultipartFile getImageFile() throws FileNotFoundException {
    	File file = ResourceUtils.getFile(
    			"classpath:nyan_cat.gif");
    	FileInputStream input;
    	MultipartFile multipartFile = null;
		try {
			input = new FileInputStream(file);
			multipartFile = new MockMultipartFile("file",
			file.getName(), "image/gif", input);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return multipartFile;
    }*/
    
    private void initUsers(Faker faker){
    	/*MultipartFile nyanCat = null;
		try {
			nyanCat = getImageFile();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}*/
        for(int i = 1; i < 21; i++){
            Address address = new Address(
                    faker.address().streetAddress(),
                    faker.lordOfTheRings().location(),
                    Integer.parseInt(faker.address().zipCode()));
            User user = new User("user" + i, encoder.encode("password" + i), "ROLE_USER",
                    faker.gameOfThrones().house(),
                    faker.pokemon().name(),
                    faker.internet().emailAddress(),
                    faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                    address);
            users.save(user);
        	//fileService.storeUser(nyanCat, user);
        }
    }

    private void initItems(Faker faker){
        for(User user : users.findAll()){
            for(int j = 0; j < 3; j++){
                Item item = new Item(faker.pokemon().name(),
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
            itemList.get(0).addToRequests(request1);
            itemList.get(1).addToRequests(request2);
            items.saveAll(itemList);
        }


        Address adminAddress = new Address(faker.address().streetAddress(),faker.pokemon().location(), Integer.parseInt(faker.address().zipCode()));
        User admin = new User("admin", encoder.encode("admin") ,"ROLE_ADMIN", faker.gameOfThrones().house(),
                faker.lordOfTheRings().character(), faker.internet().emailAddress(), faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), adminAddress);
        users.save(admin);


//        User user1 = users.findByUsername("user1").orElseThrow(()-> new RuntimeException("Users not there."));
//        User user2 = users.findByUsername("user2").orElseThrow(()-> new RuntimeException("Users not there."));
//        Item item1 = items.findFirstByLender(user1).orElseThrow(()-> new RuntimeException("Item not found."));
//
//        Conflict conflict = new Conflict();
//        conflict.setProblem("Problem");
//        conflict.setBorrower(user2);
//        conflict.setLender(user1);
//        conflicts.save(conflict);

    }

}
