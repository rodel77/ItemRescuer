package mx.com.rodel.command;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import mx.com.rodel.InventoryRescuer;
import mx.com.rodel.puppet.ItemStackPuppet;
import mx.com.rodel.utils.Utils;

public class CommandPuppet extends SubCommand{

	@Override
	public boolean onCommand(String[] args) {
		if(args.length==2){
			if(Bukkit.getPlayer(args[0])!=null){
				if(Utils.isNumeric(args[1])){
					Player player = Bukkit.getPlayer(args[0]);
					boolean dropped = false;
					for (int i = 0; i < Integer.parseInt(args[1]); i++) {
						if(player.getInventory().firstEmpty()!=-1){
							player.getInventory().addItem(new ItemStackPuppet().getItemStack());
						}else{
							dropped = true;
							player.getWorld().dropItem(player.getLocation(), new ItemStackPuppet().getItemStack());
						}
					}
					if((Boolean) InventoryRescuer.getInstance().getNode("itemrescurer.messages.get.enabled")){
						Utils.msg(player, InventoryRescuer.getInstance().getNode("puppet.messages.get").toString().replace("{AMOUNT}", args[1]), Utils.PUPPET_ITEM);
						if(dropped){
							Utils.msg(player, InventoryRescuer.getInstance().getNode("messages.get.stringfull").toString(), Utils.PUPPET_ITEM);
						}
					}
					Utils.msg(sender, "&aYou gave "+args[1]+" Puppet item to "+player.getDisplayName(), Utils.PUPPET_ITEM);
				}else{
					Utils.msg(sender, "&c"+'"'+args[1]+'"'+" must be integer!", Utils.PUPPET_ITEM);
				}
			}else{
				Utils.msg(sender, "&c"+'"'+args[1]+'"'+" is invalid player!", Utils.PUPPET_ITEM);
			}
		}else{
			ItemRescuerCommands.help(sender);
		}
		return true;
	}

	@Override
	public String getName() {
		return "puppet";
	}

	@Override
	public String getHelp() {
		return "puppet <player> <amount> &6(Give puppet to player)";
	}

	@Override
	public String getPermission() {
		return "pupet.use";
	}

	@Override
	public boolean isOnlyPlayers() {
		return false;
	}

}
