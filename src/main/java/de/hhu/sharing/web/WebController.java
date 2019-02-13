package de.hhu.sharing.web;

import de.hhu.sharing.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class WebController {

    @Autowired
    private UserRepository persons;

    @GetMapping("/")
    public String user(Model m, Principal p) {
        m.addAttribute("username", p.getName());
        System.out.println(p.getName());
        return "index";
    }

}
