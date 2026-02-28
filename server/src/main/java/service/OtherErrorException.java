package service;

import dataaccess.DataAccessException;

public class OtherErrorException extends DataAccessException {
    public OtherErrorException(String message) {
        super(message, 500);
    }
}
