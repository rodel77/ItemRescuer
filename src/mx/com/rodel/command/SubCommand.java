package mx.com.rodel.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import mx.com.rodel.InventoryRescuer;
import mx.com.rodel.utils.Utils;

public abstract class SubCommand {
	
	public Player player;
	public CommandSender sender;
	public InventoryRescuer pl;
	
	public boolean setSender(CommandSender sender){
		if(sender instanceof Player){
			player = (Player) sender;
		}else{
			if(isOnlyPlayers()){
				return false;
			}
		}
		
		this.sender = sender;
		return true;
	}
	
	public void printUsage(CommandSender sender){
		if(sender.isOp()){
			Utils.msg(player, "/ir "+getHelp()+" (itemrescuer."+getPermission()+")", Utils.ITEM_RESCUER);
		}else{
			Utils.msg(player, "/ir "+getHelp(), Utils.ITEM_RESCUER);
		}
	}
	
	public static boolean testPermission(CommandSender sender, String permission){
		if(sender.hasPermission(permission)){
			return true;
		}else{
			Utils.msg(sender, InventoryRescuer.getInstance().getNode("messages.permission").toString(), Utils.ITEM_RESCUER);
			return false;
		}
	}
	
	public abstract boolean onCommand(String[] args);
	public abstract String getName();
	public abstract String getHelp();
	public abstract String getPermission();
	public abstract boolean isOnlyPlayers();
}
