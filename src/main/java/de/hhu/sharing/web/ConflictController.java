package de.hhu.sharing.web;

import de.hhu.sharing.data.ConflictRepository;
import de.hhu.sharing.data.UserRepository;
import de.hhu.sharing.model.Conflict;
import de.hhu.sharing.model.Item;
import de.hhu.sharing.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import de.hhu.sharing.data.ItemRepository;
import java.security.Principal;
import java.util.List;


@Controller
public class ConflictController {

    @Autowired
    private ItemRepository items;

    @Autowired
    private UserRepository users;

    @Autowired
    private ConflictRepository conflicts;


    @GetMapping("/conflict")
    public String conflictPage(Model model, Principal p, @RequestParam("id") Long id){
        final Item item = this.items.findById(id)
                .orElseThrow(
                        () -> new RuntimeException("Item not found!"));
        final User user = this.users.findByUsername(p.getName())
                .orElseThrow(
                        () -> new RuntimeException("User not found!"));

        model.addAttribute("item", item);
        model.addAttribute("user", user);

        return "conflict";
    }

    @PostMapping("/saveConflict")
    public String saveConflict(Long id, String accused, String problem, Principal p){
        final User user = this.users.findByUsername(p.getName()).orElseThrow(()-> new RuntimeException("User not found!"));
        final User accusedUser = this.users.findByUsername(accused).orElseThrow(()-> new RuntimeException("User not found!"));
        final Item item = this.items.findById(id).orElseThrow(()-> new RuntimeException("Item not found!"));
        Conflict conflict = new Conflict();
        conflict.setProsecuter(user);
        conflict.setAccused(accusedUser);
        conflict.setProblem(problem);
        conflict.setItem(item);
        conflicts.save(conflict);


        return "redirect:/";
    }

    @GetMapping("/conflictView")
    public String conflictView(Model model){
        List<Conflict> allConflicts = conflicts.findAll();
        model.addAttribute("allConflicts", allConflicts);
        return "conflictView";
    }
}
