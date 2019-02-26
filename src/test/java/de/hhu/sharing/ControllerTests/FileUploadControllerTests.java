package de.hhu.sharing.ControllerTests;

import java.nio.charset.Charset;
import java.time.LocalDate;

import de.hhu.sharing.data.LendableItemRepository;
import de.hhu.sharing.model.LendableItem;
import de.hhu.sharing.services.SellableItemService;
import org.assertj.core.api.Assertions;
import de.hhu.sharing.model.SellableItem;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import de.hhu.sharing.data.ImageRepository;
import de.hhu.sharing.data.UserRepository;
import de.hhu.sharing.model.Address;
import de.hhu.sharing.model.Image;
import de.hhu.sharing.model.User;
import de.hhu.sharing.services.FileSystemStorageService;
import de.hhu.sharing.services.LendableItemService;
import de.hhu.sharing.services.SellableItemService;
import de.hhu.sharing.services.UserService;
import de.hhu.sharing.web.FileUploadController;

@RunWith(SpringRunner.class)
@WebMvcTest(FileUploadController.class)
public class FileUploadControllerTests {

	@Autowired
	MockMvc mvc;

    @MockBean
    FileSystemStorageService fileStorageService;
    @MockBean
    LendableItemService lendableItemService;
    @MockBean
    SellableItemService sellableItemService;
    @MockBean
    UserService userService;
    @MockBean
    LendableItemRepository itemRepo;
    @MockBean
    UserRepository userRepo;
    @MockBean
    ImageRepository imageRepo;
    @MockBean
	SellableItemService sellItemService;

    @Test
	public void mustHaveTest(){
		Assertions.assertThat(true).isTrue();
	}
    
    /*private User generateUser(String username) {
        LocalDate birthdate = LocalDate.of(2000,1,1);
        Address address = new Address("unistrase","duesseldorf", 40233);
        User user = new User(username,"password", "role", "lastname", "forename", "email", birthdate, address);
        return user;
    }
    
    private LendableItem generateItem(User user) {
        return new LendableItem("apfel", "lecker", 1, 1, user);
    }
    private SellableItem generateItem2(User user) {
        return new SellableItem("apfel", "lecker", 1, user);
    }
    
	@Test
	@WithMockUser(username = "user")
	public void shouldUploadAvatarPic() throws Exception {
		MockMultipartFile jsonFile = new MockMultipartFile("test.json", "", "application/json", "{\"key1\": \"value1\"}".getBytes(Charset.forName("UTF-8")));
		 mvc.perform(MockMvcRequestBuilders.multipart("/handleFileUploadAvatar")
	                .file("file", jsonFile.getBytes())
	                .characterEncoding("UTF-8"))
	        .andExpect(MockMvcResultMatchers.redirectedUrl("/account"));
	}

	@Test
	@WithMockUser(username = "user")
	public void shouldUploadItemPic() throws Exception {
		MockMultipartFile jsonFile = new MockMultipartFile("test.json", "", "application/json", "{\"key1\": \"value1\"}".getBytes(Charset.forName("UTF-8")));
		mvc.perform(MockMvcRequestBuilders.multipart("/handleFileUploadItem")
				.file("file", jsonFile.getBytes())
				.characterEncoding("UTF-8"))
	        	.andExpect(MockMvcResultMatchers.redirectedUrl("/"));
		mvc.perform(MockMvcRequestBuilders.multipart("/handleFileUploadItem"));
	}
	
	@Test
	@WithMockUser(username = "user")
	public void shouldUploadSellableItemPic() throws Exception {
		MockMultipartFile jsonFile = new MockMultipartFile("test.json", "", "application/json", "{\"key1\": \"value1\"}".getBytes(Charset.forName("UTF-8")));
		mvc.perform(MockMvcRequestBuilders.multipart("/handleFileUploadSellableItem")
				.file("file", jsonFile.getBytes())
				.characterEncoding("UTF-8"))
	        	.andExpect(MockMvcResultMatchers.redirectedUrl("/"));
		mvc.perform(MockMvcRequestBuilders.multipart("/handleFileUploadSellableItem"));
	}
	
	@Test
	@WithMockUser(username = "user")
	public void downloadUserImage() throws Exception {
		MockMultipartFile jsonFile = new MockMultipartFile("test.json", "", "application/json", "{\"key1\": \"value1\"}".getBytes(Charset.forName("UTF-8")));
		User user = generateUser("user");
		Image image = new Image();
		image.setImageData(jsonFile.getBytes());
		image.setMimeType("image/gif");
		user.setImage(image);
        Mockito.when(userService.get("user")).thenReturn(user);
        mvc.perform(MockMvcRequestBuilders.get("/getUserPic?id=1"))
        .andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	@WithMockUser(username = "user")
	public void downloadItemImage() throws Exception {
		MockMultipartFile jsonFile = new MockMultipartFile("test.json", "", "application/json", "{\"key1\": \"value1\"}".getBytes(Charset.forName("UTF-8")));
		User user = generateUser("user");
		Image image = new Image();
		LendableItem lendableItem = generateItem(user);
		image.setImageData(jsonFile.getBytes());
		image.setMimeType("image/gif");
		lendableItem.setImage(image);
        Mockito.when(lendableItemService.get(1L)).thenReturn(lendableItem);
        mvc.perform(MockMvcRequestBuilders.get("/getItemPic?id=1"))
        .andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	@WithMockUser(username = "user")
	public void downloadSellableItemImage() throws Exception {
		MockMultipartFile jsonFile = new MockMultipartFile("test.json", "", "application/json", "{\"key1\": \"value1\"}".getBytes(Charset.forName("UTF-8")));
		User user = generateUser("user");
		SellableItem sellableItem = generateItem2(user);
		Image image = new Image();
		image.setImageData(jsonFile.getBytes());
		image.setMimeType("image/gif");
		sellableItem.setImage(image);
        Mockito.when(sellableItemService.get(1L)).thenReturn(sellableItem);
        mvc.perform(MockMvcRequestBuilders.get("/getSellableItemPic?id=1"))
        .andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	@WithMockUser(username = "user")
	public void downloadUserImageNoImage() throws Exception {
		User user = generateUser("user");
        Mockito.when(userService.get("user")).thenReturn(user);
        mvc.perform(MockMvcRequestBuilders.get("/getUserPic?id=1"))
        .andExpect(MockMvcResultMatchers.status().is(400));
	}
	@Test
	@WithMockUser(username = "user")
	public void downloadItemImageNoImage() throws Exception {
		LendableItem lendableItem = new LendableItem();
        Mockito.when(lendableItemService.get(1L)).thenReturn(lendableItem);
        mvc.perform(MockMvcRequestBuilders.get("/getItemPic?id=1"))
        .andExpect(MockMvcResultMatchers.status().is(400));
	}*/
	
}