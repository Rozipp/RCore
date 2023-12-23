package ua.rozipp.core.command.rcoremenu;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.regions.CuboidRegion;
import net.kyori.adventure.text.Component;
import org.bukkit.Chunk;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import ua.rozipp.core.MessageHelper;
import ua.rozipp.core.PluginHelper;
import ua.rozipp.core.blocks.CustomBlockType;
import ua.rozipp.core.command.CustomCommand;
import ua.rozipp.core.command.CustomMenuCommand;
import ua.rozipp.core.command.Validators;
import ua.rozipp.core.command.taber.Taber;
import ua.rozipp.core.exception.ComponentException;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class RCoreBlocksMenu extends CustomMenuCommand {
    public RCoreBlocksMenu() {
        super("blocks");
        withDescription(Component.text("Меню управления кастомными блоками"));
        withValidator(Validators.mustBeAdmin);
        add(new CustomCommand("placehear").withTabCompleter(tabCustomBlockType).withExecutor((sender, label, args) -> {
            CustomBlockType customBlockType = getCustomBlockType(args, 0);
            Player player = getPlayer(sender);
            Block block = player.getTargetBlock(4);
            BlockFace blockFace = player.getTargetBlockFace(4);
            if (block != null && blockFace != null)
                PluginHelper.getCustomBlockRegistry().createCustomBlock(block.getRelative(blockFace), blockFace, customBlockType, null, player);
        }));

        add(new CustomCommand("place").withTabCompleter(tabCustomBlockType).withExecutor((sender, label, args) -> {
            CustomBlockType customBlockType = getCustomBlockType(args, 0);
            Player player = getPlayer(sender);
            BlockFace blockFace = player.getFacing();

            WorldEdit worldEditApi = WorldEdit.getInstance();
            LocalSession session = worldEditApi.getSessionManager().get(BukkitAdapter.adapt(player));
            CuboidRegion selection;
            try {
                selection = (CuboidRegion) session.getSelection(session.getSelectionWorld());
            } catch (IncompleteRegionException e) {
                throw new ComponentException("Region not found");
            }
            AtomicInteger count = new AtomicInteger();
            selection.forEach(bv3 -> {
                count.getAndIncrement();
                Block block = player.getWorld().getBlockAt(bv3.getBlockX(), bv3.getBlockY(), bv3.getBlockZ());
                PluginHelper.getCustomBlockRegistry().createCustomBlock(block, blockFace, customBlockType, null, player);
            });
            MessageHelper.sendSuccess(player, "Change " + count + " blocks");
        }));
        add(new CustomCommand("clearAllArmorStand").withExecutor((sender, label, args) -> {
            Player player = getPlayer(sender);
            Chunk chunk = player.getChunk();
            for (Entity entity : chunk.getEntities()){
                if (entity.getType() == EntityType.ARMOR_STAND) entity.remove();
            }
        }));
    }

    public CustomBlockType getCustomBlockType(String[] args, int index) throws ComponentException {
        String s = getNamedString(args, index, "Enter CustomBlockType");
        CustomBlockType customBlockType = PluginHelper.getCustomBlockRegistry().getByKey(NamespacedKey.fromString(s));
        if (customBlockType == null) throw new ComponentException("CustomBlockType for found");
        return customBlockType;
    }

    public static Taber tabCustomBlockType = (sender, arg) -> {
        ArrayList<String> list = new ArrayList<>();
        for (CustomBlockType c : PluginHelper.getCustomBlockRegistry().getTypes()) {
            if (c.getId().asString().startsWith(arg)) list.add(c.getId().asString());
        }
        return list;
    };
}
