package de.hhu.sharing.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Item nicht gefunden")
public class ItemNotFound extends RuntimeException {

}
