package ua.rozipp.core.object;

import org.bukkit.entity.Player;

import java.util.LinkedHashMap;

public class CallbackInterfaces {
    private static final LinkedHashMap<Player, CallbackInterface> CALLBACK_INTERFACE_MAP = new LinkedHashMap<>();

    public static CallbackInterface poll(Player player) {
        CallbackInterface callbackInterface = CALLBACK_INTERFACE_MAP.get(player);
        CALLBACK_INTERFACE_MAP.remove(player);
        return callbackInterface;
    }

    public static void add(Player player, CallbackInterface callbackInterface) {
        CALLBACK_INTERFACE_MAP.put(player, callbackInterface);
    }
}
