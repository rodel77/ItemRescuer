package mx.com.rodel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;

import com.google.common.io.Files;

import mx.com.rodel.command.ItemRescuerCommands;
import mx.com.rodel.itemrescuer.ItemRescurerListener;
import mx.com.rodel.itemrescuer.ItemRescurerMyPetListener;
import mx.com.rodel.itemrescuer.ItemStackRescurer;
import mx.com.rodel.puppet.ItemStackPuppet;
import mx.com.rodel.puppet.PuppetTrigger;
import mx.com.rodel.utils.UpdateChecker;
import mx.com.rodel.utils.Utils;

/** This file is part of Inventory Rescurer Spigot Resource
*
* <br><br>
* More info about Terms of Use {@link http://business.rodel.com.mx/terms} 
* 
* @author rodel77
*/
public class InventoryRescuer extends JavaPlugin{
	
	private HashMap<String, Object> nodes = new HashMap<>();
	private static InventoryRescuer instance;
	String changelog;
	
	public boolean hasPlayerVault = false;
	public Plugin playervault;
	private Metrics metrics;
	
	public List<ShapelessRecipe> recipes = new ArrayList<>();
	
	public static FileConfiguration data = new YamlConfiguration();
	
	public static InventoryRescuer getInstance(){
		return instance;	
	}
	
	@Override
	public void onEnable() {
		instance=this;
		getCommand("itemrescurer").setExecutor(new ItemRescuerCommands());
		
		ArrayList<String> allowed = new ArrayList<>();
		allowed.add(InventoryType.CRAFTING.toString());
		allowed.add(InventoryType.CREATIVE.toString());
		allowed.add(InventoryType.ENCHANTING.toString());
		allowed.add(InventoryType.ENDER_CHEST.toString());
		allowed.add(InventoryType.PLAYER.toString());
		allowed.add(InventoryType.WORKBENCH.toString());
		allowed.add("PLUGIN_VAULT");
		
		nodes.put("itemrescurer.material", "351:1");
		nodes.put("itemrescurer.name", "&9ItemRescuer");
		nodes.put("itemrescurer.glow", true);
		nodes.put("itemrescurer.lore", new String[] {"&e&oPlace this item in crafting bench", "&e&oAnd then place one item to save it", "&dOnly have 1 use!"});
		nodes.put("itemrescurer.craft.enabled", false);
		nodes.put("itemrescurer.craft.shape", new String[] {"42,341,42", "42,351:1,42", "42,138,42"});
		nodes.put("itemrescurer.crafting.allowed", new String[] {"256-258", "267-279", "283-286", "290-294", "298-317", "346"});
		nodes.put("itemrescurer.crafting.denied", new String[] {"268-271", "290"});
		nodes.put("itemrescurer.displayonitem", true);
		nodes.put("itemrescurer.bound.craft", true);
		nodes.put("itemrescurer.bound.drop", true);
		nodes.put("itemrescurer.deaths", -1);
		nodes.put("itemrescurer.break", "&cOne item has been unbounded!");
		nodes.put("itemrescurer.deathsmsg", "&9Remaining Deaths: &6{DEATHS}");
		nodes.put("itemrescurer.markeditem", "&9SoulBound: &6{PLAYER}");
		nodes.put("itemrescurer.header", "&4[&6Item&eRescurer&4]");
		nodes.put("itemrescurer.messages.respawn.enabled", true);
		nodes.put("itemrescurer.messages.nodata", "&cYou don't have data to rescue");
		nodes.put("itemrescurer.messages.respawn.string", "&eYou save {COUNT} item/s!");
		nodes.put("itemrescurer.messages.get.enabled", true);
		nodes.put("itemrescurer.messages.get.string", "&aYou get {AMOUNT} ItemRescuer item");
		nodes.put("itemrescurer.messages.get.denied", "&cYou can't bound this kind of item");
		nodes.put("itemrescurer.messages.get.bound", "&aYou bound {ITEM} succesfully");
		nodes.put("itemrestriction.enabled", true);
		nodes.put("itemrestriction.allowedinventories", allowed);
		nodes.put("itemrestriction.candrop", false);
		nodes.put("itemrestriction.canpick", false);
		nodes.put("itemrestriction.canitemframe", false);
		nodes.put("itemrestriction.canpetspick", false);
		nodes.put("itemrestriction.armorstand", false);
		nodes.put("itemrestriction.fastmode", false);
		
		nodes.put("puppet.material", "351:2");
		nodes.put("puppet.name", "&2Puppet");
		nodes.put("puppet.glow", true);
		nodes.put("puppet.lore", new String[] {"&e&oUse this item for save inventory on dead", "&dOnly have 1 use!"});
		nodes.put("puppet.craft.enabled", false);
		nodes.put("puppet.craft.shape", Arrays.asList(new String[] {"42,341,42", "42,351:2,42", "42,416,42"}));
		nodes.put("puppet.header", "&2[&4Puppet&2]");
		nodes.put("puppet.messages.get", "&aYou get {AMOUNT} puppets");
		nodes.put("puppet.messages.died", "&aYou save your inventory but you lose 1 puppet, now you have {REMAINIG} puppets!");
		
		nodes.put("messages.permission", "&cYou don't have permission to use this command");
		nodes.put("messages.get.stringfull", "&cYou have full inventory then some items has been dropped");
		nodes.put("updatechecker", true);
		nodes.put("metrics", true);
		
		loadConfiguration();
		loadData();
		
//		instance=this;
		
		loadData();
		loadConfiguration();

		for(Plugin plugin : getServer().getPluginManager().getPlugins()){
			if(plugin.getName().equalsIgnoreCase("PlayerVaults")){
				getLogger().log(Level.ALL, "ItemRecurer linked to PlayerVaults! (Now your players can place soulbound items in vaults)");
				hasPlayerVault=true;
				playervault = plugin;
			}
		}
		
		init();
		getServer().getPluginManager().registerEvents(new ItemRescurerListener(), this);
		getServer().getPluginManager().registerEvents(new ItemStackRescurer(), this);
		getServer().getPluginManager().registerEvents(new PuppetTrigger(), this);
		
		if(getServer().getPluginManager().getPlugin("MyPet")!=null){
			getServer().getPluginManager().registerEvents(new ItemRescurerMyPetListener(), this);
		}
		
		try {
			if((Boolean) getNode("updatechecker")){
				getLogger().info(UpdateChecker.getLast().equals(getDescription().getVersion()) ? "No updates available!" : "Update "+UpdateChecker.getLast()+" available!");
			}
			
			enableMetrics();
		} catch (Exception e) {
			getLogger().warning("Error getting online information!");
		}
	}
	
	@Override
	public void onDisable() {
		File d = new File(getDataFolder().getAbsoluteFile()+"/data.yml");
		
		if(d.exists()){
			try {
				Files.copy(d, new File(getDataFolder().getAbsolutePath()+"/data_backup.yml"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void init(){
		loadConfiguration();
		ItemStackRescurer it = new ItemStackRescurer();
		it.init();
		if((boolean)getNode("itemrescurer.craft.enabled")){
			ShapedRecipe recipe = Utils.getRecipe((List<String>) getNode("itemrescurer.craft.shape"), it.getItemStack());
			Bukkit.addRecipe(recipe);
			
		}
		if((boolean)getNode("puppet.craft.enabled")){
			ShapedRecipe recipe = Utils.getRecipe((List<String>) getNode("puppet.craft.shape"), new ItemStackPuppet().getItemStack());
			Bukkit.addRecipe(recipe);
		}
		
		
		getLogger().log(Level.INFO, "{0} items initialized!", recipes.size());
	}
	
	public void enableMetrics(){
		if((Boolean) getNode("metrics")){
			try{
				metrics = new Metrics(this);
				metrics.start();
			} catch (IOException e){
				Bukkit.getLogger().warning("Error on submit stats!");
			}
		}else{
			try {
				metrics.disable();
			} catch (IOException e){
				Bukkit.getLogger().warning("Error on disable stats!");
			}
		}
	}
	
	public void loadData(){
		try {
			data.load(getDataFolder().getAbsolutePath()+"/data.yml");
			saveData();
		} catch (FileNotFoundException e) {
			try {
				saveData();
				loadConfiguration();
			} catch (Exception e2) {
			}
		} catch (Exception e) {
			getLogger().warning("Error loading data.yml");
			e.printStackTrace();
		}
	}
	
	public void saveData(){
		try {
			data.save(getDataFolder().getAbsolutePath()+"/data.yml");
		} catch (IOException e) {
			getLogger().warning("Error saving data.yml");
			e.printStackTrace();
		}
	}
	
	public void loadConfiguration(){
		try {
			getConfig().load(getDataFolder().getAbsolutePath()+"/config.yml");
			getConfig().options().header("All inventory list https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/event/inventory/InventoryType.html");
			saveConfiguration();
		} catch (FileNotFoundException e) {
			try {
				getConfig().save(getDataFolder().getAbsolutePath()+"/config.yml");
				saveConfiguration();
				loadConfiguration();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	public void saveConfiguration(){
		for(Entry<String, Object> node : nodes.entrySet()){
			if(!getConfig().contains(node.getKey())){
				getConfig().set(node.getKey(), node.getValue());
			}
		}
		saveConfig();
	}
	
	public Object getNode(String node){
		return getConfig().get(node, nodes.get(node));
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, Inventory> getVaultInventories(){
		try {
			return (HashMap<String, Inventory>) playervault.getClass().getMethod("getOpenInventories").invoke(playervault);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean playerHasVault(UUID player){
		if(hasPlayerVault && getVaultInventories().size()!=0){
			for(Entry<String, Inventory> inv : getVaultInventories().entrySet()){
				if(inv.getKey().startsWith(player.toString())){
					return true;
				}
			}
		}
		return false;
	}
}
