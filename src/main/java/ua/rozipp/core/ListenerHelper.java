package ua.rozipp.core;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import redempt.redlib.RedLib;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Make a listener extend this class then all you need to do is "new ExampleListener(myPlugin);"
 * @author Goblom
 */
public abstract class ListenerHelper implements Listener {
    
    public static final List<Listener> listeners = Lists.newArrayList();
    
    public static void unregisterAll() {
        for (Listener listener : listeners) {
            HandlerList.unregisterAll(listener);
        }
    }

    public static Collection<Listener> getListeners() {
        return Collections.unmodifiableCollection(listeners);
    }
            
    public static void register(Listener listener, Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(listener, plugin);
        ListenerHelper.listeners.add(listener);
    }
    
}
