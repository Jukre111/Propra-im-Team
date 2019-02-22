package de.hhu.sharing.web;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import de.hhu.sharing.data.ImageRepository;
import de.hhu.sharing.model.Image;
import de.hhu.sharing.model.Item;
import de.hhu.sharing.model.User;
import de.hhu.sharing.services.ItemService;
import de.hhu.sharing.services.UserService;
import de.hhu.sharing.storage.StorageFileNotFoundException;
import de.hhu.sharing.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import de.hhu.sharing.storage.StorageFileNotFoundException;
import de.hhu.sharing.storage.StorageService;

@Controller
public class FileUploadController {

    private final StorageService storageService;
    
    @Autowired
    private UserService userService;    
    
    @Autowired
    private ItemService itemService;
    
    @Autowired
    private ImageRepository imageRepo;
    
    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/uploadUserPic")
    public String uploadUserPic(Model model) throws IOException {
        return "uploadUserPic";
    }
    
    @GetMapping("/uploadItemPic")
    public String uploadItemPic(Model model) throws IOException {
        return "uploadItemPic";
    }
    
    @RequestMapping(value = "getUserPic", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<InputStreamResource> downloadUserAvatarImage(Principal p) {
        User user = userService.get(p.getName());
        if(user.getImage()==null) {
        	return ResponseEntity.badRequest().build();
        }else{
        	return ResponseEntity.ok()
                .contentLength(user.getImage().getImageData().length)
                .contentType(MediaType.parseMediaType(user.getImage().getMimeType()))
                .body(new InputStreamResource(new ByteArrayInputStream(user.getImage().getImageData())));
        }
    }

    @PostMapping("/handleFileUploadAvatar")
    public String handleFileUploadAvatar( MultipartFile file,
                                   RedirectAttributes redirectAttributes, Principal p) {
    	User user = userService.get(p.getName());
        storageService.storeUser(file, user);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");
        return "redirect:/account";
    }
    
    @RequestMapping(value = "getItemPic", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<InputStreamResource> downloadItemImage(@RequestParam("id") Long id) {
        Item item = itemService.get(id);
    	if(item.getImage()==null) {
        	return ResponseEntity.badRequest().build();
        }else{
        	return ResponseEntity.ok()
                .contentLength(item.getImage().getImageData().length)
                .contentType(MediaType.parseMediaType(item.getImage().getMimeType()))
                .body(new InputStreamResource(new ByteArrayInputStream(item.getImage().getImageData())));
        }
    }

    @PostMapping("/handleFileUploadItem")
    public String handleFileUploadItem(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes, Item item) {
        storageService.storeItem(file, item);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");
        return "redirect:/";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}