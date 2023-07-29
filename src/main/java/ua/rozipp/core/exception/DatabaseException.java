package ua.rozipp.core.exception;

import ru.easydonate.easypayments.database.DatabaseType;

/**
 * Created on 16-1-2.
 */
public class DatabaseException extends Exception {

    private static final String MESSAGE_PATTERN = "Driver for the database type '%s' was not found!";

    private final DatabaseType databaseType;

    public DatabaseException(DatabaseType databaseType) {
        this(String.format(MESSAGE_PATTERN, databaseType.getName()), databaseType);
    }

    public DatabaseException(String message, DatabaseType databaseType) {
        this(message, null, databaseType);
    }

    public DatabaseException(Throwable cause, DatabaseType databaseType) {
        this(String.format(MESSAGE_PATTERN, databaseType.getName()), cause, databaseType);
    }

    public DatabaseException(String message, Throwable cause, DatabaseType databaseType) {
        super(message, cause, false, false);
        this.databaseType = databaseType;
    }
}
