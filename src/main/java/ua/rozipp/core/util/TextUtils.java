package ua.rozipp.core.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;
import ua.rozipp.core.LocaleHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TextUtils {

    public static final char SECTION_CHAR = '\u00A7'; // §
    public static final char AMPERSAND_CHAR = '&';

    public static String joinNewline(String... strings) {
        return joinNewline(Arrays.stream(strings));
    }

    public static String joinNewline(Stream<String> strings) {
        return strings.collect(Collectors.joining("\n"));
    }

    public static TextComponent fromLegacy(String input, char character) {
        return LegacyComponentSerializer.legacy(character).deserialize(input);
    }

    public static TextComponent fromLegacy(String input) {
        return LegacyComponentSerializer.legacy(AMPERSAND_CHAR).deserialize(input);
    }

    public static String toLegacy(Component component, char character) {
        return LegacyComponentSerializer.legacy(character).serialize(component);
    }

    public static String toLegacy(Component component) {
        return LegacyComponentSerializer.legacy(AMPERSAND_CHAR).serialize(component);
    }

    public static String colorize(String msg) {
        return decolorize(msg);
//        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static String decolorize(String s) {
        return s == null ? null : translateAlternateColorCodes(SECTION_CHAR, AMPERSAND_CHAR, s);
    }

    public static String translateAlternateColorCodes(char from, char to, String textToTranslate) {
        char[] b = textToTranslate.toCharArray();
        for (int i = 0; i < b.length - 1; i++) {
            if (b[i] == from && "0123456789AaBbCcDdEeFfKkLlMmNnOoRrXx".indexOf(b[i + 1]) > -1) {
                b[i] = to;
                b[i + 1] = Character.toLowerCase(b[i + 1]);
            }
        }
        return new String(b);
    }

    public static String translateHexColor(String text) {
        Pattern pattern = Pattern.compile("&#([A-Fa-f0-9]{6})");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            String firstGroup = matcher.group(1);
            text = text.replace("&#" + firstGroup, net.md_5.bungee.api.ChatColor.of("#" + firstGroup).toString());
        }
        return net.md_5.bungee.api.ChatColor.translateAlternateColorCodes((char) 38, text.replace("\n", "\n"));
    }

    private TextUtils() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }

    // ------------------- Component

    public static String componentToString(Component component) {
        return componentToString(component, LocaleHelper.getServerLocale());
    }

    public static String componentToString(Component component, Locale locale) {
        StringBuilder sb = new StringBuilder();
        readComponent(sb, component, locale);
        return sb.toString();
    }

    private static StringBuilder readComponent(StringBuilder sb, Component component, Locale locale) {
        if (component instanceof TextComponent)
            sb.append(((TextComponent) component).content());
        else {
            if (component instanceof TranslatableComponent) {
                TranslatableComponent tc = (TranslatableComponent) component;
                List<String> arg = new ArrayList<>();
                for (Component c : tc.args()) arg.add(componentToString(c, locale));

                sb.append(LocaleHelper.translate(locale, tc.key(), arg.toArray(new String[0])));
            }
        }
        if (!component.children().isEmpty())
            for (Component c : component.children()) {
                sb = readComponent(sb, c, locale);
            }
        return sb;
    }

    public static Component translate(Component component) {
        return translate(component, Locale.getDefault());
    }

    public static Component translate(Component component, Locale locale) {
        if (component instanceof TranslatableComponent tc) {
            List<String> arg = new ArrayList<>();
            for (Component c : tc.args()) arg.add(componentToString(c, locale));

            String s = LocaleHelper.translate(locale, tc.key(), arg.toArray(new String[0]));
            Component result = Component.text(s, tc.style());
            if (!tc.children().isEmpty())
                for (Component c : tc.children()) {
                    result = result.append(translate(c, locale));
                }
            return result;
        } else {
            List<Component> list = new ArrayList<>();
            if (!component.children().isEmpty())
                for (Component c : component.children()) {
                    list.add(translate(c, locale));
                }
            return (list.isEmpty()) ? component : component.children(list);
        }
    }
}