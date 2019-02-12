package de.hhu.sharing.web;

import de.hhu.sharing.data.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SharingController {

    UserRepository users;

    @GetMapping("/")
    public String startpage() {

        return "startpage";
    }


}
