package de.hhu.sharing.security;

import de.hhu.sharing.data.UserRepository;
import de.hhu.sharing.model.Address;
import de.hhu.sharing.model.User;
import de.hhu.sharing.services.BorrowingProcessService;
import de.hhu.sharing.services.UserService;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Optional;

import static junit.framework.TestCase.assertTrue;

public class UserLoginServiceTest {

    @Mock
    private UserProvider provider;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserLoginService loginService;

    @Before
    public void initialize(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testLoadUserByUsernameTest (){
        String username = "user";
        LocalDate birthdate = LocalDate.of(2000,1,1);
        Address address = new Address("unistrase","duesseldorf", 40233);
        User user = new User(username,"password", "role", "lastname", "forname", "email",birthdate,address);
        Mockito.when(provider.findByUsername(username)).thenReturn(Optional.of(user));
        UserDetails details = loginService.loadUserByUsername("user");
        Assert.assertEquals(details.getUsername(),user.getUsername());
    }

    @Test
    public void failTestLoadUserByUsernameTest (){
        UsernameNotFoundException e = null;
        String username = "user";
        LocalDate birthdate = LocalDate.of(2000,1,1);
        Address address = new Address("unistrase","duesseldorf", 40233);
        User user = new User(username,"password", "role", "lastname", "forname", "email",birthdate,address);
        Mockito.when(provider.findByUsername(username)).thenReturn(Optional.of(user));
        try {
            loginService.loadUserByUsername("user2");
        } catch (UsernameNotFoundException ex) {
            e = ex;
        }

        assertTrue(e instanceof UsernameNotFoundException);
    }
}
