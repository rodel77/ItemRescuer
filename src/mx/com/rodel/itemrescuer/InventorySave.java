package mx.com.rodel.itemrescuer;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.EnchantingInventory;
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
public class InventorySave {
	private boolean keepInventory;
	private ItemStack[] contents;
	private ItemStack[] matrix;
	private ItemStack[] anvil;
	private ItemStack[] enchanting;
	private ItemStack helmet;
	private ItemStack chesplate;
	private ItemStack leggings;
	private ItemStack boots;
	private ItemStack cursor;
	private Player player;
	
	public InventorySave(Player player, boolean keep){
		contents = player.getInventory().getContents();
		helmet = player.getEquipment().getHelmet();
		chesplate = player.getEquipment().getChestplate();
		leggings = player.getEquipment().getLeggings();
		boots = player.getEquipment().getBoots();
		cursor = player.getOpenInventory().getCursor();
		matrix = new ItemStack[] {};
		anvil = new ItemStack[] {};
		enchanting = new ItemStack[] {};
		if(player.getOpenInventory().getTopInventory() instanceof CraftingInventory){
			matrix = ((CraftingInventory) player.getOpenInventory().getTopInventory()).getMatrix();
			
			for (int i = 0; i < matrix.length; i++) {
				matrix[i] = parse(matrix[i]);
			}
		}
		
		if(player.getOpenInventory().getTopInventory() instanceof AnvilInventory){
			anvil = ((AnvilInventory) player.getOpenInventory().getTopInventory()).getContents();
			
			for (int i = 0; i < anvil.length; i++) {
				anvil[i] = parse(anvil[i]);
			}
		}
		
		if(player.getOpenInventory().getTopInventory() instanceof EnchantingInventory){
			enchanting = ((EnchantingInventory) player.getOpenInventory().getTopInventory()).getContents();
			
			for (int i = 0; i < enchanting.length; i++) {
				enchanting[i] = parse(enchanting[i]);
			}
		}
		keepInventory = keep;
		this.player = player;
		
		for (int i = 0; i < contents.length; i++) {
			if(i!=39 && i!=38 && i!=37 && i!=36){
				contents[i]=parse(contents[i]);
			}else{
				contents[i]=null;
			}
		}

		helmet = parse(helmet);
		chesplate = parse(chesplate);
		leggings = parse(leggings);
		boots = parse(boots);
		cursor = parse(cursor);
	}
	
	public ItemStack parse(ItemStack i){
		if(parseDeadths(i)==0){
			player.getWorld().dropItemNaturally(player.getLocation(), new ItemStackRescurer().removeMark(i, player));
			Utils.msg(player, InventoryRescuer.getInstance().getNode("itemrescurer.break").toString(), Utils.ITEM_RESCUER);
			return new ItemStack(Material.AIR);
		}
		
		if(new ItemStackRescurer().hasMark(i)){
			return i;
		}
		
		return null;
	}
	
	public int parseDeadths(ItemStack stack){
		int r = new ItemStackRescurer().getRemainingDeaths(stack);
		if(r!=-1){
			stack = new ItemStackRescurer().setRemaining(stack, r-1);
		}
		return r;
	}
	
	public ItemStack[] getContents() {
		return contents;
	}
	
	public void setContents(ItemStack[] contents) {
		this.contents = contents;
	}

	public ItemStack getHelmet() {
		return helmet;
	}

	public void setHelmet(ItemStack helmet) {
		this.helmet = helmet;
	}

	public ItemStack getChesplate() {
		return chesplate;
	}

	public void setChesplate(ItemStack chesplate) {
		this.chesplate = chesplate;
	}

	public ItemStack getLeggings() {
		return leggings;
	}

	public void setLeggings(ItemStack leggings) {
		this.leggings = leggings;
	}

	public ItemStack getBoots() {
		return boots;
	}

	public void setBoots(ItemStack boots) {
		this.boots = boots;
	}

	public boolean canKeepInventory() {
		return keepInventory;
	}

	public void setKeepInventory(boolean keepInventory) {
		this.keepInventory = keepInventory;
	}
	
	public Player getPlayer(){
		return player;
	}
	
	public void setPlayer(Player player){
		this.player = player;
	}
	
	public ItemStack getCursor() {
		return cursor;
	}
	
	public void setCursor(ItemStack cursor) {
		this.cursor = cursor;
	}
	
	public ItemStack[] getMatrix(){
		return matrix;
	}
	
	public void setMatrix(ItemStack[] matrix){
		this.matrix = matrix;
	}
	
	public ItemStack[] getAnvil(){
		return anvil;
	}
	
	public void setAnvil(ItemStack[] anvil){
		this.anvil = anvil;
	}
	
	public ItemStack[] getEnchanting(){
		return enchanting;
	}
	
	public void setEnchanting(ItemStack[] enchanting){
		this.enchanting = enchanting;
	}
}
