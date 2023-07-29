package ru.easydonate.easypayments.database.credentials;

import com.j256.ormlite.support.ConnectionSource;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import ru.easydonate.easypayments.database.DatabaseType;
import ru.easydonate.easypayments.database.credentials.local.LocalDatabaseCredentials;
import ru.easydonate.easypayments.database.credentials.remote.RemoteDatabaseCredentials;
import ua.rozipp.core.config.RConfig;
import ua.rozipp.core.exception.DatabaseException;

import java.sql.SQLException;

public interface DatabaseCredentials {

    static @NotNull DatabaseCredentials parse(
            @NotNull Plugin plugin,
            @NotNull RConfig config,
            @NotNull DatabaseType databaseType
    ) throws DatabaseException {
        return DatabaseCredentialsParser.parse(plugin, config, databaseType);
    }

    @NotNull DatabaseType getDatabaseType();

    @NotNull String getConnectionUrl();

    @NotNull ConnectionSource createConnectionSource() throws SQLException;

    void loadDriver(@NotNull Plugin plugin) throws DatabaseException;

    default boolean isAuthRequired() {
        return this instanceof AuthDatabaseCredentials;
    }

    default boolean isLocalDatabase() {
        return this instanceof LocalDatabaseCredentials;
    }

    default boolean isRemoteDatabase() {
        return this instanceof RemoteDatabaseCredentials;
    }

}
