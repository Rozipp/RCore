package ua.rozipp.core.util;

import org.bukkit.ChatColor;

public class StringUtils {

    /** в строку string добавляет строку addString, если длина строки addString менше чем length, то добавляет пробелы */
    public static String addTabToString(String addString, Integer length) {
        StringBuilder string = new StringBuilder(addString);
        for (int i = addString.length(); i < length; i++) {
            string.append(" ");
        }
        return string.toString();
    }

    public static String convertCommand(String comm) {
        String rus = "фисвуапршолдьтщзйкыегмцчня";
        String eng = "abcdefghijklmnopqrstuvwxyz";

        String res = comm;
        for (int i = 0; i < rus.length(); i++)
            res = res.replace(rus.charAt(i), eng.charAt(i));
        return res;
    }

    public static String colorize(String input) {
        String output = input;

        output = output.replaceAll("<red>", "§4");
        output = output.replaceAll("<rose>", "§c");
        output = output.replaceAll("<gold>", "§6");
        output = output.replaceAll("<yellow>", "§e");
        output = output.replaceAll("<green>", "§2");
        output = output.replaceAll("<lightgreen>", "§a");
        output = output.replaceAll("<lightblue>", "§b");
        output = output.replaceAll("<blue>", "§3");
        output = output.replaceAll("<navy>", "§1");
        output = output.replaceAll("<darkpurple>", "§9");
        output = output.replaceAll("<lightpurple>", "§d");
        output = output.replaceAll("<purple>", "§5");
        output = output.replaceAll("<white>", "§f");
        output = output.replaceAll("<lightgray>", "§7");
        output = output.replaceAll("<gray>", "§8");
        output = output.replaceAll("<black>", "§0");

        output = output.replaceAll("<b>", "" + ChatColor.BOLD);
        output = output.replaceAll("<u>", "" + ChatColor.UNDERLINE);
        output = output.replaceAll("<i>", "" + ChatColor.ITALIC);
        output = output.replaceAll("<r>", "" + ChatColor.RESET);

        return output;
    }

    public static String valueOf(String color) {
        switch (color.toLowerCase()) {
            case "black":
                return "§0";
            case "navy":
                return "§1";
            case "green":
                return "§2";
            case "blue":
                return "§3";
            case "red":
                return "§4";
            case "purple":
                return "§5";
            case "gold":
                return "§6";
            case "lightgray":
                return "§7";
            case "gray":
                return "§8";
            case "darkpurple":
                return "§9";
            case "lightgreen":
                return "§a";
            case "lightblue":
                return "§b";
            case "rose":
                return "§c";
            case "lightpurple":
                return "§d";
            case "yellow":
                return "§e";
            case "white":
                return "§f";
        }
        return "§f";
    }

    public static String clearColorTags(String input) {
        String output = input;

        output = output.replaceAll("<red>", "");
        output = output.replaceAll("<rose>", "");
        output = output.replaceAll("<gold>", "");
        output = output.replaceAll("<yellow>", "");
        output = output.replaceAll("<green>", "");
        output = output.replaceAll("<lightgreen>", "");
        output = output.replaceAll("<lightblue>", "");
        output = output.replaceAll("<blue>", "");
        output = output.replaceAll("<navy>", "");
        output = output.replaceAll("<darkpurple>", "");
        output = output.replaceAll("<lightpurple>", "");
        output = output.replaceAll("<purple>", "");
        output = output.replaceAll("<white>", "");
        output = output.replaceAll("<lightgray>", "");
        output = output.replaceAll("<gray>", "");
        output = output.replaceAll("<black>", "");

        output = output.replaceAll("<i>", "");
        output = output.replaceAll("<u>", "");
        output = output.replaceAll("<i>", "");
        output = output.replaceAll("<s>", "");
        output = output.replaceAll("<r>", "");

        return output;
    }

}
