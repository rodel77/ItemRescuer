package mx.com.rodel.itemrescuer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import mx.com.rodel.InventoryRescuer;
import mx.com.rodel.puppet.ItemStackPuppet;
import mx.com.rodel.utils.Utils;

/** This file is part of Inventory Rescurer Spigot Resource
*
* <br><br>
* More info about Terms of Use {@link http://business.rodel.com.mx/terms} 
* 
* @author rodel77
*/
public class ItemStackRescurer implements Listener{
	private static final String MARK = ChatColor.translateAlternateColorCodes('&', "&m&a&r&k&r");
	private static final String MARK_R = ChatColor.translateAlternateColorCodes('&', "&r&m&a&r");
	private static final String MARK_UUID = ChatColor.translateAlternateColorCodes('&', "&m&e&e&f&d&r");
	private ItemStack itemstack;
	private InventoryRescuer pl;
	
	@SuppressWarnings({ "deprecation", "unchecked" })
	public ItemStackRescurer(){
		pl = InventoryRescuer.getInstance();
		String[] material = pl.getNode("itemrescurer.material").toString().split(":");
		
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
		if(((boolean) pl.getNode("itemrescurer.glow"))==true){
			meta.addEnchant(Enchantment.DURABILITY, 1, false);
			meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		}
		
		meta.setDisplayName(Utils.colorize(pl.getNode("itemrescurer.name").toString()));
		meta.setLore(Utils.colorize((List<String>) pl.getNode("itemrescurer.lore")));
		itemstack.setItemMeta(meta);
	}
	
	public void addRecipe(ItemStack item) {
        ShapelessRecipe recipe = new ShapelessRecipe(item);
        recipe.addIngredient(item.getData());
        recipe.addIngredient(itemstack.getData());
        InventoryRescuer.getInstance().recipes.add(recipe);
        pl.getServer().addRecipe(recipe);
    }
	
	public void removeRecipes(){
		Iterator<Recipe> it = Bukkit.recipeIterator();
		while(it.hasNext()){
			Recipe r = it.next();
			
			String name = r.getClass().getName().replace(".", ",");
			name=name.split(",")[name.split(",").length-1];
			
			if(name.endsWith("ShapedRecipe")){
				if(r.getResult().equals(itemstack) || r.getResult().equals(new ItemStackPuppet().getItemStack())){
					it.remove();
				}
			}
			
			if(name.endsWith("ShapelessRecipe")){
				ShapelessRecipe sl = (ShapelessRecipe) r;
				for(ShapelessRecipe sl2 : InventoryRescuer.getInstance().recipes){
					if(sl.getIngredientList().equals(sl2.getIngredientList())){
						it.remove();
					}
				}
			}
		}
		pl.recipes.clear();
	}
	
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void init(){
		ItemStack item;
		List<String> allowedList = (List<String>) pl.getNode("itemrescurer.crafting.allowed");
		for(String allowed : allowedList){
			if(allowed.contains("-")){
				String[] splitted = allowed.split("-");
				int start = Integer.parseInt(splitted[0]);
				int end = Integer.parseInt(splitted[1]);
				for (int i = start; i <= end; i++) {
					item = new ItemStack(i, 1, (short) -1);
					
					if(!isDenied(item)){
						this.addRecipe(item);
					}
				}
			}else if(allowed.contains("#")){
				String[] splitted = allowed.split("#");
				int id = Integer.parseInt(splitted[0]);
				short data = Short.parseShort(splitted[1]);
				
				item = new ItemStack(id, 1, data);
				if(!isDenied(item)){
					this.addRecipe(item);
				}
			}else{
				int id = Integer.parseInt(allowed);
				
				item = new ItemStack(id, 1, (short) -1);
				if(!isDenied(item)){
					this.addRecipe(item);
				}
			}
		}
	}
	
	public ItemStack getItemStack(){
		return itemstack;
	}
	
	public boolean hasMark(ItemStack item){
		if(item!=null && item.getType()!=Material.AIR && item.hasItemMeta() && item.getItemMeta().hasLore()){
			for(String lore : item.getItemMeta().getLore()){
				if(lore.startsWith(MARK)){
					return true;				
				}
			}
		}
		return false;
	}
	
	public String getSoulboundLore(ItemStack item){
		if(item!=null && item.getType()!=Material.AIR && item.hasItemMeta() && item.getItemMeta().hasLore()){
			for(String lore : item.getItemMeta().getLore()){
				if(lore.startsWith(MARK)){
					return lore;
				}
			}
		}
		return "";
	}
	
	public boolean isOwner(Player player, ItemStack item){
		if(item!=null && item.getType()!=Material.AIR && item.hasItemMeta() && item.getItemMeta().hasLore()){
			for(String lore : item.getItemMeta().getLore()){
				if(lore.startsWith(MARK) && lore.contains(player.getName())){
					return true;
				}
				if(lore.startsWith(MARK_UUID) && lore.contains(player.getUniqueId().toString())){
					return true;
				}
			}
		}
		return false;
	}
	
	public int getRemainingDeaths(ItemStack item){
		if(item!=null && item.getType()!=Material.AIR && item.hasItemMeta() && item.getItemMeta().hasLore()){
			for(String lore : item.getItemMeta().getLore()){
				if(lore.startsWith(MARK_R)){
					String sf = lore.substring((MARK_R + pl.getNode("itemrescurer.deathsmsg").toString()).lastIndexOf('{'));
					int fi = sf.length();
					int i = 0;
					for(char c : sf.toCharArray()){
						if(!Character.isDigit(c)){
							fi=i;
						}
						i++;
					}
					
					return Integer.parseInt(sf.substring(0, fi));
				}
			}
		}
		return -1;
	}
	
	public ItemStack setRemaining(ItemStack item, int r){
		List<String> l = new ArrayList<>();
		if(item!=null && item.getType()!=Material.AIR && item.hasItemMeta() && item.getItemMeta().hasLore()){
			for(String lore : item.getItemMeta().getLore()){
				if(lore.startsWith(MARK_R)){
					l.add(ChatColor.translateAlternateColorCodes('&', MARK_R + pl.getNode("itemrescurer.deathsmsg").toString().replace("{DEATHS}", String.valueOf(r))));
				}else{
					l.add(lore);
				}
			}
		}
		ItemMeta meta = item.getItemMeta();
		meta.setLore(l);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack addMark(ItemStack item, Player player){
		if (!this.hasMark(item) && item!=null && item.getType()!=Material.AIR) {
            ItemMeta im = item.getItemMeta();
            List<String> lore = im.hasLore() ? im.getLore() : new ArrayList<String>();
            lore.add(MARK + Utils.colorize(pl.getNode("itemrescurer.markeditem").toString()).replace("{PLAYER}", player.getName()));
            if((int) pl.getNode("itemrescurer.deaths")!=-1){
            	lore.add(MARK_R + Utils.colorize(pl.getNode("itemrescurer.deathsmsg").toString().replace("{DEATHS}", pl.getNode("itemrescurer.deaths").toString())));
            }
            lore.add(MARK_UUID + Utils.colorize("&0"+player.getUniqueId()));
            im.setLore(lore);
            item.setItemMeta(im);
        }

        return item;
	}
	
	public ItemStack removeMark(ItemStack item, Player player){
		if(hasMark(item)){
			ItemMeta im = item.getItemMeta();
			
			List<String> lore = new ArrayList<>();
			for (int i = 0; i < im.getLore().size(); i++) {
				if(!im.getLore().get(i).startsWith(MARK) && !im.getLore().get(i).startsWith(MARK_R) && !im.getLore().get(i).startsWith(MARK_UUID)){
					lore.add(im.getLore().get(i));
				}
			}
			
			im.setLore(lore);
			item.setItemMeta(im);
			return item;
		}
		return item;
	}
	
	@SuppressWarnings({ "deprecation", "unchecked" }) 
	public boolean isDenied(ItemStack item){
		List<String> deniedList = (List<String>) pl.getNode("itemrescurer.crafting.denied");
		for(String denied : deniedList){
			if (denied.contains("-")) {
                String[] splitted = denied.split("-");
                int start = Integer.parseInt(splitted[0]);
                int end = Integer.parseInt(splitted[1]);

                if (item.getType().getId() >= start && item.getType().getId() <= end) {
                    return true;
                }
            } else if (denied.contains("#")) {
                String[] splitted = denied.split("#");
                int id = Integer.parseInt(splitted[0]);
                short data = Short.parseShort(splitted[1]);

                if (item.getType().getId() == id && item.getDurability() == data) {
                    return true;
                }
            } else {
                int id = Integer.parseInt(denied);

                if (item.getType().getId() == id) {
                    return true;
                }
            }
		}
		
		return false;
	}
	
	@EventHandler
	public void onPrepareItemCraftEvent(PrepareItemCraftEvent e){
		if(e.getViewers().size()!=0){
			List<ItemStack> matrix = new ArrayList<>(Arrays.asList(e.getInventory().getMatrix()));
			ItemStackRescurer item = new ItemStackRescurer();
	    	if(e.getRecipe() instanceof ShapelessRecipe){
	    		for(ItemStack i : e.getInventory().getMatrix()){
	    			if(i==null || i.getType() == Material.AIR){
	    				matrix.remove(i);
	    			}
	    		}
	    		
	    		if(matrix.contains(itemstack)){
	    			e.getInventory().setResult(itemstack);
	    		}
	    		
	    		if(matrix.size() != 2){
	    			return;
	    		}
	    		
	    		for(Iterator<ItemStack> it = matrix.iterator(); it.hasNext();){
	    			ItemStack is = it.next();
	    			if(!is.getItemMeta().hasLore()){
	    				continue;
	    			}
	    			
	    			if(is.getItemMeta().getLore().containsAll(item.getItemStack().getItemMeta().getLore())){
	    				it.remove();
	        			
	        			ItemStack result = matrix.get(0).clone();
	        			
	        			if(item.isDenied(result) || item.hasMark(result)){
	        				e.getInventory().setResult(null);
	        			}else{
	        				e.getInventory().setResult(item.addMark(result, (Player) e.getViewers().get(0)));
	        			}
	    			}
	    		}
	    	}
		}else{
			e.getInventory().setResult(null);
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler(priority=EventPriority.LOW)
	public void onInventoryClickEvent(InventoryClickEvent e){
		if(e.getCursor().equals(itemstack) && e.getSlot()!=-999 && e.getCurrentItem()!=null && e.getCurrentItem().getType()!=Material.AIR){
			boolean find = false;
			for(ShapelessRecipe r : pl.recipes){
				if(r.getResult().getType()==e.getCurrentItem().getType() && !isDenied(e.getCurrentItem())){
					find = true;
				}
			}
			if(find && !hasMark(e.getCurrentItem())){
				e.setCursor(new ItemStack(Material.AIR));
				e.setCancelled(true);
				addMark(e.getCurrentItem(), (Player) e.getWhoClicked());
			}
		}
	}
	
	public ItemStack returnIfHasMark(ItemStack item){
		return hasMark(item) ? item : new ItemStack(Material.AIR);
	}
	
	public ItemStack returnIfHasMarkAndOwner(ItemStack item, Player owner){
		return isOwner(owner, item) ? item : new ItemStack(Material.AIR);
	}
}
