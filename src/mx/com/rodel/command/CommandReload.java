package mx.com.rodel.command;

import mx.com.rodel.InventoryRescuer;
import mx.com.rodel.itemrescuer.ItemStackRescurer;
import mx.com.rodel.utils.Utils;

public class CommandReload extends SubCommand{

	@Override
	public boolean onCommand(String[] args) {
		InventoryRescuer.getInstance().loadConfiguration();
		Utils.msg(sender, "&aConfiguration reloaded...", Utils.ITEM_RESCUER);
		new ItemStackRescurer().removeRecipes();
		Utils.msg(sender, "&aRemoving recipes...", Utils.ITEM_RESCUER);
		InventoryRescuer.getInstance().init();
		Utils.msg(sender, "&a"+InventoryRescuer.getInstance().recipes.size()+" recipes initialized...", Utils.ITEM_RESCUER);
		return true;
	}

	@Override
	public String getName() {
		return "reload";
	}

	@Override
	public String getHelp() {
		return "reload &6(Reload configuration)";
	}

	@Override
	public String getPermission() {
		return "reload";
	}

	@Override
	public boolean isOnlyPlayers() {
		return false;
	}
	
}
