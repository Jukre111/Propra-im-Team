package de.hhu.sharing.data;

import de.hhu.sharing.model.Image;
import de.hhu.sharing.model.User;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ImageRepository extends CrudRepository <Image, Long> {

    List <Image> findAll();
}
