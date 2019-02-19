package de.hhu.sharing;

import de.hhu.sharing.data.ItemRepository;
import de.hhu.sharing.data.RequestRepository;
import de.hhu.sharing.data.UserRepository;
import de.hhu.sharing.security.SecurityConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;

import java.security.Principal;

//@RunWith(SpringRunner.class)
//@WebMvcTest
public class RequestControllerTest{

    @Test
    public void nestedException(){

    }
    /*

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
    @Test(expected = NestedServletException.class)
    public void retrieveStatusRequest()throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/request?id=1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    @WithMockUser(username = "test")
    @Test
    public void retrieveStatusPostRequest()throws Exception{
        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn("test");


        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/request")
                .principal(mockPrincipal)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();
        int status = response.getStatus();
        Assert.assertEquals("response status is wrong", 302, status);
        /*MultiValueMap<String,String> map = new LinkedMultiValueMap<>();
        map.add("startdate","2007-12-03");
        map.add("enddate","2007-12-04");
        map.add("p","test");
        mvc.perform(MockMvcRequestBuilders.post("/request?id=1").params(map))
                .andExpect(MockMvcResultMatchers.status().isOk());
    */
    //}

    /*@WithMockUser
    @Test(expected = NestedServletException.class)
    public void retrieveStatusDeleteRequest()throws Exception{

        mvc.perform(MockMvcRequestBuilders.get("/deleteRequest?id=1"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/account"));
    }*/
}
