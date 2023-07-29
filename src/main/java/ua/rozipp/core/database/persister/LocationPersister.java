package ua.rozipp.core.database.persister;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.StringType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class LocationPersister extends StringType {

    private static final LocationPersister SINGLETON = new LocationPersister();

    public static @NotNull LocationPersister getSingleton() {
        return SINGLETON;
    }

    protected LocationPersister() {
        super(SqlType.STRING, new Class<?>[]{Location.class});
    }

    @Override
    public @Nullable String javaToSqlArg(@NotNull FieldType fieldType, @Nullable Object javaObject) {
        if (javaObject instanceof Location) {
            Location location = (Location) javaObject;
            StringBuilder sb = new StringBuilder();
            sb.append(location.getWorld().getUID());
            sb.append(",");
            sb.append(location.getX());
            sb.append(",");
            sb.append(location.getY());
            sb.append(",");
            sb.append(location.getZ());
            sb.append(",");
            sb.append(location.getPitch());
            sb.append(",");
            sb.append(location.getYaw());
            return sb.toString();
        }
        return null;
    }

    @Override
    public @Nullable Location sqlArgToJava(@NotNull FieldType fieldType, @Nullable Object sqlArg, int columnPos) {
        if (sqlArg instanceof String) {
            String[] strings = ((String) sqlArg).split(",");
            return new Location(Bukkit.getWorld(UUID.fromString(strings[0])),
                    Double.parseDouble(strings[1]),
                    Double.parseDouble(strings[2]),
                    Double.parseDouble(strings[3]),
                    Float.parseFloat(strings[4]),
                    Float.parseFloat(strings[5])
            );
        }
        return null;
    }
}
