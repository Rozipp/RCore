package redempt.redlib.misc;

import org.bukkit.ChatColor;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Contains utilities for formatting various information
 * @author Redempt
 */
public class FormatUtils {
	
	private static DecimalFormat format = new DecimalFormat("0.00");
	private static char[] suffixes = {'K', 'M', 'B', 'T', 'Q'};
	
	/**
	 * Formats a time offset like 1h3m8s (1 hour, 3 minutes, 8 seconds)
	 * @param millis The time offset, in milliseconds
	 * @param truncate The number of units to truncate -
	 *                 1 for seconds, 2 for seconds and minutes, 3 for seconds, minutes, and hours
	 * @return The formatted string
	 */
	public static String formatTimeOffset(long millis, int truncate) {
		millis /= 1000;
		long days = millis / 86400;
		millis %= 86400;
		long hours = millis / 3600;
		millis %= 3600;
		long minutes = millis / 60;
		millis %= 60;
		StringBuilder output = new StringBuilder();
		if (days > 0 || truncate == 3) {
			output.append(days).append("d");
		}
		if (hours > 0 && (truncate < 3 || (days == 0))) {
			output.append(hours).append("h");
		}
		if (minutes > 0 && (truncate < 2 || (hours == 0))) {
			output.append(minutes).append("m");
		}
		if (truncate < 1) {
			output.append(millis).append("s");
		}
		return output.toString();
	}
	
	/**
	 * Formats a time offset like 1h3m8s (1 hour, 3 minutes, 8 seconds)
	 * @param millis The time offset, in milliseconds
	 * @return The formatted string
	 */
	public static String formatTimeOffset(long millis) {
		return formatTimeOffset(millis, 0);
	}
	
	/**
	 * Truncates a double using a DecimalFormat with 0.00 as its format string
	 * @param input The input double
	 * @return The formatted double
	 */
	public static String truncateDouble(double input) {
		return format.format(input);
	}
	
	private static Set<Character> colorChars = "4c6e2ab319d5f780rlonmk".chars().mapToObj(i -> (char) i).collect(Collectors.toSet());
	
	/**
	 * Shorthand for {@link ChatColor#translateAlternateColorCodes(char, String)} which also formats hex color codes
	 * @param input The input string
	 * @return The colored string, replacing color codes using ampersands with proper codes
	 */
	public static String color(String input) {
		return color(input, true);
	}
	
	/**
	 * Shorthand for {@link ChatColor#translateAlternateColorCodes(char, String)} with the option to format hex color codes
	 * @param input The input string
	 * @param hex Whether to translate hex color codes for 1.16+ (format: {@literal &#FF0000})
	 * @return The colored string, replacing color codes using ampersands with proper codes
	 */
	public static String color(String input, boolean hex) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (i + 1 >= input.length()) {
				builder.append(c);
				continue;
			}
			char n = input.charAt(i + 1);
			if (c == '\\' && (n == '&' || n == '\\')) {
				i++;
				builder.append(n);
				continue;
			}
			if (c != '&') {
				builder.append(c);
				continue;
			}
			if (colorChars.contains(n)) {
				builder.append(ChatColor.COLOR_CHAR);
				continue;
			}
			if (hex && n == '#' && i + 7 <= input.length()) {
				String hexCode = input.substring(i + 2, i + 8).toUpperCase(Locale.ROOT);
				if (hexCode.chars().allMatch(ch -> (ch <= '9' && ch >= '0') || (ch <= 'F' && ch >= 'A'))) {
					hexCode = Arrays.stream(hexCode.split("")).map(s -> ChatColor.COLOR_CHAR + s).collect(Collectors.joining());
					builder.append(ChatColor.COLOR_CHAR).append("x").append(hexCode);
					i += 7;
					continue;
				}
			}
			builder.append(c);
		}
		return builder.toString();
	}
	
	/**
	 * Formats money like 3.5B representing 3.5 billion
	 * @param money The money
	 * @return The formatted output string
	 */
	public static String formatMoney(double money) {
		int i = -1;
		while (money >= 1000) {
			money /= 1000;
			i++;
		}
		if (i >= 0) {
			return truncateDouble(money) + suffixes[i];
		}
		return truncateDouble(money);
	}
	
	/**
	 * Formats a large number with commas, like 12345 as 12,345
	 * @param num The number to format
	 * @return The formatted String representing the number
	 */
	public static String formatLargeInteger(long num) {
		if (num == 0) {
			return "0";
		}
		StringBuilder out = new StringBuilder();
		boolean negative = num < 0;
		num = Math.abs(num);
		int iter = 0;
		while (num > 0) {
			out.append(num % 10);
			num /= 10;
			iter++;
			if (iter == 3) {
				iter = 0;
				out.append(',');
			}
		}
		if (negative) {
			out.append('-');
		}
		out.reverse();
		if (out.charAt(0) == ',') {
			out.deleteCharAt(0);
		}
		return out.toString();
	}
	
	/**
	 * Converts a string to Title Case, where the first character and every character after a space is capitalized. Preserves spaces.
	 * @param string The string to convert to Title Case
	 * @return The Title Case string
	 */
	public static String toTitleCase(String string) {
		StringBuilder str = new StringBuilder();
		boolean space = true;
		for (int i = 0; i < string.length(); i++) {
			char c = string.charAt(i);
			str.append(space ? Character.toUpperCase(c) : Character.toLowerCase(c));
			space = c == ' ';
		}
		return str.toString();
	}
	
	/**
	 * Converts a string to camelCase, where every character after a space is capitalized. Removes spaces.
	 * @param string The string to convert to camelCase
	 * @return The camelCase string
	 */
	public static String toCamelCase(String string) {
		StringBuilder str = new StringBuilder();
		boolean space = false;
		for (int i = 0; i < string.length(); i++) {
			char c = string.charAt(i);
			str.append(c == ' ' ? "" : (space ? Character.toUpperCase(c) : Character.toLowerCase(c)));
			space = c == ' ';
		}
		return str.toString();
	}
	
	/**
	 * Splits a line of text across multiple lines, attempting to limit the length of each line
	 * @param line The line to wrap
	 * @param maxLength The max length to cap each line at
	 * @return The list of separate lines
	 */
	public static List<String> lineWrap(String line, int maxLength) {
		String[] split = line.split(" ");
		List<String> lines = new ArrayList<>();
		StringBuilder current = new StringBuilder();
		for (String word : split) {
			if (current.length() + word.length() + 1 <= maxLength) {
				current.append(word).append(' ');
			} else {
				if (current.length() > 0) {
					lines.add(current.substring(0, current.length() - 1));
				}
				current = new StringBuilder(word).append(' ');
			}
		}
		if (current.length() > 0) {
			lines.add(current.substring(0, current.length() - 1));
		}
		return lines;
	}
	
}
