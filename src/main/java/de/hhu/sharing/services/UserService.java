package de.hhu.sharing.services;

import de.hhu.sharing.data.UserRepository;
import de.hhu.sharing.model.Item;
import de.hhu.sharing.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserService {

    @Autowired
    private UserRepository users;

    public User get(String username) {
        User user = this.users.findByUsername(username)
                .orElseThrow(
                        () -> new RuntimeException("User not found!"));
        return user;
    }

    public void addToBorrowedItems(User user, Item item) {
        user.addToBorrowedItems(item);
        users.save(user);
    }
}