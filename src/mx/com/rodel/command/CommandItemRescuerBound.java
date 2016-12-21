package mx.com.rodel.command;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import mx.com.rodel.InventoryRescuer;
import mx.com.rodel.itemrescuer.ItemStackRescurer;
import mx.com.rodel.utils.Utils;

public class CommandItemRescuerBound extends SubCommand{

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(String[] args) {
		if(args.length==0){
			if(testPermission(sender, "itemrescuer.bound.self")){
				if(sender instanceof Player){
					Player player = (Player) sender;
					if(player.getItemInHand()!=null && !new ItemStackRescurer().hasMark(player.getItemInHand())){
						if(!new ItemStackRescurer().isDenied(player.getItemInHand())){
							player.setItemInHand(new ItemStackRescurer().addMark(player.getItemInHand(), player));
							Utils.msg(sender, "&cYou bound succesfully "+Utils.itemNameBetterizer(player.getItemInHand()), Utils.ITEM_RESCUER);
						}else{
							Utils.msg(sender, InventoryRescuer.getInstance().getNode("itemrescurer.messages.get.denied").toString(), Utils.ITEM_RESCUER);
						}
					}else{
						Utils.msg(sender, "&cYou don't have item in hand or is bounded!", Utils.ITEM_RESCUER);
					}
				}else{
					Utils.msg(sender, "&cYou must be a player to use this command!", Utils.ITEM_RESCUER);
				}
			}
		}else if(args.length==1){
			if(testPermission(sender, "itemrescuer.bound.other")){
				if(Bukkit.getPlayer(args[0])!=null){
					Player player = Bukkit.getPlayer(args[0]);
					if(player.getItemInHand()!=null && !new ItemStackRescurer().hasMark(player.getItemInHand())){
						if(!new ItemStackRescurer().isDenied(player.getItemInHand())){
							player.setItemInHand(new ItemStackRescurer().addMark(player.getItemInHand(), player));
							Utils.msg(sender, "&cYou bound succesfully "+player.getName()+" "+Utils.itemNameBetterizer(player.getItemInHand()), Utils.ITEM_RESCUER);
							Utils.msg(player, InventoryRescuer.getInstance().getNode("itemrescurer.messages.get.bound").toString().replace("{ITEM}", Utils.itemNameBetterizer(player.getItemInHand())), Utils.ITEM_RESCUER);
						}else{
							Utils.msg(sender, InventoryRescuer.getInstance().getNode("itemrescurer.messages.get.denied").toString(), Utils.ITEM_RESCUER);
						}
					}else{
						Utils.msg(sender, "&cYou don't have item in hand or is bounded!", Utils.ITEM_RESCUER);
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
		return "bound";
	}

	@Override
	public String getHelp() {
		return "bound [player] &6(Bound item to you or player)";
	}

	@Override
	public String getPermission() {
		return "bound";
	}

	@Override
	public boolean isOnlyPlayers() {
		return false;
	}

}
