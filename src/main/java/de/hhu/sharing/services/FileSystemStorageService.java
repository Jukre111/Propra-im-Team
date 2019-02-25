package de.hhu.sharing.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import de.hhu.sharing.data.ImageRepository;
import de.hhu.sharing.data.ItemRepository;
import de.hhu.sharing.data.UserRepository;
import de.hhu.sharing.model.Image;
import de.hhu.sharing.model.lendableItem;
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
    private ItemRepository itemRepo;

    
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
            System.out.println("No valid data");
        }
    	return image;
    }

    private byte[] readFile(MultipartFile file){
        byte[] byteArr = new byte[0];
        try {
            byteArr = file.getBytes();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            InputStream inputStream = new ByteArrayInputStream(byteArr);
            int eof = inputStream.read(byteArr);
            if(eof==byteArr.length){
                inputStream.close();
            }else{
                inputStream.close();
                throw new Exception();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return byteArr;
    }

    @Override
    public void storeUserInitalizer(byte[] byteArr, User user){
        byte[] bytes = byteArr;
        Image image = new Image();
        String contentType = "image/gif";
        image.setMimeType(contentType);
        image.setImageData(bytes);
        imageRepo.save(image);
        user.setImage(image);
        userRepo.save(user);
    }

    @Override
    public void storeItemInitalizer(byte[] byteArr, lendableItem lendableItem) {
        byte[] bytes = byteArr;
        Image image = new Image();
        String contentType = "image/gif";
        image.setMimeType(contentType);
        image.setImageData(bytes);
        imageRepo.save(image);
        lendableItem.setImage(image);
        itemRepo.save(lendableItem);
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
    public void storeItem(MultipartFile file, lendableItem lendableItem){
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
        itemRepo.save(lendableItem);
    }
}