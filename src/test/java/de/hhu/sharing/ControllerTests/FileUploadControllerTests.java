package de.hhu.sharing.ControllerTests;

import java.nio.charset.Charset;
import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import de.hhu.sharing.data.ImageRepository;
import de.hhu.sharing.data.ItemRepository;
import de.hhu.sharing.data.UserRepository;
import de.hhu.sharing.model.Address;
import de.hhu.sharing.model.Image;
import de.hhu.sharing.model.Item;
import de.hhu.sharing.model.User;
import de.hhu.sharing.services.FileSystemStorageService;
import de.hhu.sharing.services.ItemService;
import de.hhu.sharing.services.RequestService;
import de.hhu.sharing.services.UserService;
import de.hhu.sharing.storage.StorageService;
import de.hhu.sharing.web.FileUploadController;

@RunWith(SpringRunner.class)
@WebMvcTest(FileUploadController.class)
public class FileUploadControllerTests {

	@Autowired
	MockMvc mvc;

    @MockBean
    FileSystemStorageService fileStorageService;
    @MockBean
    ItemService itemService;
    @MockBean
    UserService userService;
    @MockBean
    ItemRepository itemRepo;
    @MockBean
    UserRepository userRepo;
    @MockBean
    ImageRepository imageRepo;
    
    private User generateUser(String username) {
        LocalDate birthdate = LocalDate.of(2000,1,1);
        Address address = new Address("unistrase","duesseldorf", 40233);
        User user = new User(username,"password", "role", "lastname", "forename", "email", birthdate, address);
        return user;
    }
    
    private Item generateItem(User user) {
        return new Item("apfel", "lecker", 1, 1, user);
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
		Item item = generateItem(user);
		Image image = new Image();
		image.setImageData(jsonFile.getBytes());
		image.setMimeType("image/gif");
		item.setImage(image);
        Mockito.when(itemService.get(1L)).thenReturn(item);
        mvc.perform(MockMvcRequestBuilders.get("/getItemPic?id=1"))
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
		Item item = new Item();
        Mockito.when(itemService.get(1L)).thenReturn(item);
        mvc.perform(MockMvcRequestBuilders.get("/getItemPic?id=1"))
        .andExpect(MockMvcResultMatchers.status().is(400));
	}
	
}