package ua.rozipp.core.database.persister;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.StringType;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ua.rozipp.core.object.ChunkCoord;

import java.util.Objects;
import java.util.UUID;

public class ChunkCoordPersister extends StringType {

    private static final ChunkCoordPersister SINGLETON = new ChunkCoordPersister();

    public static @NotNull ChunkCoordPersister getSingleton() {
        return SINGLETON;
    }

    protected ChunkCoordPersister() {
        super(SqlType.STRING, new Class<?>[]{ChunkCoord.class});
    }

    @Override
    public @Nullable String javaToSqlArg(@NotNull FieldType fieldType, @Nullable Object javaObject) {
        if (javaObject instanceof ChunkCoord) {
            ChunkCoord chunkCoord = (ChunkCoord) javaObject;
            StringBuilder sb = new StringBuilder();
            sb.append(chunkCoord.getWorld().getUID());
            sb.append(",");
            sb.append(chunkCoord.getX());
            sb.append(",");
            sb.append(chunkCoord.getZ());
            return sb.toString();
        }
        return null;
    }

    @Override
    public @Nullable ChunkCoord sqlArgToJava(@NotNull FieldType fieldType, @Nullable Object sqlArg, int columnPos) {
        if (sqlArg instanceof String) {
            String[] strings = ((String) sqlArg).split(",");
            return new ChunkCoord(Objects.requireNonNull(Bukkit.getWorld(UUID.fromString(strings[0]))), Integer.parseInt(strings[1]), Integer.parseInt(strings[2]));
        }
        return null;
    }
}
