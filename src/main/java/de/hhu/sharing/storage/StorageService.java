package de.hhu.sharing.storage;

import de.hhu.sharing.model.LendableItem;
import de.hhu.sharing.model.SellableItem;
import org.springframework.web.multipart.MultipartFile;

import de.hhu.sharing.model.User;

public interface StorageService {

	void storeUser(MultipartFile file, User user);
	
	void storeItem(MultipartFile file, LendableItem lendableItem);

	void storeUserInitalizer(byte[] byteArr, User user);

	void storeItemInitalizer(byte[] byteArr, LendableItem lendableItem);

	void storeSellableItem(MultipartFile file, SellableItem sellableItem);


}