package service;

import dataaccess.DataAccessException;

public class UnauthorizedException extends DataAccessException {

    public UnauthorizedException(String message) {
        super(message, 401);
    }

}
