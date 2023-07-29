package ua.rozipp.core.threading.tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ua.rozipp.core.LogHelper;

import java.util.UUID;

public class PlayerLoginAsyncTask implements Runnable {

    UUID playerUUID;

    public PlayerLoginAsyncTask(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(playerUUID);
    }

    @Override
    public void run() {
        if (playerUUID == null || getPlayer() == null) {
            LogHelper.info("Skipping PlayerLoginAsyncTask, otherwise NPE.");
            return;
        }

//        Resident resident = CivGlobal.getResident(playerUUID);
//        resident.setUnitObjectId(0);
//        resident.calculateWalkingModifier(getPlayer());
    }
}
