package de.hhu.sharing.web;

import de.hhu.sharing.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SharingController {

    @Autowired
    private UserRepository persons;

    @GetMapping("/")
    public String startpage() {

        return "index";
    }

}
