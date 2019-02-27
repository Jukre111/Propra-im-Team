package de.hhu.sharing.web;

import java.io.ByteArrayInputStream;
import java.security.Principal;

import de.hhu.sharing.data.ImageRepository;
import de.hhu.sharing.model.LendableItem;
import de.hhu.sharing.model.SellableItem;
import de.hhu.sharing.model.User;
import de.hhu.sharing.services.SellableItemService;
import de.hhu.sharing.services.LendableItemService;
import de.hhu.sharing.services.UserService;
import de.hhu.sharing.storage.StorageFileNotFoundException;
import de.hhu.sharing.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class FileUploadController {

    private final StorageService storageService;
    
    @Autowired
    private UserService userService;    
    
    @Autowired
    private LendableItemService lendableItemService;

    @Autowired
    private SellableItemService sellableItemService;
    
    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
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
    public String handleFileUploadAvatar(@RequestParam("file") MultipartFile file,
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
        LendableItem lendableItem = lendableItemService.get(id);
    	if(lendableItem.getImage()==null) {
        	return ResponseEntity.badRequest().build();
        }else{
        	return ResponseEntity.ok()
                .contentLength(lendableItem.getImage().getImageData().length)
                .contentType(MediaType.parseMediaType(lendableItem.getImage().getMimeType()))
                .body(new InputStreamResource(new ByteArrayInputStream(lendableItem.getImage().getImageData())));
        }
    }

    @PostMapping("/handleFileUploadItem")
    public String handleFileUploadItem(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes, LendableItem lendableItem) {
        storageService.storeLendableItem(file, lendableItem);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");
        return "redirect:/";
    }

    @RequestMapping(value = "getSellableItemPic", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<InputStreamResource> downloadSellableItemImage(@RequestParam("id") Long id) {
        SellableItem sellableItem = sellableItemService.get(id);
        if(sellableItem.getImage()==null) {
            return ResponseEntity.badRequest().build();
        }else{
            return ResponseEntity.ok()
                    .contentLength(sellableItem.getImage().getImageData().length)
                    .contentType(MediaType.parseMediaType(sellableItem.getImage().getMimeType()))
                    .body(new InputStreamResource(new ByteArrayInputStream(sellableItem.getImage().getImageData())));
        }
    }

    @PostMapping("/handleFileUploadSellableItem")
    public String handleFileUploadSellableItem(@RequestParam("file") MultipartFile file,
                                       RedirectAttributes redirectAttributes, SellableItem sellableItem) {
        storageService.storeSellableItem(file, sellableItem);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");
        return "redirect:/";
    }


    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}