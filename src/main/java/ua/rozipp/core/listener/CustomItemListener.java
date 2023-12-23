package ua.rozipp.core.listener;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
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
import ua.rozipp.core.Task;
import ua.rozipp.core.RCore;
import ua.rozipp.core.enchantment.CustomEnchantment;
import ua.rozipp.core.enchantment.Enchantments;
import ua.rozipp.core.gui.GuiHelper;
import ua.rozipp.core.items.CustomMaterial;

import java.util.HashMap;

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
        CustomMaterial material = CustomMaterial.getCustomMaterial(event.getPlayer().getInventory().getItemInMainHand());
        if (material != null) material.onInteractEntity(event);
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

    @EventHandler(priority = EventPriority.LOW)
    public void onEntityShootBowEvent(EntityShootBowEvent event) {
//        if (event.getEntity() instanceof Player) {
//            Player player = (Player) event.getEntity();
//            Arrow arrow = (Arrow) event.getProjectile();
//
//            int slot = ArrowComponent.foundShootingArrow(player.getInventory());
//            if (slot == -1) {
////				event.setCancelled(true);
////				event.setProjectile(null);
//            } else {
//                Location loc = event.getEntity().getEyeLocation();
//                Vector velocity = arrow.getVelocity();
//                float speed = (float) velocity.length();
//                Vector dir = event.getEntity().getEyeLocation().getDirection();
//
//                ItemStack stack = player.getInventory().getItem(slot);
//                FixedMetadataValue metadata = ArrowComponent.getMetadata(stack);
//                if (metadata != null) {
//                    TippedArrow tarrow = loc.getWorld().spawnArrow(loc.add(dir.multiply(2)), dir, speed, 0.0f, TippedArrow.class);
//                    if (metadata.equals(ArrowComponent.arrow_fire1)) tarrow.setFireTicks(2000);
//                    if (metadata.equals(ArrowComponent.arrow_fire2)) tarrow.setFireTicks(4000);
//                    if (metadata.equals(ArrowComponent.arrow_fire3)) tarrow.setFireTicks(6000);
//                    if (metadata.equals(ArrowComponent.arrow_knockback1)) tarrow.setKnockbackStrength(1);
//                    if (metadata.equals(ArrowComponent.arrow_knockback2)) tarrow.setKnockbackStrength(2);
//                    if (metadata.equals(ArrowComponent.arrow_knockback3)) tarrow.setKnockbackStrength(3);
//                    tarrow.setMetadata("civ_arrow_effect", metadata);
//                    arrow = tarrow;
//                } else {
//                    arrow = loc.getWorld().spawnArrow(loc.add(dir.multiply(2)), dir, speed, 0.0f);
//                }
//                arrow.setShooter(event.getEntity());
//                arrow.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
//                event.setProjectile(arrow);
//            }
//        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerDefenseAndAttack(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        Player attacker;
        if (event.getDamager() instanceof Player) {
            attacker = (Player) event.getDamager();
        } else if (event.getDamager() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getDamager();
            if (arrow.getShooter() instanceof Player) attacker = (Player) arrow.getShooter();
        }

        Double baseDamage = event.getDamage();

       /* Player defendingPlayer = null;
        if (event.getEntity() instanceof Player) defendingPlayer = (Player) event.getEntity();

        if (event.getDamager() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getDamager();

            if (event.getEntity() instanceof LivingEntity) {
                if (arrow.hasMetadata("civ_arrow_effect")) {
                    for (MetadataValue dd : arrow.getMetadata("civ_arrow_effect")) {
                        // if (dd.equals(ArrowComponent.arrow_fire)) defendingPlayer.set);
                        PotionEffect pe = null;
//                        if (dd.equals(ArrowComponent.arrow_frost1)) pe = new PotionEffect(PotionEffectType.SLOW, 40, 1);
//                        if (dd.equals(ArrowComponent.arrow_frost2)) pe = new PotionEffect(PotionEffectType.SLOW, 80, 1);
//                        if (dd.equals(ArrowComponent.arrow_frost3))
//                            pe = new PotionEffect(PotionEffectType.SLOW, 120, 1);
//                        if (dd.equals(ArrowComponent.arrow_poison1))
//                            pe = new PotionEffect(PotionEffectType.POISON, 40, 1);
//                        if (dd.equals(ArrowComponent.arrow_poison2))
//                            pe = new PotionEffect(PotionEffectType.POISON, 80, 1);
//                        if (dd.equals(ArrowComponent.arrow_poison3))
//                            pe = new PotionEffect(PotionEffectType.POISON, 120, 1);

                        if (pe != null) ((LivingEntity) event.getEntity()).addPotionEffect(pe);
                    }
                }
            }

            if (arrow.getShooter() instanceof Player) {
                attacker = (Player) arrow.getShooter();
                ItemStack inHand = attacker.getEquipment().getItemInMainHand();
                if (!CustomMaterial.getMid(inHand).asString().contains("_bow"))
                    inHand = attacker.getEquipment().getItemInOffHand();

                CustomMaterial cMat = CustomMaterial.getCustomMaterial(inHand);
                if (cMat != null) cMat.onRangedAttack(event, inHand);
            }
        }

        if (event.getDamager() instanceof Player) {
            attacker = (Player) event.getDamager();

            ItemStack inHand = attacker.getInventory().getItemInMainHand();
            CustomMaterial cMat = CustomMaterial.getCustomMaterial(inHand);

            if (cMat != null)
                cMat.onAttack(event, inHand);
            else
                event.setDamage(RCore.minDamage); *//* Non-civcraft items only do 0.5 damage. *//*

            if (Enchantments.hasEnchantment(inHand, CustomEnchantment.Critical))
                CriticalEnchantment.run(event, inHand);
        }

        if (defendingPlayer != null) {
            *//* Search equipt items for defense event. *//*
            for (ItemStack stack : defendingPlayer.getEquipment().getArmorContents()) {
                CustomMaterial cmat = CustomMaterial.getCustomMaterial(stack);
                if (cmat != null) cmat.onDefense(event, stack);
            }
            if (event.getDamager() instanceof LivingEntity) {
                LivingEntity le = (LivingEntity) event.getDamager();
                ItemStack chestplate = defendingPlayer.getEquipment().getChestplate();
                if (Enchantments.hasEnchantment(chestplate, CustomEnchantment.Thorns)) {
                    le.damage(event.getDamage() * Enchantments.getLevelEnchantment(chestplate, CustomEnchantment.Thorns), defendingPlayer);
                }
            }
        }*/
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void OnInventoryClose(InventoryCloseEvent event) {
        GuiHelper.clearInventoryStack(event.getPlayer().getUniqueId());
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

        /* Search and execute any enhancements */
        for (int i = 0; i < event.getEntity().getInventory().getSize(); i++) {
            ItemStack stack = event.getEntity().getInventory().getItem(i);
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
        ItemStack[] contents = event.getEntity().getInventory().getArmorContents();
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

        // event.getEntity().getInventory().getArmorContents()

        boolean keepInventory = Boolean.parseBoolean(event.getEntity().getWorld().getGameRuleValue("keepInventory"));
        String playerName = event.getEntity().getName();
        if (!keepInventory) {
            Task.syncDelayed(RCore.getInstance(), () -> {
                Player player = Bukkit.getPlayer(playerName);
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
            case DROP_ALL_CURSOR:
            case DROP_ONE_CURSOR:
                otherStack = event.getCursor();
                result = itemDrop(event, player, otherStack);
                break;
            case PICKUP_ALL: // Взять стак
            case PICKUP_HALF:// Взять пол стака
            case PICKUP_ONE: // Взять один
            case PICKUP_SOME://
            case COLLECT_TO_CURSOR: // Взять все что найду двойным кликом //TODO Отменить это опасное действие
                result = itemFromInventory(event, player, clickInv, topInv, clickStack);
                break;
            case DROP_ALL_SLOT:
            case DROP_ONE_SLOT:  // выбросить предмет
                result = itemFromInventory(event, player, clickInv, topInv, clickStack);
                if (result) break;
                result = itemDrop(event, player, clickStack);
                break;
            case PLACE_ALL:  //
            case PLACE_ONE:  // положить
            case PLACE_SOME: //
                otherStack = event.getCursor();
                result = itemToInventory(event, player, clickInv, topInv, otherStack);
                break;
            case SWAP_WITH_CURSOR: // меняем то что в руке на то что в инвентаре
                otherStack = event.getCursor();
                result = itemFromInventory(event, player, clickInv, topInv, clickStack);
                if (result) break;
                result = itemToInventory(event, player, clickInv, topInv, otherStack);
                break;
            case MOVE_TO_OTHER_INVENTORY:
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
                break;
            case HOTBAR_SWAP:
            case HOTBAR_MOVE_AND_READD:
                otherInv = event.getWhoClicked().getInventory();
                otherStack = otherInv.getItem(event.getHotbarButton());
                result = itemFromInventory(event, player, clickInv, topInv, clickStack);
                if (result) break;
                result = itemToInventory(event, player, otherInv, topInv, clickStack);
                if (result) break;
                result = itemFromInventory(event, player, otherInv, topInv, otherStack);
                if (result) break;
                result = itemToInventory(event, player, clickInv, topInv, otherStack);
                break;
            default:
                break;
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
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();

        ItemStack stack = event.getOldCursor();
        if ((stack == null) || stack.getType().equals(Material.AIR)) return;

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
