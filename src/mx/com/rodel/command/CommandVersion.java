package mx.com.rodel.command;

import mx.com.rodel.InventoryRescuer;
import mx.com.rodel.utils.UpdateChecker;
import mx.com.rodel.utils.Utils;

public class CommandVersion extends SubCommand{

	@Override
	public boolean onCommand(String[] args) {
		Utils.msg(sender, "&aItemRescuer &6"+InventoryRescuer.getInstance().getDescription().getVersion(), Utils.ITEM_RESCUER);
		String last = UpdateChecker.getLast();
		if(!last.equals(InventoryRescuer.getInstance().getDescription().getVersion())){
			Utils.msg(sender, "&a"+last+" available!", Utils.ITEM_RESCUER);
		}else{
			Utils.msg(sender, "&cNo updates available!", Utils.ITEM_RESCUER);
		}
		return true;
	}

	@Override
	public String getName() {
		return "version";
	}

	@Override
	public String getHelp() {
		return "version &6(See the plugin version)";
	}

	@Override
	public String getPermission() {
		return "version";
	}

	@Override
	public boolean isOnlyPlayers() {
		return false;
	}
	
}
