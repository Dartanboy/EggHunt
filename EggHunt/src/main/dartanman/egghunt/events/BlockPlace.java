/**
 * BlockPlaceEvent Listener
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
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlace implements Listener {
	public Main plugin;

	/**
	 * Constructs the BlockPlaceEvent Listener class with the Main class for access
	 * @param pl
	 *  The Main class
	 */
	public BlockPlace(Main pl) {
		this.plugin = pl;
	}

	/**
	 * Listens to an event. In this case, the BlockPlaceEvent
	 * In this particular method, it is used to save the locations of newly placed Easter Eggs.
	 * @param event
	 *  The event to listen to
	 */
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlockPlaced();
		if ((block.getType() == Material.PLAYER_HEAD || block.getType() == Material.PLAYER_WALL_HEAD)
				&& this.plugin.placing.contains(player.getName())) {
			Location blockLoc = block.getLocation();
			String locStr = String.valueOf(blockLoc.getWorld().getName()) + "/" + blockLoc.getBlockX() + "/"
					+ blockLoc.getBlockY() + "/" + blockLoc.getBlockZ();
			List<String> eggList = new ArrayList<>();
			eggList = this.plugin.getEggDataFile().getStringList("EggLocations");
			eggList.add(locStr);
			if (eggList.contains("Placeholder"))
				eggList.remove("Placeholder");
			this.plugin.getEggDataFile().set("EggLocations", eggList);
			this.plugin.getEggDataFile().set("EggCount",
					Integer.valueOf(this.plugin.getEggDataFile().getInt("EggCount") + 1));
			this.plugin.saveEggDataFile();
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Messages.SuccessfullyAdded")));
		}
	}
}
