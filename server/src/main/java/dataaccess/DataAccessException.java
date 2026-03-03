package dataaccess;

/**
 * Indicates there was an error connecting to the database
 */
public class DataAccessException extends Exception{
    private final int code;
    public DataAccessException(String message, int code) {
        super(message);
        this.code = code;
    }
    public int code(){
        return code;
    }
}
