package mx.com.rodel.command;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import mx.com.rodel.InventoryRescuer;
import mx.com.rodel.itemrescuer.ItemStackRescurer;
import mx.com.rodel.utils.Utils;

public class CommandItemRescuer extends SubCommand{

	@Override
	public boolean onCommand(String[] args) {
		if(args.length>=1){
			if(args[0].equalsIgnoreCase("rescue")){
				List<String> ag = new LinkedList<>(Arrays.asList(args));
				ag.remove(0);
				ItemRescuerCommands.executeCommand(new CommandRescue(), sender, ag.toArray(new String[] {}));
			}else if(args[0].equalsIgnoreCase("bound")){
				List<String> ag = new LinkedList<>(Arrays.asList(args));
				ag.remove(0);
				ItemRescuerCommands.executeCommand(new CommandItemRescuerBound(), sender, ag.toArray(new String[] {}));
			}else if(args[0].equalsIgnoreCase("unbound")){
				List<String> ag = new LinkedList<>(Arrays.asList(args));
				ag.remove(0);
				ItemRescuerCommands.executeCommand(new CommandItemRescuerUnbound(), sender, ag.toArray(new String[] {}));
			}else if(Bukkit.getPlayer(args[0])!=null){
				if(args.length==2){
					if(Utils.isNumeric(args[1])){
						if(testPermission(sender, "itemrescurer.use")){
							Player player = Bukkit.getPlayer(args[0]);
							boolean dropped = false;
							for (int i = 0; i < Integer.parseInt(args[1]); i++) {
								if(player.getInventory().firstEmpty()!=-1){
									player.getInventory().addItem(new ItemStackRescurer().getItemStack());
								}else{
									dropped = true;
									player.getWorld().dropItem(player.getLocation(), new ItemStackRescurer().getItemStack());
								}
							}
							if((Boolean) InventoryRescuer.getInstance().getNode("itemrescurer.messages.get.enabled")){
								Utils.msg(player, InventoryRescuer.getInstance().getNode("itemrescurer.messages.get.string").toString().replace("{AMOUNT}", args[1]), Utils.ITEM_RESCUER);
								if(dropped){
									Utils.msg(player, InventoryRescuer.getInstance().getNode("messages.get.stringfull").toString(), Utils.ITEM_RESCUER);
								}
							}
							Utils.msg(sender, "&aYou gave "+args[1]+" ItemRescurer item to "+player.getDisplayName(), Utils.ITEM_RESCUER);
						}
					}else{
						Utils.msg(sender, "&c"+'"'+args[1]+'"'+" must be integer!", Utils.ITEM_RESCUER);
					}
				}else{
					ItemRescuerCommands.help(sender);
				}
			}else{
				ItemRescuerCommands.help(sender);
			}
		}else{
			ItemRescuerCommands.help(sender);
		}
		return true;
	}

	@Override
	public String getName() {
		return "itemrescurer";
	}

	@Override
	public String getHelp() {
		return "itemrescurer <unbound|bound|player> <amount|player>";
	}

	@Override
	public String getPermission() {
		return "itemrescurer";
	}

	@Override
	public boolean isOnlyPlayers() {
		return false;
	}

}
