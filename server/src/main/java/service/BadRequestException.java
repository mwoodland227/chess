package service;

import dataaccess.DataAccessException;

public class BadRequestException extends DataAccessException {
    public BadRequestException(String message) {
        super(message, 400);
    }

//    public int code() {
//        return 400;
//    }
}
