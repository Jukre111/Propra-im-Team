package de.hhu.sharing.services;

import java.io.IOException;
import de.hhu.sharing.data.ImageRepository;
import de.hhu.sharing.data.LendableItemRepository;
import de.hhu.sharing.data.SellableItemRepository;
import de.hhu.sharing.data.UserRepository;
import de.hhu.sharing.model.Image;
import de.hhu.sharing.model.LendableItem;
import de.hhu.sharing.model.SellableItem;
import de.hhu.sharing.model.User;
import de.hhu.sharing.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileSystemStorageService implements StorageService {

    @Autowired
    private ImageRepository imageRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private LendableItemRepository lendableItemRepository;

    @Autowired
    private SellableItemRepository sellableItemRepository;


    public Image createImageVars(String mimetype) {
        Image image = new Image();
        switch(mimetype){
            case "image/jpeg":
                image.setMimeType("image/jpeg");
                break;
            case "image/png":
                image.setMimeType("image/png");
                break;
            case "image/gif":
                image.setMimeType("image/gif");
                break;
            case "image/bmp":
                image.setMimeType("image/bmp");
                break;
            default:
        }
        return image;
    }

    private byte[] readFile(MultipartFile file){
        byte[] byteArr = new byte[0];
        try {
            byteArr = file.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArr;
    }

    @Override
    public void storeUser(MultipartFile file, User user){
        byte[] byteArr = readFile(file);
        Image image = null;
        String contentType = null;
        contentType = file.getContentType();
        if(contentType!=null)
            image = createImageVars(contentType);
        else
            image = createImageVars("default");
        image.setImageData(byteArr);
        imageRepo.save(image);
        user.setImage(image);
        userRepo.save(user);
    }

    @Override
    public void storeLendableItem(MultipartFile file, LendableItem lendableItem){
        byte[] byteArr = readFile(file);
        Image image = null;
        String contentType = null;
        contentType = file.getContentType();
        if(contentType!=null)
            image = createImageVars(contentType);
        else
            image = createImageVars("default");
        image.setImageData(byteArr);
        imageRepo.save(image);
        lendableItem.setImage(image);
        lendableItemRepository.save(lendableItem);
    }

    @Override
    public void storeSellableItem(MultipartFile file, SellableItem sellableItem){
        byte[] byteArr = readFile(file);
        Image image = null;
        String contentType = null;
        contentType = file.getContentType();
        if(contentType!=null)
            image = createImageVars(contentType);
        else
            image = createImageVars("default");
        image.setImageData(byteArr);
        imageRepo.save(image);
        sellableItem.setImage(image);
        sellableItemRepository.save(sellableItem);
    }
}