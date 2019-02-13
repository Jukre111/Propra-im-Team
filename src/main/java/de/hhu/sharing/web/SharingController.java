package de.hhu.sharing.web;

import de.hhu.sharing.data.UserRepository;
import de.hhu.sharing.model.Message;
import de.hhu.sharing.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SharingController {

    @Autowired
    private UserRepository persons;

    @GetMapping("/")
    public String startpage(Model model) {
        return "index";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

}
