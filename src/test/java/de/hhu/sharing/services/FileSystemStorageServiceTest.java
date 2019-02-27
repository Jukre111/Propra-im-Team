package de.hhu.sharing.services;

import static org.mockito.internal.verification.VerificationModeFactory.times;

import java.nio.charset.Charset;
import java.time.LocalDate;

import de.hhu.sharing.data.LendableItemRepository;
import de.hhu.sharing.model.LendableItem;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import de.hhu.sharing.data.ImageRepository;
import de.hhu.sharing.data.UserRepository;
import de.hhu.sharing.model.Address;
import de.hhu.sharing.model.Image;
import de.hhu.sharing.model.User;
import de.hhu.sharing.services.FileSystemStorageService;

public class FileSystemStorageServiceTest {


    @Mock
    private LendableItemRepository items;
    
    @Mock
    private ImageRepository images;
    
    @Mock
    private UserRepository users;

    @InjectMocks
    private FileSystemStorageService fileStorageService;

    @Before public void initialize(){
        MockitoAnnotations.initMocks(this);
    }
    
    private User generateUser(String username) {
        LocalDate birthdate = LocalDate.of(2000,1,1);
        Address address = new Address("unistrase","duesseldorf", 40233);
        User user = new User(username,"password", "role", "lastname", "forename", "email", birthdate, address);
        return user;
    }

    private LendableItem generateItem(User user) {
        return new LendableItem("apfel", "lecker", 1, 1, user);
    }
    
    @Test
    public void testCreateImageVars(){
    	String mimetype = "image/gif";
    	Image image = fileStorageService.createImageVars(mimetype);
    	Assert.assertEquals(image.getMimeType(), mimetype);
    	mimetype = "image/bmp";
    	image = fileStorageService.createImageVars(mimetype);
    	Assert.assertEquals(image.getMimeType(), mimetype);
    	mimetype = "image/jpeg";
    	image = fileStorageService.createImageVars(mimetype);
    	Assert.assertEquals(image.getMimeType(), mimetype);
    	mimetype = "image/png";
    	image = fileStorageService.createImageVars(mimetype);
    	Assert.assertEquals(image.getMimeType(), mimetype);
    	mimetype = "irgendwas";
    	image = fileStorageService.createImageVars(mimetype);
    	Assert.assertEquals(image.getMimeType(), null);
    }

    @Test
    public void testStoreUser() throws Exception{
    	User user = generateUser("user");
    	MockMultipartFile jsonFile = new MockMultipartFile("test.gif", "", "image/gif", "{\"key1\": \"value1\"}".getBytes(Charset.forName("UTF-8")));         
    	fileStorageService.storeUser(jsonFile, user);
        ArgumentCaptor<Image> captor = ArgumentCaptor.forClass(Image.class);
        Mockito.verify(images, times(1)).save(captor.capture());
        Assert.assertEquals(captor.getAllValues().get(0).getMimeType(), "image/gif");
        Assert.assertEquals(captor.getAllValues().get(0).getImageData().length, jsonFile.getBytes().length);
        ArgumentCaptor<User> captor2 = ArgumentCaptor.forClass(User.class);
        Mockito.verify(users, times(1)).save(captor2.capture());
        Assert.assertEquals(captor2.getAllValues().get(0).getRole(), "role");
        Assert.assertEquals(captor2.getAllValues().get(0).getUsername(), "user");
    }
    
    @Test
    public void testStoreItem() throws Exception{
    	User user = generateUser("user");
    	LendableItem lendableItem = generateItem(user);
    	MockMultipartFile jsonFile = new MockMultipartFile("test.gif", "", "image/gif", "{\"key1\": \"value1\"}".getBytes(Charset.forName("UTF-8")));         
    	fileStorageService.storeLendableItem(jsonFile, lendableItem);
        ArgumentCaptor<Image> captor = ArgumentCaptor.forClass(Image.class);
        Mockito.verify(images, times(1)).save(captor.capture());
        Assert.assertEquals(captor.getAllValues().get(0).getMimeType(), "image/gif");
        Assert.assertEquals(captor.getAllValues().get(0).getImageData().length, jsonFile.getBytes().length);
        ArgumentCaptor<LendableItem> captor2 = ArgumentCaptor.forClass(LendableItem.class);
        Mockito.verify(items, times(1)).save(captor2.capture());
        Assert.assertEquals(captor2.getAllValues().get(0).getName(), "apfel");
        Assert.assertEquals(captor2.getAllValues().get(0).getDescription(), "lecker");
    }
}
