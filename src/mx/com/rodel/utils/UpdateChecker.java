package mx.com.rodel.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import mx.com.rodel.InventoryRescuer;

/** This file is part of Inventory Rescurer Spigot Resource
*
* <br><br>
* More info about Terms of Use {@link http://business.rodel.com.mx/terms} 
* 
* @author rodel77
*/
@SuppressWarnings("unused")
public class UpdateChecker {
	private static final int resource = 25073;
	private static final String CHANGELOG_URL = "http://resources.rodel.com.mx/ir/lastchanges.txt#%%__USER__%%";
	
	public static String getLast(){
		final String uid = "%%__USER__%%";
		final String rid = "%%__RESOURCE__%%";
		final String nonce = "%%__NONCE__%%";
		
		final AtomicString as = new AtomicString(InventoryRescuer.getInstance().getDescription().getVersion());
		
		Bukkit.getScheduler().runTaskAsynchronously(InventoryRescuer.getInstance(), new Runnable() {
			public void run() {
				try {
					HttpURLConnection spigotApi = (HttpURLConnection)new URL("http://www.spigotmc.org/api/general.php").openConnection();
					spigotApi.setDoOutput(true);
					spigotApi.setRequestMethod("POST");
					spigotApi.getOutputStream().write(("key=98BE0FE67F88AB82B4C197FAF1DC3B69206EFDCC4D3B80FC83A00037510B99B4&resource=" + resource).getBytes("UTF-8"));
					as.set(new BufferedReader(new InputStreamReader(spigotApi.getInputStream())).readLine());
				} catch (Exception e) {
					Bukkit.getLogger().warning("Error getting updates info!");
				}
			}
		});
		
		return as.get();
	}
	
	public static String changelog(){
		final AtomicString as = new AtomicString();
		
		Bukkit.getScheduler().runTaskAsynchronously(InventoryRescuer.getInstance(), new Runnable() {
			public void run() {
				try {
					String changelog = "";
					HttpURLConnection changelogr = (HttpURLConnection)new URL(CHANGELOG_URL).openConnection();
					BufferedReader r = new BufferedReader(new InputStreamReader(changelogr.getInputStream()));
					String line = "";
					while((line = r.readLine())!=null){
						changelog += "\n"+line;
					}
					as.set(changelog);
				} catch (Exception e) {
					Bukkit.getLogger().warning("Error getting updates info!");
				}
			}
		});
		
		return as.get();
	}
}
