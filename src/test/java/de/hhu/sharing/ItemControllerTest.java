package de.hhu.sharing;

import de.hhu.sharing.data.ItemRepository;
import de.hhu.sharing.data.RequestRepository;
import de.hhu.sharing.data.UserRepository;
import de.hhu.sharing.model.Address;
import de.hhu.sharing.model.Item;
import de.hhu.sharing.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.springSecurity;


@RunWith(SpringRunner.class)
@WebMvcTest
public class ItemControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    WebApplicationContext webContext;

    @MockBean
    ItemRepository itemRepository;

    @MockBean
    UserRepository userRepository;

    @MockBean
    RequestRepository requestRepository;

   /* @Test
    public void retrieveStatusHome()throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }*/

   /*@Before
   public void setup(){
       mvc = MockMvcBuilders.webAppContextSetup(webContext).apply(springSecurity()).build();
   }
*/

   @Test
    public void retrieveStatusDetails() throws Exception{
        Item item = new Item();
        item.setId((long)1);
        User user = new User();
        Address address = new Address();
        user.setAddress(address);
        item.setLender(user);
        Mockito.when(itemRepository.findById((long)1)).thenReturn(Optional.of(item));
        mvc.perform(MockMvcRequestBuilders.get("/details").param("id","1"))
                .andExpect(MockMvcResultMatchers.status().is(302));
    }

}
