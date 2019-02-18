package de.hhu.sharing;

import de.hhu.sharing.data.ItemRepository;
import de.hhu.sharing.data.RequestRepository;
import de.hhu.sharing.data.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;


@RunWith(SpringRunner.class)
@WebMvcTest
public class IndexControllerTest {

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

    @WithMockUser
    @Test
    public void retrieveStatusIndex()throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @WithMockUser
    @Test(expected = NestedServletException.class)
    public void retrieveStatusAccount()throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/account"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }



    @WithMockUser
    @Test
    public void retrieveStatusSearch()throws Exception{

        mvc.perform(MockMvcRequestBuilders.get("/search?query=item"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}
