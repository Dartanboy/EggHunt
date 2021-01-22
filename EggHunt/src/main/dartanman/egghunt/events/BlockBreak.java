/**
 * BlockBreakEvent Listener
 * @author Dartanman (Austin Dart)
 */

package main.dartanman.egghunt.events;

import java.util.ArrayList;
import java.util.List;
import main.dartanman.egghunt.Main;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreak implements Listener {
	public Main plugin;

	/**
	 * Constructs the BlockBreakEvent Listener class with the Main class for access
	 * @param pl
	 *  The Main class
	 */
	public BlockBreak(Main pl) {
		this.plugin = pl;
	}

	/**
	 * Listens to an event. In this case, the BlockBreakEvent
	 * In this particular method, it is used to decide whether or not a block can be destroyed, as Easter Eggs cannot be destroyed.
	 * @param event
	 *  The event to listen to
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlock();
		if (block.getType() == Material.PLAYER_HEAD || block.getType() == Material.PLAYER_WALL_HEAD) {
			Location blockLoc = block.getLocation();
			String locStr = String.valueOf(blockLoc.getWorld().getName()) + "/" + blockLoc.getBlockX() + "/"
					+ blockLoc.getBlockY() + "/" + blockLoc.getBlockZ();
			List<String> eggList = new ArrayList<>();
			eggList = this.plugin.getEggDataFile().getStringList("EggLocations");
			if (eggList.contains(locStr)) {
				event.setCancelled(true);
				player.sendMessage(ChatColor.translateAlternateColorCodes('&',
						this.plugin.getConfig().getString("Messages.CannotDestroy")));
			}
		}
	}
}
