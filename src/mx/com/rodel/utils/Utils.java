package mx.com.rodel.utils;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.material.MaterialData;

import mx.com.rodel.InventoryRescuer;

/** This file is part of Inventory Rescurer Spigot Resource
*
* <br><br>
* More info about Terms of Use {@link http://business.rodel.com.mx/terms} 
* 
* @author rodel77
*/
public class Utils {
	
	public static final String[] ALPAHBET = new String[] {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
	
	public static final int ITEM_RESCUER = 0;
	public static final int PUPPET_ITEM = 1;
	
	public static String colorize(String string){
		return ChatColor.translateAlternateColorCodes('&', string);
	}
	
	public static boolean isNumeric(String string){
		for(char c : string.toCharArray()){
			if(!Character.isDigit(c)){
				return false;
			}
		}
		return true;
	}
	
	public static List<String> colorize(List<String> list){
		List<String> l = new ArrayList<>();
		for(String string : list){
			l.add(colorize(string));
		}
		return l;
	}
	
	public static boolean isEmpty(Player playerInventory){
		for(ItemStack item : playerInventory.getInventory().getContents()){
			if(item!=null){
				return false;
			}
		}
		
		for(ItemStack item : playerInventory.getEquipment().getArmorContents()){
			if(item!=null && item.getType()!=Material.AIR){
				return false;
			}
		}
		return true;
	}
	
	public static boolean playerHasArmor(Player playerInventory, ItemStack compare){
		for(ItemStack armor : playerInventory.getEquipment().getArmorContents()){
			if(armor!=null && armor.isSimilar(compare)){
				return true;
			}
		}
		return false;
	}
	
	public static boolean hasSpace(Player playerInventory){
		for(ItemStack content : playerInventory.getInventory().getContents()){
			if(content==null || content.getType()==Material.AIR){
				return true;
			}
		}
		return false;
	}
	
	public static boolean hasItemInInventory(Player player, ItemStack item){
		for(ItemStack c : player.getInventory().getContents()){
			if(c!=null && c.equals(item)){
				return true;
			}
		}
		return false;
	}
	
	public static void msg(CommandSender sender, String message, int item){
		if(item==ITEM_RESCUER){
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', InventoryRescuer.getInstance().getNode("itemrescurer.header")+" "+message));
		}else if(item==PUPPET_ITEM){
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', InventoryRescuer.getInstance().getNode("puppet.header")+" "+message));
		}
	}
	
	public static String itemNameBetterizer(ItemStack item){
		if(item.hasItemMeta() && item.getItemMeta().hasDisplayName()){
			return item.getItemMeta().getDisplayName();
		}else{
			String name = item.getType().toString();
			String[] word = name.split("_");
			String finalName = "";
			for(String w : word){
				finalName+=w.substring(0, 1).toUpperCase() + w.substring(1).toLowerCase()+" ";
			}
			return finalName.substring(0, finalName.length()-1);
		}
	}
	
	@SuppressWarnings("deprecation")
	public static MaterialData toMaterialData(String material){
		int type = 0;
		byte data = 0;

		if(material.contains(":")){
			String[] m = material.split(":");
			if(m.length!=2){
				throw new Error("Error parsing material: Invalid material metadata ("+material+")");
			}
			
			if(isNumeric(m[0]) && isNumeric(m[1])){
				type=Integer.parseInt(m[0]);
				data=Byte.parseByte(m[1]);
			}
		}else{
			if(isNumeric(material)){
				type=Integer.parseInt(material);
			}
		}
		
		return new MaterialData(type, data);
	}
	
	public static ShapedRecipe getRecipe(List<String> lines, ItemStack result){
		if(lines.size()!=3){
			throw new Error("Error parsing recipe: The recipe don't have 3 lines");
		}
		
		ShapedRecipe recipe = new ShapedRecipe(result);
		
		recipe.shape("012", "345", "678");
		
		int i = 0;
		for(String line : lines){
			for(String ingredient : line.split(",")){
				recipe.setIngredient(String.valueOf(i).charAt(0)/*ALPAHBET[i].charAt(0)*/, toMaterialData(ingredient));
				++i;
			}
		}
		
		return recipe;
	}
	
	public static void consumeItem(Inventory inventory, int slot){
		ItemStack current = inventory.getItem(slot);
		if(current.getAmount()==0){
			inventory.setItem(slot, new ItemStack(Material.AIR));
		}else{
			current.setAmount(current.getAmount()-1);
			inventory.setItem(slot, current);
		}
	}
	
	public static String itemStringer(ItemStack item){
		YamlConfiguration blob = new YamlConfiguration();
		blob.set("i", item);
		return blob.saveToString();
	}
	
	public static ItemStack stringItemer(String item){
		YamlConfiguration blob = new YamlConfiguration();
		try {
			blob.loadFromString(item);
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}
		return blob.getItemStack("i", null);
	}
}
