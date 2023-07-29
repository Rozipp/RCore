package ua.rozipp.core;

import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.TranslationRegistry;
import net.kyori.adventure.translation.Translator;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;
import ua.rozipp.core.config.RConfigFile;

import java.io.File;
import java.text.MessageFormat;
import java.util.*;

public class LocaleHelper {

    private static Locale serverLocale = Locale.US;
    private static final Map<String, Locale> usingLocales = new HashMap<>();

    public static void registerPluginsLocaleFiles(Plugin plugin) {
        String serverLocaleName = plugin.getConfig().getString("server_locale", "ru_ru");
        if (!serverLocaleName.isEmpty()) LocaleHelper.setServerLocaleName(serverLocaleName);

        LocaleHelper.foundLocaleFiles(plugin);
        LocaleHelper.loadLocaleFile(plugin);
    }

    static public Locale getServerLocale() {
        return serverLocale;
    }

    public static void setServerLocaleName(String languageTag) {
        serverLocale = forLanguageTag(languageTag);
        Locale.setDefault(serverLocale);
        LogHelper.info("serverLocale set \"" + serverLocale.toLanguageTag() + "\"");
    }

    public static Locale forLanguageTag(String languageTag) {
        String clearLanguageTag = languageTag.replace("_", "-");
        if (usingLocales.containsKey(clearLanguageTag))
            return usingLocales.get(clearLanguageTag);
        else {
            Locale result = Locale.forLanguageTag(clearLanguageTag);
            usingLocales.put(result.toLanguageTag(), result);
            return result;
        }
    }

    public static String translate(String key) {
        return translate(serverLocale, key, null);
    }

    public static String translate(String key, String[] args) {
        return translate(serverLocale, key, args);
    }

    public static String translate(Locale locale, String key) {
        return translate(serverLocale, key, null);
    }

    private static String translate(Locale locale, String key, String[] args) {
        MessageFormat mf = GlobalTranslator.get().translate(key, locale);
        if (mf == null) {
//            String languageTag = locale.toLanguageTag();
//            saveToFileConfigure("locale" + File.separator + languageTag + ".yml", key, key);
//            LogHelper.warning("Add new translatable key \"" + key + "\" in " + languageTag + " locale");
            return "FIXME " + key;
        }
        return mf.format(args);
    }

    public static void foundLocaleFiles(Plugin plugin) {
        File localeFolder = new File(plugin.getDataFolder(), "locale/");
        if (!localeFolder.exists()) {
            if (!localeFolder.mkdirs()) return;
        }

        for (String fileName : Objects.requireNonNull(localeFolder.list())) {
            String[] split = fileName.split("\\.");
            if (split.length != 2 || !split[1].equals("yml")) {
                LogHelper.info("Found file " + fileName + ", but it is not locale file.");
                continue;
            }
            LogHelper.fine("Found locale file " + fileName);
            forLanguageTag(split[0]);
        }
    }

    public static void reloadLocaleFile(Plugin plugin) {
        try {
            for (Translator t : GlobalTranslator.get().sources())
                if (t.name().namespace().equals(plugin.getName())) GlobalTranslator.get().removeSource(t);
        } catch (Exception ignored) {
        }
        loadLocaleFile(plugin);
    }

    public static void loadLocaleFile(Plugin plugin) {
        TranslationRegistry translator = TranslationRegistry.create(new NamespacedKey(plugin, "translator"));
        for (String languageTag : usingLocales.keySet()) {
            RConfigFile cfg = ConfigHelper.loadConfigFile(plugin, "locale/" + languageTag + ".yml");

            Locale locale = usingLocales.get(languageTag);

            for (String key : cfg.getKeys(false)) {
                if (key.isEmpty()) continue;
                String format = cfg.getString(key);
                if (format == null || format.isEmpty()) continue;
                format = format
                        .replace("[%0]", "{0}")
                        .replace("[%1]", "{1}")
                        .replace("[%2]", "{2}")
                        .replace("[%3]", "{3}"); //фикс старых ключей
                translator.register(key, locale, new MessageFormat(format, locale));
                try {
                    GlobalTranslator.get().addSource(translator);
                } catch (Exception ignored) {
                }
            }
        }
    }

}
