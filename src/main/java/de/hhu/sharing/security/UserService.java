package de.hhu.sharing.security;

import de.hhu.sharing.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserProvider provider;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        Optional<User> user = provider.findByUsername(username);
        if(user.isPresent()){
            User u = user.get();
            UserDetails userdetails = org.springframework.security.core.userdetails.User.builder()
                    .username(u.getUsername()).password(u.getPassword()).authorities(u.getRole()).build();
            return userdetails;
        }
        throw new UsernameNotFoundException("Invalid Username");

    }

}
