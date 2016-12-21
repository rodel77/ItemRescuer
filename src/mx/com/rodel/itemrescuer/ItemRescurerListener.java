package mx.com.rodel.itemrescuer;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import mx.com.rodel.InventoryRescuer;
import mx.com.rodel.utils.DataManager;
import mx.com.rodel.utils.Utils;

/** This file is part of Inventory Rescurer Spigot Resource
*
* <br><br>
* More info about Terms of Use {@link http://business.rodel.com.mx/terms} 
* 
* @author rodel77
*/
@SuppressWarnings("deprecation")
public class ItemRescurerListener implements Listener {
	ItemStackRescurer isr = new ItemStackRescurer();
	
//	private static final Map<UUID, InventorySave> INVENTORY_SAVE = new HashMap<>();
//	private static final Map<UUID, FastInventorySave> FAST_INVENTORY_SAVE = new HashMap<>();
	
	@EventHandler
	public void onPlayerDropItemEvent(PlayerDropItemEvent e){
		if(isr.hasMark(e.getItemDrop().getItemStack()) && !e.getPlayer().hasPermission("itemrescurer.itemrestriction.drop")){
			int i = 0;
			boolean space = false;
			for(ItemStack cont : e.getPlayer().getInventory().getContents()){
				++i;
				if(i!=37 && i!=38 && i!=39 && i!=40 && i!=41 && cont==null ){
					space = true;
				}
			}
			e.setCancelled(space ? !(Boolean) InventoryRescuer.getInstance().getNode("itemrestriction.candrop") : space);
		}
	}
	
	@EventHandler
	public void onBlockDispenseEvent(BlockDispenseEvent e){
		if(isr.hasMark(e.getItem())){
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerInteractAtEntityEvent(PlayerInteractEntityEvent e){
		if(e.getRightClicked() instanceof ItemFrame && !(Boolean) InventoryRescuer.getInstance().getNode("itemrestriction.canitemframe") && isr.hasMark(e.getPlayer().getItemInHand()) && !e.getPlayer().hasPermission("itemrescurer.itemrestriction.itemframe")){
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void on(PlayerInteractAtEntityEvent e){
		if(e.getRightClicked() instanceof ArmorStand && !(Boolean) InventoryRescuer.getInstance().getNode("itemrestriction.armorstand") && 
				isr.hasMark(e.getPlayer().getItemInHand()) && 
				!e.getPlayer().hasPermission("itemrescurer.itemrestriction.armorstand")){
			
			e.setCancelled(true);
		}
	}
	
	@SuppressWarnings("unchecked")
	@EventHandler(priority=EventPriority.LOWEST)
	public void onInventoryClickEvent(InventoryClickEvent e){
		if(e.getSlot()!=-999 && !e.getWhoClicked().hasPermission("itemrescurer.itemrestriction.inventory")){
			try {
				boolean deny = false;
				ArrayList<String> invs = (ArrayList<String>) InventoryRescuer.getInstance().getNode("itemrestriction.allowedinventories");
				
				
				if(!invs.contains(e.getInventory().getType().toString()) && !InventoryRescuer.getInstance().playerHasVault(e.getWhoClicked().getUniqueId()) && invs.contains("PLUGIN_VAULT")){
					deny=true;
				}
				
				if(deny){
					if(e.getClickedInventory().getType()!=InventoryType.WORKBENCH){
						if(isr.hasMark(e.getCurrentItem()) || isr.hasMark(e.getCursor())){
							e.setCancelled(true);
						}
						
						if(e.getHotbarButton()!=-1){
							ItemStack hotbar = e.getWhoClicked().getInventory().getItem(e.getHotbarButton())==null ? new ItemStack(Material.AIR) : e.getWhoClicked().getInventory().getItem(e.getHotbarButton());
							if(isr.hasMark(hotbar)){
								e.setCancelled(true);
							}
						}
					}
				}
			} catch (NullPointerException e2) {
			}
		}
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onPlayerDeathEvent(PlayerDeathEvent e){
		if(!e.getKeepInventory()){
			if(e.getDrops().isEmpty() && !Utils.isEmpty(e.getEntity())){
				for(Entity entity : e.getEntity().getNearbyEntities(5, 5, 5)){
					if(entity instanceof Item && isr.isOwner(e.getEntity(), ((Item) entity).getItemStack())){
						if(isr.isOwner(e.getEntity(), ((Item) entity).getItemStack())){
							entity.remove();
						}
					}
				}
			}else{
				for(ItemStack stack : new ArrayList<>(e.getDrops())){
					if(stack!=null && stack.getType()!=Material.AIR && isr.isOwner(e.getEntity(), stack)){
						e.getDrops().remove(stack);
					}
				}
			}

			DataManager.setInventorySave(new InventorySave(e.getEntity(), e.getKeepInventory()));
		}
	}
	
	@EventHandler
	public void onPlayerPickupItemEvent(PlayerPickupItemEvent e){
		if((boolean)InventoryRescuer.getInstance().getNode("itemrescurer.displayonitem")){
			e.getItem().setCustomName(isr.getSoulboundLore(e.getItem().getItemStack()));
			e.getItem().setCustomNameVisible(true);
		}
		if(!(boolean)InventoryRescuer.getInstance().getNode("itemrestriction.canpick")){
			e.setCancelled(isr.hasMark(e.getItem().getItemStack()) ? !isr.isOwner(e.getPlayer(), e.getItem().getItemStack()) : e.isCancelled());
		}
	}
	
	@EventHandler
	public void onPlayerRespawnEvent(PlayerRespawnEvent e){
		InventorySave inv = DataManager.getInventorySave(e.getPlayer().getUniqueId());
		
		if(inv!=null){
			if(!inv.canKeepInventory()){
				DataManager.restore(inv, e.getPlayer());
			}
		}
	}
}