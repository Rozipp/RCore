package ua.rozipp.core;

import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LogHelper {

    @Setter
    private static CommandSender consoleSender;

    public static void heading(@NotNull String title) {
        getLogger().info("========= " + title + " =========");
    }

    public static void info(@NotNull String message) {
        info(Component.text(message));
    }

    public static void info(@NotNull Component message) {
        if (getLogger().isLoggable(Level.INFO))
            sendConsole(Component.text("[INFO] ").append(message.color(NamedTextColor.AQUA)));
    }

    public static void debug(@NotNull String message) {
        debug(Component.text(message));
    }

    public static void debug(@NotNull Component message) {
        if (getLogger().isLoggable(Level.ALL))
            sendConsole(Component.text("[DEBUG] ").append(message.color(NamedTextColor.DARK_GREEN)));
    }

    public static void warning(@NotNull String message) {
        warning(Component.text(message));
    }

    public static void warning(@NotNull Component message) {
        if (getLogger().isLoggable(Level.WARNING))
            sendConsole(Component.text("[WARNING] ", NamedTextColor.YELLOW).append(message));
    }

    public static void error(@NotNull String message) {
        error(Component.text(message));
    }

    public static void error(@NotNull Component message) {
        if (getLogger().isLoggable(Level.SEVERE))
            sendConsole(Component.text("[ERROR] ", NamedTextColor.RED).append(message));
    }

    public static void fine(@NotNull String message) {
        fine(Component.text(message));
    }

    public static void fine(@NotNull Component message) {
        if (getLogger().isLoggable(Level.FINEST))
            sendConsole(Component.text("[OK] ", NamedTextColor.GREEN).append(message));
    }

    public static void moneylog(final String name, final String message) {
        if (getLogger().isLoggable(Level.INFO))
            sendConsole(Component.text("[moneylog: " + name + "] " + message).color(NamedTextColor.AQUA));
    }

    private static void sendConsole(Component component) {
        MessageHelper.send(getConsoleSender(), component);
    }

    private static CommandSender getConsoleSender() {
        if (consoleSender == null)
            consoleSender = Bukkit.getConsoleSender(); //For test and Logger
        return consoleSender;
    }

    public static @NotNull Logger getLogger() {
        try {
            return Bukkit.getLogger();
        }catch (Exception e){
            return Logger.getGlobal();
        }
    }

    public static void setLevel(String name) {
        Level level = Level.INFO;
        try {
            level = Level.parse(name);
        } catch (Exception ignored) {
        }
        getLogger().setLevel(level);
        LogHelper.info("Logging level changed to " + getLogger().getLevel().getName());
    }

}
