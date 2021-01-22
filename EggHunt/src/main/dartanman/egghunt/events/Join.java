/**
 * PlayerJoinEvent Listener
 * @author Dartanman (Austin Dart)
 */

package main.dartanman.egghunt.events;

import java.util.UUID;
import main.dartanman.egghunt.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Join implements Listener {
	public Main plugin;

	/**
	 * Constructs the PlayerJoinEvent Listener class with the Main class for access
	 * @param pl
	 *  The Main class
	 */
	public Join(Main pl) {
		this.plugin = pl;
	}

	/**
	 * Listens to an event. In this case, the PlayerJoinEvent
	 * In this particular method, it is used to add new players to EggData.yml so the plugin can keep track of how many eggs they've found.
	 * @param event
	 *  The event to listen to
	 */
	@EventHandler(priority = EventPriority.MONITOR)
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		String uuidStr = uuid.toString();
		if (this.plugin.getEggDataFile().get("Players." + uuidStr) == null) {
			this.plugin.getEggDataFile().set("Players." + uuidStr + ".Count", Integer.valueOf(0));
			this.plugin.saveEggDataFile();
		}
	}
}
