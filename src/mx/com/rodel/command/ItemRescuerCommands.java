package mx.com.rodel.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import mx.com.rodel.utils.Utils;

public class ItemRescuerCommands implements CommandExecutor{
	public final static HashMap<String, SubCommand> subCommands = new HashMap<>();
	public final static List<String> sb = new ArrayList<>();
	public final static List<String> puppet = new ArrayList<>();
	
	public ItemRescuerCommands() {
		setup();
	}
	
	static {
		setup();
	}

	public static void setup(){
		if(subCommands.size()==0){
			subCommands.put("itemrescurer", new CommandItemRescuer());
			subCommands.put("reload", new CommandReload());
			subCommands.put("version", new CommandVersion());
			subCommands.put("puppet", new CommandPuppet());
		}
		
		if(sb.isEmpty()){
			sb.addAll(Arrays.asList(new String[] {"soulbound", "sb", "ir", "itemrescurer"}));
			puppet.addAll(Arrays.asList(new String[] {"puppet", "pup"}));
		}
	}
	
	public static boolean executeCommand(SubCommand cmd, CommandSender sender, String[] args){
		if(cmd.setSender(sender)){
			if(SubCommand.testPermission(sender, "itemrescurer."+cmd.getPermission())){
				return cmd.onCommand(args);
			}else{
				return false;
			}
		}else{
			Utils.msg(sender, "&cYou must be a player to execute this command", Utils.ITEM_RESCUER);
			return false;
		}
	}
	
	public static void help(CommandSender sender){
		for(SubCommand subCommand : subCommands.values()){
			if(subCommand instanceof CommandItemRescuer){
				Utils.msg(sender, "&4/ir itemrescurer <player> <amount> &6(Give itemrescuer to player)", Utils.ITEM_RESCUER);
				Utils.msg(sender, "&4/ir itemrescurer bound [player] &6(Bound item in hand to player)", Utils.ITEM_RESCUER);
				Utils.msg(sender, "&4/ir itemrescurer unbound [player] &6(Unbound item in hand to player)", Utils.ITEM_RESCUER);
				Utils.msg(sender, "&4/ir itemrescurer rescue &6(Unbound item in hand to player)", Utils.ITEM_RESCUER);
			}else{
				Utils.msg(sender, "/ir "+subCommand.getHelp(), Utils.ITEM_RESCUER);
			}
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(command.getName().equalsIgnoreCase("itemrescurer")){
			if(args.length>=1){
				if(sb.contains(args[0])){
					List<String> ag = new LinkedList<>(Arrays.asList(args));
					ag.remove(0);
					executeCommand(new CommandItemRescuer(), sender, ag.toArray(new String[] {}));
					return true;
				}
				
				if(puppet.contains(args[0])){
					List<String> ag = new LinkedList<>(Arrays.asList(args));
					ag.remove(0);
					executeCommand(new CommandPuppet(), sender, ag.toArray(new String[] {}));
					return true;
				}
				
				if(subCommands.containsKey(args[0].toLowerCase())){
					List<String> ag = new LinkedList<>(Arrays.asList(args));
					ag.remove(0);
					executeCommand(subCommands.get(args[0]), sender, ag.toArray(new String[] {}));
				}else{
					help(sender);
				}
			}else{
				help(sender);
			}
		}
		return true;
	}
}