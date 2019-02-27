package de.hhu.sharing.services;

import static org.mockito.internal.verification.VerificationModeFactory.noMoreInteractions;
import static org.mockito.internal.verification.VerificationModeFactory.times;

import java.nio.charset.Charset;
import java.time.LocalDate;

import de.hhu.sharing.data.LendableItemRepository;
import de.hhu.sharing.data.SellableItemRepository;
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

public class FileSystemStorageServiceTest {
    
    @Mock
    private ImageRepository images;
    
    @Mock
    private UserRepository users;

    @Mock
    private LendableItemRepository items;

    @Mock
    private SellableItemRepository sellableItemRepository;

    @InjectMocks
    private FileSystemStorageService fileStorageService;

    @Before public void initialize(){
        MockitoAnnotations.initMocks(this);
    }
    
    private User generateUser(String username) {
        LocalDate birthdate = LocalDate.of(2000,1,1);
        Address address = new Address("unistrase","duesseldorf", 40233);
        return new User(username,"password", "role", "lastname", "forename", "email", birthdate, address);
    }

    private LendableItem generateItem(User user) {
        return new LendableItem("apfel", "lecker", 1, 1, user);
    }

    @Test
    public void testCreateImageVarsJpeg() {
        String mimetype = "image/jpeg";
        Image image = fileStorageService.createImageVars(mimetype);
        Assert.assertEquals(image.getMimeType(), mimetype);
    }

    @Test
    public void testCreateImageVarsPng() {
        String mimetype = "image/png";
        Image image = fileStorageService.createImageVars(mimetype);
        Assert.assertEquals(image.getMimeType(), mimetype);
    }

    @Test
    public void testCreateImageVarsGif() {
        String mimetype = "image/gif";
        Image image = fileStorageService.createImageVars(mimetype);
        Assert.assertEquals(image.getMimeType(), mimetype);
    }

    @Test
    public void testCreateImageVarsBmp() {
        String mimetype = "image/bmp";
        Image image = fileStorageService.createImageVars(mimetype);
        Assert.assertEquals(image.getMimeType(), mimetype);
    }

    @Test
    public void testCreateImageVarsDefault() {
        String mimetype = "anyString";
        Image image = fileStorageService.createImageVars(mimetype);
        Assert.assertNull(image.getMimeType());
    }

//
//    @Test
//    public void testStoreItemInitalizer() throws Exception{
//    	User user = generateUser("user");
//    	LendableItem lendableItem = generateItem(user);
//    	MockMultipartFile jsonFile = new MockMultipartFile("test.gif", "", "image/gif", "{\"key1\": \"value1\"}".getBytes(Charset.forName("UTF-8")));
//    	fileStorageService.storeLendableItemInitalizer(jsonFile.getBytes(), lendableItem);
//        ArgumentCaptor<Image> captor = ArgumentCaptor.forClass(Image.class);
//        Mockito.verify(images, times(1)).save(captor.capture());
//        Assert.assertEquals(captor.getAllValues().get(0).getMimeType(), "image/gif");
//        Assert.assertEquals(captor.getAllValues().get(0).getImageData().length, jsonFile.getBytes().length);
//        ArgumentCaptor<LendableItem> captor2 = ArgumentCaptor.forClass(LendableItem.class);
//        Mockito.verify(items, times(1)).save(captor2.capture());
//        Assert.assertEquals(captor2.getAllValues().get(0).getName(), "apfel");
//        Assert.assertEquals(captor2.getAllValues().get(0).getDescription(), "lecker");
//    }
    
    @Test
    public void testStoreUserContentTypeNotNull() throws Exception{
    	User user = generateUser("user");
    	MockMultipartFile jsonFile = new MockMultipartFile("test.gif", "", "image/gif", "{\"key1\": \"value1\"}".getBytes(Charset.forName("UTF-8")));         

    	fileStorageService.storeUser(jsonFile, user);

    	ArgumentCaptor<Image> captor = ArgumentCaptor.forClass(Image.class);
        Mockito.verify(images, times(1)).save(captor.capture());
        ArgumentCaptor<User> captor2 = ArgumentCaptor.forClass(User.class);
        Mockito.verify(users, times(1)).save(captor2.capture());

        Assert.assertEquals(captor.getValue().getMimeType(), "image/gif");
        Assert.assertEquals(captor.getValue().getImageData().length, jsonFile.getBytes().length);
        Assert.assertEquals(captor2.getValue().getRole(), "role");
        Assert.assertEquals(captor2.getValue().getUsername(), "user");
        Assert.assertEquals(captor2.getValue().getImage(), captor.getValue());
    }

    @Test
    public void testStoreUserContentTypeNull() throws Exception{
        User user = generateUser("user");
        MockMultipartFile jsonFile = new MockMultipartFile("test.gif", "", null, "{\"key1\": \"value1\"}".getBytes(Charset.forName("UTF-8")));

        fileStorageService.storeUser(jsonFile, user);

        ArgumentCaptor<Image> captor = ArgumentCaptor.forClass(Image.class);
        Mockito.verify(images, times(1)).save(captor.capture());
        ArgumentCaptor<User> captor2 = ArgumentCaptor.forClass(User.class);
        Mockito.verify(users, times(1)).save(captor2.capture());

        Assert.assertNull(captor.getValue().getMimeType());
        Assert.assertEquals(captor.getValue().getImageData().length, jsonFile.getBytes().length);
        Assert.assertEquals(captor2.getValue().getRole(), "role");
        Assert.assertEquals(captor2.getValue().getUsername(), "user");
        Assert.assertEquals(captor2.getValue().getImage(), captor.getValue());
    }


    @Test
    public void testStoreLendableItemContentTypeNotNull() throws Exception{
    	User user = generateUser("user");
    	LendableItem lendableItem = generateItem(user);
    	MockMultipartFile jsonFile = new MockMultipartFile("test.gif", "", "image/gif", "{\"key1\": \"value1\"}".getBytes(Charset.forName("UTF-8")));

    	fileStorageService.storeLendableItem(jsonFile, lendableItem);

    	ArgumentCaptor<Image> captor = ArgumentCaptor.forClass(Image.class);
        Mockito.verify(images, times(1)).save(captor.capture());
        ArgumentCaptor<LendableItem> captor2 = ArgumentCaptor.forClass(LendableItem.class);
        Mockito.verify(items, times(1)).save(captor2.capture());


        Assert.assertEquals(captor.getValue().getMimeType(), "image/gif");
        Assert.assertEquals(captor.getValue().getImageData().length, jsonFile.getBytes().length);
        Assert.assertEquals(captor2.getValue().getName(), "apfel");
        Assert.assertEquals(captor2.getValue().getDescription(), "lecker");
        Assert.assertEquals(captor2.getValue().getImage(), captor.getValue());
    }
}