package de.hhu.sharing.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import de.hhu.sharing.model.Item;
import de.hhu.sharing.model.User;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

	void storeUser(MultipartFile file, User user);
	
	void storeItem(MultipartFile file, Item item);

	void storeUserInitalizer(byte[] byteArr, User user);
}