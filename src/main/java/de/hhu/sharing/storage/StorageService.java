package de.hhu.sharing.storage;

import org.springframework.web.multipart.MultipartFile;

import de.hhu.sharing.model.lendableItem;
import de.hhu.sharing.model.User;

public interface StorageService {

	void storeUser(MultipartFile file, User user);
	
	void storeItem(MultipartFile file, lendableItem lendableItem);

	void storeUserInitalizer(byte[] byteArr, User user);

	void storeItemInitalizer(byte[] byteArr, lendableItem lendableItem);
}