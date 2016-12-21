package mx.com.rodel.puppet;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import mx.com.rodel.InventoryRescuer;
import mx.com.rodel.utils.Utils;

/** This file is part of Inventory Rescurer Spigot Resource
*
* <br><br>
* More info about Terms of Use {@link http://business.rodel.com.mx/terms} 
* 
* @author rodel77
*/
public class PuppetTrigger implements Listener{
	@EventHandler(priority=EventPriority.HIGH)
	public void onPlayerDeathEvent(PlayerDeathEvent e){
		boolean find = false;
		int i = 0;
		for(ItemStack c : e.getEntity().getInventory().getContents()){
			if(c!=null && c.getType()!=Material.AIR && c.isSimilar(new ItemStackPuppet().getItemStack())){
				find=true;
			}
			if(!find){
				i+=1;
			}
		}
		
		if(find){
			e.setKeepInventory(true);
			e.setKeepLevel(true);
			e.setDroppedExp(0);
			for(Entity nearby : e.getEntity().getNearbyEntities(5, 5, 5)){
				if(nearby instanceof Item){
					if(Utils.hasItemInInventory(e.getEntity(), ((Item) nearby).getItemStack()) || Utils.playerHasArmor(e.getEntity(), ((Item) nearby).getItemStack())){
						nearby.remove();
					}
				}
			}
			
			Utils.consumeItem(e.getEntity().getInventory(), i);
			
			int puppets = 0;
			
			for(ItemStack item : e.getEntity().getInventory().getContents()){
				if(item!=null && item.getType()!=Material.AIR && item.isSimilar(new ItemStackPuppet().getItemStack())){
					puppets+=item.getAmount();
				}
			}
			
			Utils.msg(e.getEntity(), InventoryRescuer.getInstance().getNode("puppet.messages.died").toString().replace("{REMAINIG}", puppets+""), Utils.PUPPET_ITEM);
		}
	}
}
