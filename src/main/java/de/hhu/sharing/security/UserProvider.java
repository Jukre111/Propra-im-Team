package de.hhu.sharing.security;

import de.hhu.sharing.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProvider extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
