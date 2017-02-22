package mx.com.rodel.puppet;

import java.util.List;
import java.util.logging.Level;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import mx.com.rodel.InventoryRescuer;
import mx.com.rodel.utils.Utils;

/** This file is part of Inventory Rescurer Spigot Resource
*
* <br><br>
* More info about Terms of Use {@link http://business.rodel.com.mx/terms} 
* 
* @author rodel77
*/
public class ItemStackPuppet {
	private InventoryRescuer pl =  InventoryRescuer.getInstance();
	
	private ItemStack itemstack;
	@SuppressWarnings({ "deprecation", "unchecked" })
	public ItemStackPuppet(){
		String[] material = pl.getNode("puppet.material").toString().split(":");
		
		if(Utils.isNumeric(material[0])){
			String sid = "0";
			if(material.length!=1){
				if(Utils.isNumeric(material[1])){
					sid = material[1];
				}else{
					pl.getLogger().log(Level.WARNING, "Invalid ItemRescurer item meta-id ({0}) must be number!", material[1]);
				}
			}
			itemstack = new ItemStack(Integer.parseInt(material[0]), 1, (short) 0, Byte.parseByte(sid));
		}else{
			pl.getLogger().log(Level.WARNING, "Invalid ItemRescurer item material ({0}) must be number!", material[0]);
		}
		
		ItemMeta meta = itemstack.getItemMeta();
		if(((boolean) pl.getNode("puppet.glow"))==true){
			meta.addEnchant(Enchantment.DURABILITY, 1, false);
			meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		}
		
		meta.setDisplayName(Utils.colorize(pl.getNode("puppet.name").toString()));
		meta.setLore(Utils.colorize((List<String>) pl.getNode("puppet.lore")));
		itemstack.setItemMeta(meta);
	}
	
	public ItemStack getItemStack(){
		return itemstack;
	}
}
