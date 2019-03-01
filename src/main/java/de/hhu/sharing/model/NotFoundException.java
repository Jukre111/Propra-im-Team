package de.hhu.sharing.model;

import org.omg.CosNaming.NamingContextPackage.NotFound;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
