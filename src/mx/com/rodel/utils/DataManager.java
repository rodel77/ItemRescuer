package mx.com.rodel.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import mx.com.rodel.InventoryRescuer;
import mx.com.rodel.itemrescuer.InventorySave;
import mx.com.rodel.itemrescuer.ItemStackRescurer;

public class DataManager {
	
//	public static List<ItemStack> getItemsFromPlayer(UUID player){
//		List<ItemStack> stack = new ArrayList<>();
//		if(InventoryRescuer.data.contains(String.valueOf(player))){
//			for(String item : InventoryRescuer.data.getStringList(String.valueOf(player))){
//				stack.add(Utils.stringItemer(item));
//			}
//		}
//		return stack;
//	}
	
	public static void restore(InventorySave inv, Player player){
		
		ItemStackRescurer isr = new ItemStackRescurer();
		
		int count = 0;
		for (int i = 0; i < inv.getContents().length; i++) {
			ItemStack content = inv.getContents()[i];
			if(isr.isOwner(player, content)){
				
				if(i!=39 && i!=38 && i!=37 && i!=36){
					++count;
					player.getInventory().setItem(i, content);
				}
			}
		}
		
			EntityEquipment equip = player.getEquipment();
			
			equip.setHelmet(isr.returnIfHasMarkAndOwner(inv.getHelmet(), player));
			equip.setChestplate(isr.returnIfHasMarkAndOwner(inv.getChesplate(), player));
			equip.setLeggings(isr.returnIfHasMarkAndOwner(inv.getLeggings(), player));
			equip.setBoots(isr.returnIfHasMarkAndOwner(inv.getBoots(), player));
			
			if((boolean) InventoryRescuer.getInstance().getNode("itemrescurer.messages.respawn.enabled")){
				if(inv.getHelmet()!=null && inv.getHelmet().getType()!=Material.AIR){
					++count;
				}
				
				if(inv.getChesplate()!=null && inv.getChesplate().getType()!=Material.AIR){
					++count;
				}
				
				if(inv.getLeggings()!=null && inv.getLeggings().getType()!=Material.AIR){
					++count;
				}
				
				if(inv.getBoots()!=null && inv.getBoots().getType()!=Material.AIR){
					++count;
				}
			}
		
		if(count!=0 && (boolean) InventoryRescuer.getInstance().getNode("itemrescurer.messages.respawn.enabled")){
			Utils.msg(player, InventoryRescuer.getInstance().getNode("itemrescurer.messages.respawn.string").toString().replace("{COUNT}", count+""), Utils.ITEM_RESCUER);
		}
		
		removePlayer(player.getUniqueId());
		InventoryRescuer.getInstance().saveData();
		InventoryRescuer.getInstance().loadData();
	}
	
	public static void removePlayer(UUID player){
		InventoryRescuer.data.set(player.toString(), null);
	}
	
	public static InventorySave getInventorySave(UUID player){
		if(InventoryRescuer.data.contains(player.toString())){
			List<ItemStack> contents = translateBack(getStringList(player+".contents"));
			ItemStack helmet = getItem(player+".helmet");
			ItemStack chesplate = getItem(player+".chesplate");
			ItemStack leggings = getItem(player+".leggings");
			ItemStack boots = getItem(player+".boots");
			
			InventorySave save = new InventorySave(Bukkit.getPlayer(player), false);
			save.setContents(contents.toArray(new ItemStack[] {}));
			save.setHelmet(helmet);
			save.setChesplate(chesplate);
			save.setLeggings(leggings);
			save.setBoots(boots);
			return save;
		}
		
		return null;
	}
	
	private static ItemStack getItem(String node){
		if(InventoryRescuer.data.contains(node)){
			return Utils.stringItemer(InventoryRescuer.data.getStringList(node).get(0));
		}
		return new ItemStack(Material.AIR);
	}
	
	private static List<String> getStringList(String node){
		return InventoryRescuer.data.getStringList(node);
	}
	
	public static void setInventorySave(InventorySave save){
		UUID uid = save.getPlayer().getUniqueId();
		
		List<Boolean> results = new ArrayList<>();
		
		results.add(setItems(uid, "contents", Arrays.asList(save.getContents())));
		results.add(setItems(uid, "helmet", createArray(save.getHelmet())));
		results.add(setItems(uid, "chesplate", createArray(save.getChesplate())));
		results.add(setItems(uid, "leggings", createArray(save.getLeggings())));
		results.add(setItems(uid, "boots", createArray(save.getBoots())));
		
		if(results.contains(true)){
			setNode(uid, "timestamp", System.currentTimeMillis());
		}
	}
	
	private static void setNode(UUID player, String node, Object value){
		InventoryRescuer.data.set(player+"."+node, value);
		InventoryRescuer.getInstance().saveData();
		InventoryRescuer.getInstance().loadData();
	}
	
	private static boolean setItems(UUID player, String subnode, List<ItemStack> item){
		if(item==null){
			return false;
		}
		
		boolean delete = true;
		
		for(ItemStack stack : item){
			if(stack!=null){
				delete = false;
			}
		}
		
		if(delete){
			return false;
		}
			
		InventoryRescuer.data.set(player+"."+subnode, translate(item));
		InventoryRescuer.getInstance().saveData();
		InventoryRescuer.getInstance().loadData();
		
		return true;
	}
	
	private static List<ItemStack> createArray(ItemStack stack){
		if(stack!=null && stack.getType()!=Material.AIR){
			return Arrays.asList(new ItemStack[] {stack});
		}
		return null;
	}
	
	private static List<String> translate(List<ItemStack> items){
		List<String> t = new ArrayList<>();
		for(ItemStack item : items){
			if(item!=null){
				t.add(Utils.itemStringer(item));
			}
		}
		return t;
	}
	
	private static List<ItemStack> translateBack(List<String> strings){
		List<ItemStack> t = new ArrayList<>();
		for(String string : strings){
			t.add(Utils.stringItemer(string));
		}
		return t;
	}
}
