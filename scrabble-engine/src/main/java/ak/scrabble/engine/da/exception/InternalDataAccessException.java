package ak.scrabble.engine.da.exception;

import org.springframework.dao.DataAccessException;

/**
 * Created by akopylov on 05/07/16.
 */
public class InternalDataAccessException extends DataAccessException {
    public InternalDataAccessException(String msg) {
        super(msg);
    }

    public InternalDataAccessException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
