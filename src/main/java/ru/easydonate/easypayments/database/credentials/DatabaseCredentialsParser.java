package ru.easydonate.easypayments.database.credentials;

import com.j256.ormlite.support.ConnectionSource;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import ru.easydonate.easypayments.database.DatabaseType;
import ru.easydonate.easypayments.utility.Reflection;
import ua.rozipp.core.config.RConfig;
import ua.rozipp.core.exception.DatabaseException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public final class DatabaseCredentialsParser {

    @SuppressWarnings("unchecked")
    public static <T extends DatabaseCredentials> T parse(
            @NotNull Plugin plugin,
            @NotNull RConfig config,
            @NotNull DatabaseType databaseType
    ) throws DatabaseException {
        Class<T> providingClass = (Class<T>) databaseType.getProvidingClass();
        T credentials;

        ClassLoader main = Thread.currentThread().getContextClassLoader();
        // create new instance
        try {
            Thread.currentThread().setContextClassLoader(DatabaseCredentialsParser.class.getClassLoader());
            Constructor<T> constructor = providingClass.getConstructor(Plugin.class);
            credentials = constructor.newInstance(plugin);
        } catch (NoSuchMethodException ignored) {
            try {
                Constructor<T> constructor = providingClass.getConstructor();
                credentials = constructor.newInstance();
            } catch (NoSuchMethodException ex) {
                throw new DatabaseException("A valid constructor was not found in " + providingClass.getName() + "!", databaseType);
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException ex) {
                throw new DatabaseException("Cannot invoke constructor in " + providingClass.getName() + "!", ex, databaseType);
            }
        } catch (InvocationTargetException | IllegalAccessException | InstantiationException ex) {
            throw new DatabaseException("Cannot invoke constructor in " + providingClass.getName() + "!", ex, databaseType);
        }
        finally {
            Thread.currentThread().setContextClassLoader(main);
        }

        // get configuration keys
        Map<String, Object> keys = config.getValues(false);

        // working with fields
        Map<Field, CredentialField> fields = Reflection.findAnnotatedDeclaredFields(providingClass, CredentialField.class);
        if(!fields.isEmpty()) {
            for (Field field : fields.keySet()) {
                CredentialField annotation = fields.get(field);

                String fieldName = field.getName();
                String configField = annotation.value();
                if(configField == null)
                    continue;

                Object configValue = keys.get(configField);
                if(configValue == null) {
                    if(annotation.optional()) {
                        continue;
                    } else {
                        throw new DatabaseException("A required credentials field '" + fieldName + "' isn't specified in your config!", databaseType);
                    }
                }

                try {
                    field.setAccessible(true);
                } catch (SecurityException ex) {
                    throw new DatabaseException("Couldn't make field '" + fieldName + "' accessible!", databaseType);
                }

                try {
                    field.set(credentials, configValue);
                } catch (IllegalAccessException ex) {
                    throw new DatabaseException("Couldn't modify value of field '" + fieldName + "'!", databaseType);
                }
            }
        }

        return credentials;
    }

}
