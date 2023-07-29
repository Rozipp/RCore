package ua.rozipp.core.command.taber;

import org.bukkit.command.CommandSender;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Клас кешируемых аргументов для дополнения клавишей Tab
 * @author ua.rozipp */
public abstract class CashedTaber implements Taber {

    protected final String cmdName;
    protected final Map<String, List<String>> cashTabList = new LinkedHashMap<>();

    public CashedTaber(String cmdName) {
        this.cmdName = cmdName;
    }

    private String getKey(String arg){
        return cmdName + " " + arg;
    }

    private List<String> addTabList(String arg, List<String> tabList) {
        if (tabList.isEmpty()) {
            if (arg.isEmpty()) tabList = null;
        }
        cashTabList.put(getKey(arg), tabList);
        return tabList;
    }

    protected abstract List<String> newTabList(String arg);

    @Override
    public List<String> getTabList(CommandSender sender, String arg) {
        String key = getKey(arg);
        if (cashTabList.containsKey(key))
            return cashTabList.get(key);
        else {
            List<String> tabList = newTabList(arg);
            if (tabList.isEmpty()) {
                if (arg.isEmpty()) tabList = null;
            }
            cashTabList.put(key, tabList);
            return tabList;
        }
    }

}

