package service;

import dataaccess.DataAccessException;

public class AlreadyTakenException extends DataAccessException {
    public AlreadyTakenException(String message) {
        super(message, 403);
    }

//    public int code() {
//        return 403;
//    }
}
