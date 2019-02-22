package de.hhu.sharing.services;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import de.hhu.sharing.data.ImageRepository;
import de.hhu.sharing.data.ItemRepository;
import de.hhu.sharing.data.UserRepository;
import de.hhu.sharing.model.Image;
import de.hhu.sharing.model.Item;
import de.hhu.sharing.model.User;
import de.hhu.sharing.storage.StorageException;
import de.hhu.sharing.storage.StorageFileNotFoundException;
import de.hhu.sharing.storage.StorageProperties;
import de.hhu.sharing.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;

    @Autowired
    private ImageRepository imageRepo;
    
    @Autowired
    private UserRepository userRepo;   
    
    @Autowired
    private ItemRepository itemRepo;
    
    @Autowired
    public FileSystemStorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }
    
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

    @Override
    public void storeUser(MultipartFile file, User user){
    	byte[] byteArr = new byte[1];
    	String mimetype = file.getContentType();
		try {
			byteArr = file.getBytes();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
        try {
        	InputStream inputStream = new ByteArrayInputStream(byteArr);
            //convert file into array of bytes
            int actualLength = inputStream.read(byteArr);
            if(actualLength==byteArr.length){
                inputStream.close();
            }else{
                inputStream.reset();
                inputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Image image = createImageVars(mimetype);
        image.setImageData(byteArr);
        imageRepo.save(image);
        user.setImage(image);
        userRepo.save(user);
    }
    
    @Override
    public void storeItem(MultipartFile file, Item item){
    	String mimetype = file.getContentType();
    	byte[] byteArr = new byte[1];
		try {
			byteArr = file.getBytes();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
        try {
        	InputStream inputStream = new ByteArrayInputStream(byteArr);
            //convert file into array of byte
            int actualLength = inputStream.read(byteArr);
            if(actualLength==byteArr.length){
                inputStream.close();
            }else{
                inputStream.reset();
                inputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Image image = createImageVars(mimetype);
        image.setImageData(byteArr);
        imageRepo.save(image);
        item.setImage(image);
        itemRepo.save(item);
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        }
        catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }

    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + filename);

            }
        }
        catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        }
        catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
}