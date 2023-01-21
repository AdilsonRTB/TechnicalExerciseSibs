package cv.pn.sibs.utilities;

import cv.pn.sibs.exceptions.RecordNotFoundException;

import java.util.Optional;

public class APIUtilities {

    public static <T> Optional<T> checkResource(Optional<T> resource, String message) {
        if (!resource.isPresent()) {
            throw new RecordNotFoundException(message);
        }
        return resource;
    }
}
