package ua.rozipp.core.util;

import org.bukkit.ChatColor;

public class TextUtils {

    public static String translateAlternateColorCodes(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

}