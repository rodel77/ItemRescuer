package mx.com.rodel.command;

import mx.com.rodel.InventoryRescuer;
import mx.com.rodel.itemrescuer.InventorySave;
import mx.com.rodel.utils.DataManager;
import mx.com.rodel.utils.Utils;

public class CommandRescue extends SubCommand{

	@Override
	public boolean onCommand(String[] args) {
		InventorySave save = DataManager.getInventorySave(player.getUniqueId());
		
		if(save!=null){
			DataManager.restore(save, player);
		}else{
			Utils.msg(sender, InventoryRescuer.getInstance().getConfig().getString("itemrescurer.messages.nodata"), Utils.ITEM_RESCUER);
		}
		
		return true;
	}

	@Override
	public String getName() {
		return "rescue";
	}

	@Override
	public String getHelp() {
		return "rescue";
	}

	@Override
	public String getPermission() {
		return "rescue";
	}

	@Override
	public boolean isOnlyPlayers() {
		return true;
	}

}
