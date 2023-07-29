package ua.rozipp.core.config;

import ua.rozipp.core.exception.InvalidConfiguration;

import java.util.ArrayList;
import java.util.List;

public abstract class RConfigSection implements RConfig {

    @Override
    public String getString(String path, String defVal, String message) throws InvalidConfiguration {
        String result = (String) get(path);
        if (result != null)
            return result;
        if (defVal == null)
            throw new InvalidConfiguration((message != null) ? message : "This configSection does not contain the data block \"" + path + "\"");
        return defVal;
    }

    @Override
    public Boolean getBoolean(String path, Boolean defVal, String message) throws InvalidConfiguration {
        Boolean result = (Boolean) get(path);
        if (result != null)
            return result;
        if (defVal == null)
            throw new InvalidConfiguration((message != null) ? message : "This configSection does not contain the data block \"" + path + "\"");
        return defVal;
    }

    @Override
    public Integer getInt(String path, Integer defVal, String message) throws InvalidConfiguration {
        try {
            Object o = get(path);
            if (o instanceof Number)
                return ((Number) o).intValue();
            if (o instanceof String)
                return Integer.parseInt((String) o);
        } catch (Exception e) {
            if (defVal == null)
                throw new InvalidConfiguration((message != null) ? message : "This configSection does not contain the data block \"" + path + "\"");
        }
        return defVal;
    }

    @Override
    public Double getDouble(String path, Double defVal, String message) throws InvalidConfiguration {
        try {
            Object o = get(path);
            if (o instanceof Number)
                return ((Number) o).doubleValue();
            if (o instanceof String)
                return Double.parseDouble((String) o);
        } catch (Exception ignored) {
        }
        if (defVal == null)
            throw new InvalidConfiguration((message != null) ? message : "This configSection does not contain the data block \"" + path + "\"");
        return defVal;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> getList(String path, Class<T> aClass, List<T> defVal, String message) throws InvalidConfiguration {
        List<Object> list = (List<Object>) get(path);
        if (list != null) {
            List<T> result = new ArrayList<>();
            for (Object o : list)
                try {
                    result.add((T) o);
                } catch (Exception ignored) {
                }
            return result;
        }
        if (defVal == null)
            throw new InvalidConfiguration((message != null) ? message : "This configSection does not contain the data block \"" + path + "\"");
        return defVal;
    }

}
