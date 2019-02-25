package de.hhu.sharing.ControllerTests;

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
import de.hhu.sharing.model.User;
import de.hhu.sharing.services.FileSystemStorageService;
import de.hhu.sharing.services.ItemService;
import de.hhu.sharing.services.RequestService;
import de.hhu.sharing.services.UserService;
import de.hhu.sharing.storage.StorageService;
import de.hhu.sharing.web.FileUploadController;
import de.hhu.sharing.web.IndexController;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.any;

import java.time.LocalDate;

@RunWith(SpringRunner.class)
@WebMvcTest(FileUploadController.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FileUploadControllerTest {
		
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
	
	@Test
	@WithMockUser(username = "user")
	public void shouldUploadAvatarPic() throws Exception {
		MockMultipartFile jsonFile = new MockMultipartFile("test.json", "", "application/json", "{\"key1\": \"value1\"}".getBytes());
		 mvc.perform(MockMvcRequestBuilders.multipart("/handleFileUploadAvatar")
	                .file("file", jsonFile.getBytes())
	                .characterEncoding("UTF-8"))
	        .andExpect(MockMvcResultMatchers.redirectedUrl("/account"));
	}
	
	@Test
	@WithMockUser(username = "user")
	public void shouldUploadItemPic() throws Exception {
		MockMultipartFile jsonFile = new MockMultipartFile("test.json", "", "application/json", "{\"key1\": \"value1\"}".getBytes());
		 mvc.perform(MockMvcRequestBuilders.multipart("/handleFileUploadItem")
	                .file("file", jsonFile.getBytes())
	                .characterEncoding("UTF-8"))
	        .andExpect(MockMvcResultMatchers.redirectedUrl("/"));
		 mvc.perform(MockMvcRequestBuilders.multipart("/handleFileUploadItem"));
	}
	/*
	@Test
	public void shouldDownloadFile() throws Exception {
		ClassPathResource resource = new ClassPathResource("testupload.txt", getClass());
		given(this.storageService.loadAsResource("testupload.txt")).willReturn(resource);

		ResponseEntity<String> response = this.restTemplate
				.getForEntity("/files/{filename}", String.class, "testupload.txt");

		assertThat(response.getStatusCodeValue()).isEqualTo(200);
		assertThat(response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION))
				.isEqualTo("attachment; filename=\"testupload.txt\"");
		assertThat(response.getBody()).isEqualTo("Spring Framework");
	}*/

}
