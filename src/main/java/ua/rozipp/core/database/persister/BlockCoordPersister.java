package ua.rozipp.core.database.persister;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.StringType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ua.rozipp.core.object.BlockCoord;

public class BlockCoordPersister extends StringType {

    private static final BlockCoordPersister SINGLETON = new BlockCoordPersister();

    public static @NotNull BlockCoordPersister getSingleton() {
        return SINGLETON;
    }

    protected BlockCoordPersister() {
        super(SqlType.STRING, new Class<?>[]{BlockCoord.class});
    }

    @Override
    public @Nullable String javaToSqlArg(@NotNull FieldType fieldType, @Nullable Object javaObject) {
        if (javaObject instanceof BlockCoord) {
            return javaObject.toString();
        }
        return null;
    }

    @Override
    public @Nullable BlockCoord sqlArgToJava(@NotNull FieldType fieldType, @Nullable Object sqlArg, int columnPos) {
        if (sqlArg instanceof String) {
            return new BlockCoord((String) sqlArg);
        }
        return null;
    }
}
