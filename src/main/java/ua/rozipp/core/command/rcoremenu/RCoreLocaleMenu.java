package ua.rozipp.core.command.rcoremenu;

import ua.rozipp.core.LocaleHelper;
import ua.rozipp.core.MessageHelper;
import ua.rozipp.core.command.CustomCommand;
import ua.rozipp.core.command.CustomMenuCommand;

import java.util.Locale;

public class RCoreLocaleMenu extends CustomMenuCommand {
    public RCoreLocaleMenu() {
        super("locale");

        add(new CustomCommand("getserverlocale").withExecutor((sender, label, args) -> {
            sender.sendMessage(LocaleHelper.getServerLocale().toLanguageTag());
            sender.sendMessage("Name : " + LocaleHelper.getServerLocale().getDisplayName());
            sender.sendMessage("Country : " + LocaleHelper.getServerLocale().getDisplayCountry());
            sender.sendMessage("Language : " + LocaleHelper.getServerLocale().getDisplayLanguage());
        }));
        add(new CustomCommand("setserverlocale").withExecutor((sender, label, args) -> {
            String languageTag = getNamedString(args, 0, "Enter LanguageTag");
            Locale newLocale = Locale.forLanguageTag(languageTag);
            if (newLocale == null) {
                MessageHelper.sendError(sender, "Locale \"" + languageTag + "\" is not correct.");
                return;
            }
            LocaleHelper.setServerLocaleName(languageTag);
        }));
        add(new CustomCommand("stringtolocale").withExecutor((sender, label, args) -> {
            String key = getNamedString(args, 0, "Enter key to language string");
            Locale locale = LocaleHelper.getServerLocale();
            try {
                String languageTag = getNamedString(args, 1, "Enter LanguageTag");
                locale = LocaleHelper.forLanguageTag(languageTag);
                if (locale == null) {
                    MessageHelper.sendError(sender, "Locale \"" + languageTag + "\" is not correct.");
                    return;
                }
            }catch (Exception ignored){
                locale = LocaleHelper.getServerLocale();
            }

            MessageHelper.sendSuccess(sender, LocaleHelper.translate(locale, key));
        }));
        add(new CustomCommand("mylocale").withExecutor((sender, label, args) -> sender.sendMessage("My locale = " + getPlayer(sender).locale().toLanguageTag())));
    }
}
