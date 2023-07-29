package ru.easydonate.easypayments.database.credentials.local;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import ru.easydonate.easypayments.database.DatabaseType;
import ua.rozipp.core.exception.DatabaseException;

public final class SQLiteDatabaseCredentials extends AbstractLocalDatabaseCredentials {

    public static final String DRIVER_CLASS = "org.sqlite.JDBC";
    public static final String URL_PATTERN = "jdbc:sqlite:%s%s";

    public SQLiteDatabaseCredentials(@NotNull Plugin plugin) {
        super(plugin, DatabaseType.SQLITE);
    }

    @Override
    public @NotNull String getConnectionUrl() {
        return String.format(URL_PATTERN, getFilePath(), formatParameters());
    }

    @Override
    public void loadDriver(@NotNull Plugin plugin) throws DatabaseException {
        checkDriver(plugin, DRIVER_CLASS);
    }

}
