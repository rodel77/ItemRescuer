package mx.com.rodel.itemrescuer;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

import de.Keyle.MyPet.api.event.MyPetInventoryActionEvent;
import mx.com.rodel.InventoryRescuer;

/** This file is part of Inventory Rescurer Spigot Resource
*
* <br><br>
* More info about Terms of Use {@link http://business.rodel.com.mx/terms} 
* 
* @author rodel77
*/
public class ItemRescurerMyPetListener implements Listener{
	HashMap<UUID, Long> timestamp = new HashMap<>();
	HashMap<UUID, MyPetInventoryActionEvent> event = new HashMap<>();
	
	@EventHandler
	public void on(MyPetInventoryActionEvent e){
		timestamp.put(e.getOwner().getPlayerUUID(), System.currentTimeMillis());
		event.put(e.getOwner().getPlayerUUID(), e);
	}
	
	@EventHandler
	public void on(PlayerPickupItemEvent e){
		if(timestamp.containsKey(e.getPlayer().getUniqueId()) && !(boolean) InventoryRescuer.getInstance().getNode("itemrestriction.canpetspick")){
			if((timestamp.get(e.getPlayer().getUniqueId())-System.currentTimeMillis())>-5){
				/*MyPetInventoryActionEvent ev = event.get(e.getPlayer().getUniqueId());
				for(Entity entity : ev.getPet().getEntity().get().getNearbyEntities(2, 2, 2)){
					if(entity instanceof Item){
						e.setCancelled(new ItemStackRescurer().hasMark(((Item) entity).getItemStack()) ? true : e.isCancelled());
					}
				}*/
				
				e.setCancelled(new ItemStackRescurer().hasMark(e.getItem().getItemStack()) ? true : e.isCancelled());
			}
		}
	}
	
}
