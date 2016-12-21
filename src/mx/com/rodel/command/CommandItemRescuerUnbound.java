package mx.com.rodel.command;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import mx.com.rodel.InventoryRescuer;
import mx.com.rodel.itemrescuer.ItemStackRescurer;
import mx.com.rodel.utils.Utils;

public class CommandItemRescuerUnbound extends SubCommand{

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(String[] args) {
		if(args.length==0){
			if(testPermission(sender, "itemrescuer.unbound.self")){
				if(sender instanceof Player){
					Player player = (Player) sender;
					if(player.getItemInHand()!=null && new ItemStackRescurer().hasMark(player.getItemInHand())){
						player.setItemInHand(new ItemStackRescurer().removeMark(player.getItemInHand(), player));
						Utils.msg(sender, "&cYou unbound succesfully "+Utils.itemNameBetterizer(player.getItemInHand()), Utils.ITEM_RESCUER);
					}else{
						Utils.msg(sender, "&cYou don't have item in hand or isn't bounded!", Utils.ITEM_RESCUER);
					}
				}else{
					Utils.msg(sender, "&cYou must be a player to use this command!", Utils.ITEM_RESCUER);
				}
			}
		}else if(args.length==1){
			if(testPermission(sender, "itemrescuer.unbound.other")){
				if(Bukkit.getPlayer(args[0])!=null){
					Player player = Bukkit.getPlayer(args[0]);
					if(player.getItemInHand()!=null && new ItemStackRescurer().hasMark(player.getItemInHand())){
						player.setItemInHand(new ItemStackRescurer().removeMark(player.getItemInHand(), player));
						Utils.msg(sender, "&cYou unbound succesfully "+player.getName()+" "+Utils.itemNameBetterizer(player.getItemInHand()), Utils.ITEM_RESCUER);
						Utils.msg(player, InventoryRescuer.getInstance().getNode("itemrescurer.messages.get.bound").toString().replace("{ITEM}", Utils.itemNameBetterizer(player.getItemInHand())), Utils.ITEM_RESCUER);
					}else{
						Utils.msg(sender, "&cYou don't have item in hand or isn't bounded!", Utils.ITEM_RESCUER);
					}
				}else{
					Utils.msg(sender, "&cInvalid player!", Utils.ITEM_RESCUER);
				}
			}
		}else{
			printUsage(sender);
		}
		return true;
	}

	@Override
	public String getName() {
		return "unbound";
	}

	@Override
	public String getHelp() {
		return "unbound [player] &6(Unbound item from you or other player)";
	}

	@Override
	public String getPermission() {
		return "unbound";
	}

	@Override
	public boolean isOnlyPlayers() {
		return false;
	}

}
