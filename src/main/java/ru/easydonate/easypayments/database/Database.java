package ru.easydonate.easypayments.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.field.DataPersister;
import com.j256.ormlite.field.DataPersisterManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import lombok.Getter;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import ru.easydonate.easypayments.database.credentials.DatabaseCredentials;
import ru.easydonate.easypayments.database.credentials.DatabaseCredentialsParser;
import ua.rozipp.core.LogHelper;
import ua.rozipp.core.config.RConfig;
import ua.rozipp.core.database.SQLObject;
import ua.rozipp.core.exception.DatabaseException;
import ua.rozipp.core.exception.InvalidConfiguration;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Getter
public final class Database {

    private final Plugin plugin;
    private final DatabaseCredentials databaseCredentials;
    private final ConnectionSource connectionSource;
    private final ExecutorService asyncExecutorService;
    private final Map<Class<?>, Dao<?, ?>> daoMap = new HashMap<>();
    private final List<Class<?>> registeredClass = new ArrayList<>();

    public Database(@NotNull Plugin plugin, @NotNull RConfig config) throws SQLException, InvalidConfiguration, DatabaseException {
        this(plugin, config, parseDatabaseType(config));
    }

    public Database(@NotNull Plugin plugin, @NotNull RConfig config, @NotNull DatabaseType databaseType) throws InvalidConfiguration, DatabaseException, SQLException {
        this.plugin = plugin;

        RConfig credentialsConfig = config.getRConfig("database." + databaseType.getKey());
        if (credentialsConfig == null)
            throw new InvalidConfiguration("Not found key " + "database." + databaseType.getKey());
        this.databaseCredentials = DatabaseCredentialsParser.parse(plugin, credentialsConfig, databaseType);
        this.databaseCredentials.loadDriver(plugin);
        this.connectionSource = createConnection();
        this.asyncExecutorService = Executors.newCachedThreadPool();
    }

    private static @NotNull DatabaseType parseDatabaseType(@NotNull RConfig config) throws IllegalArgumentException {
        String rawType = null;
        DatabaseType databaseType = null;
        try {
            rawType = config.getString("database.type", "", null);
            databaseType = DatabaseType.getByKey(rawType);
        } catch (InvalidConfiguration e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        if (databaseType.isUnknown())
            throw new IllegalArgumentException(String.format("Unknown database type '%s'!", rawType));

        return databaseType;
    }

    public <T extends SQLObject> void registerSQLObject(@NotNull Class<T> tableClass) throws SQLException {
        registeredClass.add(tableClass);
        Dao<T, ?> dao = DaoManager.createDao(this.createConnection(), tableClass);
        daoMap.put(tableClass, dao);
    }

    public <T extends SQLObject> void createTable() throws SQLException {
        @NotNull ConnectionSource connection = this.createConnection();
        for (Class<?> tableClass : registeredClass) {
            TableUtils.createTableIfNotExists(connection, tableClass);
        }
    }

    public <D extends Dao<T, ?>, T extends SQLObject<T, ?>> D getDao(@NotNull Class<T> tableClass) {
        return (D) daoMap.get(tableClass);
    }

    public @NotNull ConnectionSource createConnection() throws SQLException {
        return databaseCredentials.createConnectionSource();
    }

    public <T extends SQLObject> void save(@NotNull T object) {
        runAsync(() -> this.getDao(object.getClass()).createOrUpdate(object));
    }

    // --- async proxied methods
    private @NotNull CompletableFuture<Void> runAsync(@NotNull ThrowableRunnable task) {
        return CompletableFuture.runAsync(() -> {
            try {
                task.run();
            } catch (SQLException ex) {
                handleThrowable(ex);
            }
        }, asyncExecutorService);
    }

    private void handleThrowable(@NotNull Throwable throwable) {
        LogHelper.error("An error has occurred when this plugin tried to handle an SQL statement!");
        Throwable cause = throwable;
        while (cause != null) {
            LogHelper.error(cause.toString());
            cause = cause.getCause();
        }
    }

    public void registerDataPersisters(@NotNull DataPersister... dataPersisters) {
        DataPersisterManager.registerDataPersisters(dataPersisters);
    }

    public void close() {
        if (asyncExecutorService != null)
            asyncExecutorService.shutdown();
        if (connectionSource != null)
            connectionSource.closeQuietly();
    }

    @FunctionalInterface
    private interface ThrowableRunnable {
        void run() throws SQLException;
    }
}
