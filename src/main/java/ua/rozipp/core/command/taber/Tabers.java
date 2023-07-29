package ua.rozipp.core.command.taber;

import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class Tabers {
    public static Taber onlinePlayer = (sender, lastWord) -> {
        Validate.notNull(sender, "Sender cannot be null");

        Player senderPlayer = sender instanceof Player ? (Player) sender : null;

        List<String> matchedPlayers = new ArrayList<>();
        for (Player player : sender.getServer().getOnlinePlayers()) {
            String name = player.getName();
            if ((senderPlayer == null || senderPlayer.canSee(player)) && StringUtil.startsWithIgnoreCase(name, lastWord)) {
                matchedPlayers.add(name);
            }
        }

        matchedPlayers.sort(String.CASE_INSENSITIVE_ORDER);
        return matchedPlayers;
    };
}
