package ua.rozipp.core.database;

import com.j256.ormlite.dao.Dao;
import ua.rozipp.core.LogHelper;

import java.sql.SQLException;
import java.util.Objects;

public abstract class SQLObject<T, ID> {

    private Dao<T, ID> dao;

    public abstract ID getId();

    public int create() {
        try {
            this.checkForDao();
            return dao.create((T) this);
        } catch (Exception e) {
            LogHelper.error(e.getMessage());
            return 0;
        }
    }

    public int refresh() {
        try {
            this.checkForDao();
            return dao.refresh((T) this);
        } catch (Exception e) {
            LogHelper.error(e.getMessage());
            return 0;
        }
    }

    public int update() {
        try {
            this.checkForDao();
            return dao.update((T) this);
        } catch (Exception e) {
            LogHelper.error(e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    public int delete() {
        try {
            this.checkForDao();
            return dao.delete((T) this);
        } catch (Exception e) {
            LogHelper.error(e.getMessage());
            return 0;
        }
    }

    public String objectToString() {
        try {
            this.checkForDao();
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }

        return dao.objectToString((T) this);
    }

    public boolean objectsEqual(T other) throws SQLException {
        this.checkForDao();
        return dao.objectsEqual((T) this, other);
    }

    private void checkForDao() throws SQLException {
        if (dao == null) {
            throw new SQLException("Dao has not been set on " + this.getClass() + " object: " + this);
        }
    }

    public Dao<T, ID> getDao() {
        return this.dao;
    }

    public void setDao(Dao<T, ID> dao) {
        this.dao = dao;
    }

    public void postLoad(Dao<T, ID> dao){
        setDao(dao);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        SQLObject sqlo = (SQLObject) o;
        return Objects.equals(getId(), sqlo.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
