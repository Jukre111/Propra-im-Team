package de.hhu.sharing.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import de.hhu.sharing.model.Item;
import de.hhu.sharing.model.User;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

    void init();

    Stream<Path> loadAll();

    Path load(String filename);

    Resource loadAsResource(String filename);

    void deleteAll();

	void storeUser(MultipartFile file, User user);
	
	void storeItem(MultipartFile file, Item item);
    
}