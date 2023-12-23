package ua.rozipp.core.items;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;
import redempt.redlib.RedLib;
import ua.rozipp.core.enchantment.Enchantments;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/*
 * Original serializer by Phil2812 (https://forums.bukkit.org/threads/serialize-inventory-to-single-string-and-vice-versa.92094/)
 */

public class InventorySerializer {
	
	@SuppressWarnings("deprecation")
	private static String getSerializedItemStack(ItemStack is) {
        StringBuilder serializedItemStack = new StringBuilder();
        String isType = is.getType().getKey().asString();
        serializedItemStack.append("t@").append(isType);
       
        if (is.getDurability() != 0)
        {
            String isDurability = String.valueOf(is.getDurability());
            serializedItemStack.append("&d@").append(isDurability);
        }
       
        if (is.getAmount() != 1)
        {
            String isAmount = String.valueOf(is.getAmount());
            serializedItemStack.append("&a@").append(isAmount);
        }
       
        Map<Enchantment,Integer> isEnch = is.getEnchantments();
        if (isEnch.size() > 0)
        {
            for (Map.Entry<Enchantment,Integer> ench : isEnch.entrySet())
            {
                serializedItemStack.append("&e@").append(ench.getKey().getKey()).append("@").append(ench.getValue());
            }
        }

        ItemMeta meta = is.getItemMeta();
        if (meta != null && meta.hasLore()) {
        	for (String lore : Objects.requireNonNull(meta.getLore())) {
        		char[] encode = Base64Coder.encode(lore.getBytes());
        		String encodedString = new String(encode);
        		serializedItemStack.append("&l@").append(encodedString);
        	}
        }
        
        if (meta != null) {
            meta.getDisplayName();
            serializedItemStack.append("&D@").append(meta.getDisplayName());
        }
        
        CustomMaterial mat = CustomMaterial.getCustomMaterial(is);
        if (mat != null) {
        	serializedItemStack.append("&C@").append(mat.getMid());
        }

        return serializedItemStack.toString();
	}

    //TODO Переделать под ItemStackBuilder
	private static ItemStack getItemStackFromSerial(String serial) {
        ItemStack is = null;
        boolean createdItemStack = false;
        List<String> lore = new LinkedList<>();
       
        //String[] serializedItemStack = serializedBlock[1].split("&");
        String[] serializedItemStack = serial.split("&");
        for (String itemInfo : serializedItemStack)
        {
            String[] itemAttribute = itemInfo.split("@");
            if (itemAttribute[0].equals("t"))
            {
                is = ItemHelper.createItemStack(itemAttribute[1], 1);
                createdItemStack = true;
            }
            else if (itemAttribute[0].equals("d") && createdItemStack)
            {
                is.setDurability(Short.parseShort(itemAttribute[1]));
            }
            else if (itemAttribute[0].equals("a") && createdItemStack)
            {
                is.setAmount(Integer.parseInt(itemAttribute[1]));
            }
            else if (itemAttribute[0].equals("e") && createdItemStack)
            {
                Enchantments.addEnchantment(is, Enchantments.enchantmentList.get(Integer.valueOf(itemAttribute[1])), Integer.valueOf(itemAttribute[2]));
            } 
            else if (itemAttribute[0].equals("l") && createdItemStack) 
            {
            	byte[] decode = Base64Coder.decode(itemAttribute[1]);
            	String decodedString = new String(decode);                	
            	lore.add(decodedString);
            }
            else if (itemAttribute[0].equals("D") && createdItemStack) {
            	ItemMeta meta = is.getItemMeta();
            	if (meta != null) {
            		meta.setDisplayName(itemAttribute[1]);
            	}
            	is.setItemMeta(meta);
            } else if (itemAttribute[0].equals("C")) {
            	CustomMaterial mat = CustomMaterial.getCustomMaterial(NamespacedKey.fromString(itemAttribute[1]));
                //TODO Переделать под ItemStackBuilder
            }
        }
        
        if (lore.size() > 0) {
        	ItemMeta meta = is.getItemMeta();
        	if (meta != null) {
        		meta.setLore(lore);
        		is.setItemMeta(meta);
        	}
        }
        
        return is;
	}
	
    public static String InventoryToString (Inventory invInventory)
    {
        StringBuilder serialization = new StringBuilder(invInventory.getSize() + ";");
        for (int i = 0; i < invInventory.getSize(); i++)
        {
            ItemStack is = invInventory.getItem(i);
            if (is != null)
            {
            	String serializedItemStack = getSerializedItemStack(is);
                serialization.append(i).append("#").append(serializedItemStack).append(";");
            }
        }
        
        if (invInventory instanceof PlayerInventory) {
        	serialization.append("&PINV@");
        	PlayerInventory pInv = (PlayerInventory)invInventory;
        	
        	for (ItemStack stack : pInv.getArmorContents()) {
                serialization.append(getSerializedItemStack(stack)).append(";");
            }
        }
        
        return serialization.toString();
    }
   
    public static void StringToInventory (Inventory inv, String inString)
    {
    	String invString;
    	String[] inventorySplit = null; 
    	
    	if (inv instanceof PlayerInventory) {
    		inventorySplit = inString.split("&PINV@");
    		invString = inventorySplit[0];
    	} else {
    		invString = inString;
    	}
    	
        String[] serializedBlocks = invString.split(";");
        inv.clear();
       
        for (int i = 1; i < serializedBlocks.length; i++)
        {
            String[] serializedBlock = serializedBlocks[i].split("#");
            int stackPosition = Integer.parseInt(serializedBlock[0]);
           
            if (stackPosition >= inv.getSize())
            {
                continue;
            }
           
            ItemStack is = getItemStackFromSerial(serializedBlock[1]);
            inv.setItem(stackPosition, is);
        }
        
        if (inv instanceof PlayerInventory) {
        	PlayerInventory pInv = (PlayerInventory)inv;
        	invString = inventorySplit[1];
            String[] serializedBlocksArmor = invString.split(";");
           
            ItemStack[] contents = new ItemStack[4];
            for (int i = 0; i < serializedBlocksArmor.length; i++)
            { 
                ItemStack is = getItemStackFromSerial(serializedBlocksArmor[i]);
                contents[i] = is;
            }
            
            pInv.setArmorContents(contents);
        }

    }
	
}
