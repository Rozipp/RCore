package ua.rozipp.core.listener;

import org.bukkit.GameRule;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;
import org.bukkit.persistence.PersistentDataType;
import ua.rozipp.core.PluginHelper;
import ua.rozipp.core.RCore;
import ua.rozipp.core.enchantment.CustomEnchantment;
import ua.rozipp.core.enchantment.Enchantments;
import ua.rozipp.core.items.CustomMaterial;

import java.util.HashMap;
import java.util.Objects;

public class CustomItemListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.PHYSICAL) return;
        ItemStack stack = (event.getHand() == EquipmentSlot.OFF_HAND) ? event.getPlayer().getInventory().getItemInOffHand() : event.getPlayer().getInventory().getItemInMainHand();
        CustomMaterial cmat = CustomMaterial.getCustomMaterial(stack);
        if (cmat != null) cmat.onInteract(event);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onInteractEntity(PlayerInteractEntityEvent event) {
        if (event.isCancelled()) return;
        ItemStack stack = (event.getHand() == EquipmentSlot.OFF_HAND) ? event.getPlayer().getInventory().getItemInOffHand() : event.getPlayer().getInventory().getItemInMainHand();
        CustomMaterial cmat = CustomMaterial.getCustomMaterial(stack);
        if (cmat != null) cmat.onInteractEntity(event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onItemHeld(PlayerItemHeldEvent event) {
        if (event.isCancelled()) return;
        CustomMaterial material = CustomMaterial.getCustomMaterial(event.getPlayer().getInventory().getItemInMainHand());
        if (material != null) material.onHeld(event);
    }

    /* Prevent the player from using goodies in crafting recipies. */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnCraftItemEvent(CraftItemEvent event) {
        for (ItemStack stack : event.getInventory().getMatrix()) {
            CustomMaterial cmat = CustomMaterial.getCustomMaterial(stack);
            if (cmat != null) cmat.onCraftItem(event);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onItemPickup(EntityPickupItemEvent event) {
        if (event.isCancelled()) return;
        CustomMaterial cMat = CustomMaterial.getCustomMaterial(event.getItem().getItemStack());
        if (cMat != null) cMat.onPickupItem(event);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void OnPlayerDropItem(PlayerDropItemEvent event) {
        if (event.isCancelled()) return;
        ItemStack stack = event.getItemDrop().getItemStack();
        if (itemDrop(event, event.getPlayer(), stack)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnItemSpawn(ItemSpawnEvent event) {
        ItemStack stack = event.getEntity().getItemStack();
        CustomMaterial cmat = CustomMaterial.getCustomMaterial(stack);
        if (cmat != null) {
            cmat.onItemSpawn(event);
        }
    }

    private static final NamespacedKey PROJECTILE_CMAT_KEY = new NamespacedKey(RCore.getInstance(), "ProjectileCMat");

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if (event.getEntity().getShooter() instanceof LivingEntity shooter) {
            ItemStack inHand = Objects.requireNonNull(shooter.getEquipment()).getItemInMainHand();
            CustomMaterial cmat = CustomMaterial.getCustomMaterial(inHand);
            if (cmat != null) {
                event.getEntity().getPersistentDataContainer().set(PROJECTILE_CMAT_KEY,
                        PersistentDataType.STRING,
                        cmat.getMid().asString());
            }
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        String mid = event.getEntity().getPersistentDataContainer().get(PROJECTILE_CMAT_KEY, PersistentDataType.STRING);
        CustomMaterial cmat = CustomMaterial.getCustomMaterial(mid);
        if (cmat != null) {
            cmat.onProjectileHit(event);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerDefenseAndAttack(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;

        Double baseDamage = event.getDamage();

        Player defendingPlayer = null;
        if (event.getEntity() instanceof Player) defendingPlayer = (Player) event.getEntity();

        if (event.getDamager() instanceof Player) {
            ItemStack inHand = ((Player) event.getDamager()).getInventory().getItemInMainHand();
            CustomMaterial cMat = CustomMaterial.getCustomMaterial(inHand);

            if (cMat != null)
                cMat.onAttack(event, inHand);
//            else
//                event.setDamage(0.5);  //Non-civcraft items only do 0.5 damage.

//            if (Enchantments.hasEnchantment(inHand, CustomEnchantment.Critical))
//                CriticalEnchantment.run(event, inHand);
        }

        if (defendingPlayer != null) {
            /* Search equipt items for defense event. */
            for (ItemStack stack : Objects.requireNonNull(defendingPlayer.getEquipment()).getArmorContents()) {
                CustomMaterial cmat = CustomMaterial.getCustomMaterial(stack);
                if (cmat != null) cmat.onDefense(event, stack);
            }
            if (event.getDamager() instanceof LivingEntity le) {
                ItemStack chestplate = defendingPlayer.getEquipment().getChestplate();
                if (Enchantments.hasEnchantment(chestplate, CustomEnchantment.Thorns)) {
                    le.damage(event.getDamage() * Enchantments.getLevelEnchantment(chestplate, CustomEnchantment.Thorns), defendingPlayer);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void OnInventoryClose(InventoryCloseEvent event) {
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void OnInventoryOpen(InventoryOpenEvent event) {
        for (ItemStack stack : event.getInventory().getContents()) {
            CustomMaterial cmat = CustomMaterial.getCustomMaterial(stack);
            if (cmat != null) cmat.onInventoryOpen(event, stack);
        }

        for (ItemStack stack : event.getPlayer().getInventory()) {
            CustomMaterial cmat = CustomMaterial.getCustomMaterial(stack);
            if (cmat != null) cmat.onInventoryOpen(event, stack);
        }

        for (ItemStack stack : event.getPlayer().getInventory().getArmorContents()) {
            CustomMaterial cmat = CustomMaterial.getCustomMaterial(stack);
            if (cmat != null) cmat.onInventoryOpen(event, stack);
        }
    }

    /* Returns false if item is destroyed. */
   /* private boolean processDurabilityChanges(PlayerDeathEvent event, ItemStack stack, int i) {
        CustomMaterial cMat = CustomMaterials.getCustomMaterial(stack);
        if (cMat != null) {
            ItemChangeResult result = cMat.onDurabilityDeath(event, stack);
            if (result != null) {
                if (!result.destroyItem) {
                    event.getEntity().getInventory().setItem(i, result.stack);
                } else {
                    event.getEntity().getInventory().setItem(i, new ItemStack(Material.AIR));
                    event.getDrops().remove(stack);
                    return false;
                }
            }
        }

        return true;
    }*/

/*    private boolean processArmorDurabilityChanges(PlayerDeathEvent event, ItemStack stack, int i) {
        CustomMaterial cMat = CustomMaterials.getCustomMaterial(stack);
        if (cMat != null) {
            ItemChangeResult result = cMat.onDurabilityDeath(event, stack);
            if (result != null) {
                if (!result.destroyItem) {
                    replaceItem(event.getEntity().getInventory(), stack, result.stack);
                } else {
                    replaceItem(event.getEntity().getInventory(), stack, new ItemStack(Material.AIR));
                    event.getDrops().remove(stack);
                    return false;
                }
            }
        }

        return true;
    }*/

    /*private void replaceItem(PlayerInventory playerInventory, ItemStack oldItem, ItemStack newItem) {
        ArmorType type = ArmorType.matchType(oldItem);
        switch (type) {
            case HELMET: {
                playerInventory.setHelmet(newItem);
                break;
            }
            case CHESTPLATE: {
                playerInventory.setChestplate(newItem);
                break;
            }
            case LEGGINGS: {
                playerInventory.setLeggings(newItem);
                break;
            }
            case BOOTS: {
                playerInventory.setBoots(newItem);
                break;
            }
        }

    }*/

    // ---------------Inventory begin

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        HashMap<Integer, ItemStack> noDrop = new HashMap<>();
        Player player = event.getEntity();
        /* Search and execute any enhancements */
        for (int i = 0; i < player.getInventory().getSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack == null) continue;

            if (Enchantments.hasEnchantment(stack, CustomEnchantment.SoulBound)) {
                event.getDrops().remove(stack);
                noDrop.put(i, stack);
            }
            if (Enchantments.hasEnchantment(stack, CustomEnchantment.UnitItem)) {
                event.getDrops().remove(stack);
            }
        }

        /* Search for armor, apparently it doesnt show up in the normal inventory. */
        ItemStack[] contents = player.getInventory().getArmorContents();
        for (int i = 0; i < contents.length; i++) {
            ItemStack stack = contents[i];
            if (stack == null) continue;

            if (Enchantments.hasEnchantment(stack, CustomEnchantment.SoulBound)) {
                event.getDrops().remove(stack);
                noDrop.put(i, stack);
            }
            if (Enchantments.hasEnchantment(stack, CustomEnchantment.UnitItem)) {
                event.getDrops().remove(stack);
            }

        }

        // player.getInventory().getArmorContents()

        boolean keepInventory = Boolean.TRUE.equals(player.getWorld().getGameRuleValue(GameRule.KEEP_INVENTORY));
        if (!keepInventory) {
            PluginHelper.sync().run(() -> {
                PlayerInventory inv = player.getInventory();
                for (Integer slot : noDrop.keySet()) {
                    ItemStack stack = noDrop.get(slot);
                    inv.setItem(slot, stack);
                }
            });
        }

    }

    private boolean itemToInventory(Cancellable event, Player player, Inventory inv, Inventory topInv, ItemStack stack) {
        boolean stackEmpty = (stack == null) || stack.getType().equals(Material.AIR);
        if (stackEmpty) return false;
//        InventoryGUI gi = GuiHelper.getActiveGuiInventory(player.getUniqueId());
//        if (gi != null && inv.equals(topInv)) return gi.onItemToInventory(event, player, inv, stack);
        if (Enchantments.hasEnchantment(stack, CustomEnchantment.UnitItem) && inv.getType() != InventoryType.PLAYER)
            return true;
        CustomMaterial cMat = CustomMaterial.getCustomMaterial(stack);
        if (cMat != null) return cMat.onInvItemDrop(event, player, inv, stack);
        return false;
    }

    private boolean itemFromInventory(Cancellable event, Player player, Inventory inv, Inventory topInv, ItemStack stack) {
        boolean stackEmpty = (stack == null) || stack.getType().equals(Material.AIR);
        if (stackEmpty) return false;
//        if (Cooldown.isCooldown(stack)) return true;
//        InventoryGUI gi = GuiHelper.getActiveGuiInventory(player.getUniqueId());
//        if (gi != null && inv.equals(topInv)) return gi.onItemFromInventory(event, player, inv, stack);
        if (Enchantments.hasEnchantment(stack, CustomEnchantment.UnitItem) && inv.getType() != InventoryType.PLAYER)
            return true;
        CustomMaterial cMat = CustomMaterial.getCustomMaterial(stack);
        if (cMat != null) return cMat.onInvItemPickup(event, player, inv, stack);
        return false;
    }

    private boolean itemDrop(Cancellable event, Player player, ItemStack stack) {
        boolean stackEmpty = (stack == null) || stack.getType().equals(Material.AIR);
        if (stackEmpty) return false;
//        if (Cooldown.isCooldown(stack)) return true;
//        InventoryGUI gi = GuiHelper.getActiveGuiInventory(player.getUniqueId());
//        if (gi != null && GuiHelper.isGUIItem(stack)) return true;
        if (Enchantments.hasEnchantment(stack, CustomEnchantment.UnitItem)) return true;
        CustomMaterial cMat = CustomMaterial.getCustomMaterial(stack);
        if (cMat != null) return cMat.onDropItem(player, stack);
        return false;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.isCancelled()) return;
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();

        ItemStack clickStack = event.getCurrentItem();
        Inventory clickInv = event.getClickedInventory();
        InventoryAction ia = event.getAction();
        Inventory topInv = event.getView().getTopInventory();

        ItemStack otherStack;
        Inventory otherInv;

        boolean result = false;
        switch (ia) {
            case DROP_ALL_CURSOR, DROP_ONE_CURSOR -> {
                otherStack = event.getCursor();
                result = itemDrop(event, player, otherStack);
            }
            case PICKUP_ALL, PICKUP_HALF, PICKUP_ONE, PICKUP_SOME ->
                    result = itemFromInventory(event, player, clickInv, topInv, clickStack);
            case COLLECT_TO_CURSOR -> // Взять все что найду двойным кликом //TODO Отменить это опасное действие
                    result = true;
            case DROP_ALL_SLOT, DROP_ONE_SLOT -> {  // выбросить предмет
                result = itemFromInventory(event, player, clickInv, topInv, clickStack);
                if (result) break;
                result = itemDrop(event, player, clickStack);
            }
            case PLACE_ALL, PLACE_ONE, PLACE_SOME -> //
                    result = itemToInventory(event, player, clickInv, topInv, event.getCursor());
            case SWAP_WITH_CURSOR -> { // меняем то что в руке на то что в инвентаре
                otherStack = event.getCursor();
                result = itemFromInventory(event, player, clickInv, topInv, clickStack);
                if (result) break;
                result = itemToInventory(event, player, clickInv, topInv, otherStack);
            }
            case MOVE_TO_OTHER_INVENTORY -> {
                InventoryView view = event.getView();
                otherInv = view.getBottomInventory();
                if (!view.getType().equals(InventoryType.CRAFTING)) {
                    if (event.getRawSlot() == event.getSlot())
                        otherInv = view.getBottomInventory();
                    else
                        otherInv = view.getTopInventory();
                }
                result = itemFromInventory(event, player, clickInv, topInv, clickStack);
                if (result) break;
                result = itemToInventory(event, player, otherInv, topInv, clickStack);
            }
            case HOTBAR_SWAP, HOTBAR_MOVE_AND_READD -> {
                otherInv = event.getWhoClicked().getInventory();
                otherStack = otherInv.getItem(event.getHotbarButton());
                result = itemFromInventory(event, player, clickInv, topInv, clickStack);
                if (result) break;
                result = itemFromInventory(event, player, otherInv, topInv, otherStack);
                if (result) break;
                result = itemToInventory(event, player, otherInv, topInv, clickStack);
                if (result) break;
                result = itemToInventory(event, player, clickInv, topInv, otherStack);
            }
            default -> {
            }
        }
        if (result) {
            event.setCancelled(true);
            event.setResult(Result.DENY);
            player.updateInventory();
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryMoveItem(InventoryMoveItemEvent event) {
        //TODO это перемещение с помощью воронки. Оно необрабатывается. А надо бы.
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryDrag(InventoryDragEvent event) {
        if (event.isCancelled()) return;
        if (!(event.getWhoClicked() instanceof Player player)) return;

        ItemStack stack = event.getOldCursor();
        if (stack.getType().equals(Material.AIR)) return;

        Integer[] iSlots = event.getInventorySlots().toArray(new Integer[0]);
        Integer[] rSlots = event.getRawSlots().toArray(new Integer[0]);

        InventoryView view = event.getView();
        Inventory inv = view.getBottomInventory();
        for (int j = 0; j < iSlots.length; j++) {
            if (!view.getType().equals(InventoryType.CRAFTING)) {
                if (rSlots[j].equals(iSlots[j])) // Clicked in the top holder
                    inv = view.getTopInventory();
                else
                    inv = view.getBottomInventory();
            }

            if (itemToInventory(event, player, inv, view.getTopInventory(), stack)) {
                event.setCancelled(true);
                event.setResult(Result.DENY);
                player.updateInventory();
                return;
            }
        }
    }

    // ---------------Inventory end

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {
        CustomMaterial cMat = CustomMaterial.getCustomMaterial(event.getPlayer().getInventory().getItem(event.getHand()));
        if (cMat != null) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerLeashEvent(PlayerLeashEntityEvent event) {
        CustomMaterial cMat = CustomMaterial.getCustomMaterial(event.getPlayer().getInventory().getItemInMainHand());
        if (cMat != null) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onDurabilityChange(PlayerItemDamageEvent event) {
        CustomMaterial cMat = CustomMaterial.getCustomMaterial(event.getItem());
        if (cMat != null) cMat.onDurabilityChange(event);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void OnInventoryHold(PlayerItemHeldEvent event) {
        CustomMaterial cmat = CustomMaterial.getCustomMaterial(event.getPlayer().getInventory().getItem(event.getNewSlot()));
        if (cmat != null) cmat.onHeld(event);
    }

    // /* Prevent books from being inside an inventory. */
    /* Prevent vanilla gear from being used. */
    /* @EventHandler(priority = EventPriority.LOWEST) public void OnInventoryOpenRemove(InventoryOpenEvent event) {
     * //CivLog.debug("open event."); if (event.getPlayer() instanceof Player) { //for (ItemStack stack : event.getInventory()) { for (int i =
     * 0; i < event.getInventory().getSize(); i++) { ItemStack stack = event.getInventory().getItem(i); //CivLog.debug("stack cleanup");
     * AttributeUtil attrs = ItemCleanup(stack); if (attrs != null) { event.getInventory().setItem(i, attrs.getStack()); } } } } */

    /* @EventHandler(priority = EventPriority.LOW) public void onPlayerLogin(PlayerLoginEvent event) { class SyncTask implements Runnable {
     * String playerName; public SyncTask(String name) { playerName = name; }
     * @Override public void run() { try { Player player = CivGlobal.getPlayer(playerName); for (int i = 0; i < player.getInventory().getSize();
     * i++) { ItemStack stack = player.getInventory().getItem(i); AttributeUtil attrs = ItemCleanup(stack); if (attrs != null) {
     * player.getInventory().setItem(i, attrs.getStack()); } } ItemStack[] contents = new
     * ItemStack[player.getInventory().getArmorContents().length]; for (int i = 0; i < player.getInventory().getArmorContents().length; i++) {
     * ItemStack stack = player.getInventory().getArmorContents()[i]; AttributeUtil attrs = ItemCleanup(stack); if (attrs != null) { contents[i]
     * = attrs.getStack(); } else { contents[i] = stack; } } player.getInventory().setArmorContents(contents); } catch (CivException e) {
     * return; } } } TaskMaster.syncTask(new SyncTask(event.getPlayer().getName())); } */

}
