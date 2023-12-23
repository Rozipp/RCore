package ua.rozipp.core;

import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.TranslationRegistry;
import net.kyori.adventure.translation.Translator;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;
import ua.rozipp.core.config.RConfig;
import ua.rozipp.core.exception.InvalidConfiguration;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Locale;

public class LocaleHelper {

    private static Locale serverLocale = Locale.US;

    public static void initServerLocale(Plugin plugin) {
        String serverLocaleName = plugin.getConfig().getString("server_locale");
        if (serverLocaleName != null && !serverLocaleName.isEmpty()) LocaleHelper.setServerLocaleName(serverLocaleName);
    }

    static public Locale getServerLocale() {
        return serverLocale;
    }

    public static void setServerLocaleName(String languageTag) {
        serverLocale = forLanguageTag(languageTag);
        Locale.setDefault(serverLocale);
        LogHelper.info("serverLocale set \"" + serverLocale + "\"");
    }

    public static Locale forLanguageTag(String languageTag) {
        if (languageTag == null) return null;
        String clearLanguageTag = languageTag.replace("_", "-");
        return Locale.forLanguageTag(clearLanguageTag);
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

    public static String translate(Locale locale, String key, String[] args) {
        MessageFormat mf = GlobalTranslator.get().translate(key, locale);
        if (mf == null)
            return "FIXME [" + locale + "]: " + key + " " + Arrays.toString(args);
        return mf.format(args);
    }

    public static void reloadLocaleFile(Plugin plugin) throws InvalidConfiguration {
        try {
            for (Translator t : GlobalTranslator.get().sources())
                if (t.name().namespace().equals(plugin.getName()))
                    GlobalTranslator.get().removeSource(t);
        } catch (Exception e) {
            LogHelper.error(e.getMessage());
        }
        loadLocaleFile(plugin);
    }

    public static void loadLocaleFile(Plugin plugin) throws InvalidConfiguration {
        TranslationRegistry translator = TranslationRegistry.create(new NamespacedKey(plugin, "translator"));
        for (RConfig config : PluginHelper.getRConfigMultiFiles(plugin).getRConfigList("locales")) {
            try {
                Locale locale = forLanguageTag(config.getString("locale"));
                if (locale == null) continue;
                addAll(translator, config, locale, "");
            } catch (Exception e1) {
                LogHelper.error(e1.getMessage());
            }
        }

        GlobalTranslator.get().addSource(translator);
    }

    private static void addAll(TranslationRegistry translator, RConfig config, Locale locale, String root) {
        for (String key : config.getKeys(false)) {
            if (key.isEmpty() || key.equals("locale")) continue;
            String abs_key = ((root.isEmpty()) ? "" : root + ".") + key;
            String message = config.getString(key);
            if (message == null || message.isEmpty()) {
                RConfig rConfig = config.getRConfig(key);
                if (rConfig != null) addAll(translator, rConfig, locale, abs_key);
            } else {
                try {
                    translator.register(abs_key, locale, new MessageFormat(message, locale));
                } catch (Exception e) {
                    LogHelper.error(e.getMessage());
                }
            }
        }
    }

}
