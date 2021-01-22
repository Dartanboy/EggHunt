/**
 * PlayerInteractEvent Listener
 * @author Dartanman (Austin Dart)
 */

package main.dartanman.egghunt.events;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import main.dartanman.egghunt.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class Interact implements Listener {
	public Main plugin;

	/**
	 * Constructs the PlayerInteractEvent Listener class with the Main class for access
	 * @param pl
	 *  The Main class
	 */
	public Interact(Main pl) {
		this.plugin = pl;
	}

	/**
	 * Listens to an event. In this case, the PlayerInteractEvent
	 * In this particular method, it is used for running code related to a Player finding an egg.
	 * @param event
	 *  The event to listen to
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		String uuidStr = uuid.toString();
		Action action = event.getAction();
		// Prevents running twice due to the OFFHAND also being a thing.
		if (event.getHand() != EquipmentSlot.HAND) {
			return;
		}
		
		// Makes sure they are clicking a block - not the air
		if (action == Action.LEFT_CLICK_BLOCK || action == Action.RIGHT_CLICK_BLOCK) {
			Block block = event.getClickedBlock();
			// Checks it is indeed a head (eggs are heads)
			if (block.getType() == Material.PLAYER_HEAD || block.getType() == Material.PLAYER_WALL_HEAD) {
				Location blockLoc = block.getLocation();
				String locStr = String.valueOf(blockLoc.getWorld().getName()) + "/" + blockLoc.getBlockX() + "/"
						+ blockLoc.getBlockY() + "/" + blockLoc.getBlockZ();
				List<String> eggList = new ArrayList<>();
				eggList = this.plugin.getEggDataFile().getStringList("EggLocations");
				
				// Checks that there is indeed an egg where they clicked - not some other head
				if (eggList.contains(locStr)) {
					if (this.plugin.getEggDataFile().get("Players." + uuidStr + "." + locStr) == null) {
						this.plugin.getEggDataFile().set("Players." + uuidStr + ".Count", Integer
								.valueOf(this.plugin.getEggDataFile().getInt("Players." + uuidStr + ".Count") + 1));
						player.sendMessage(ChatColor.BLUE + "You found an egg!");
						player.sendMessage(ChatColor.BLUE + "You have found a total of " + ChatColor.GREEN
								+ this.plugin.getEggDataFile().getInt("Players." + uuidStr + ".Count") + " Eggs.");
						player.sendMessage(ChatColor.BLUE + "There are " + ChatColor.GREEN
								+ this.plugin.getEggDataFile().getInt("EggCount") + " Eggs" + ChatColor.BLUE
								+ " in all.");
						String msg1 = ChatColor.translateAlternateColorCodes('&',
								this.plugin.getConfig().getString("Messages.FoundEgg"));
						String msg2 = msg1.replace("{found}",
								this.plugin.getEggDataFile().getString("Players." + uuidStr + ".Count"));
						String msg = msg2.replace("{total}", this.plugin.getEggDataFile().getString("EggCount"));
						player.sendMessage(msg);
						this.plugin.getEggDataFile().set("Players." + uuidStr + "." + locStr, Boolean.valueOf(true));
						this.plugin.saveEggDataFile();
						if (this.plugin.getEggDataFile().getInt("Players." + uuidStr + ".Count") == this.plugin
								.getEggDataFile().getInt("EggCount")
								&& this.plugin.getConfig().getBoolean("BroadcastWhenAllFound.Enabled"))
							Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig()
									.getString("BroadcastWhenAllFound.Message").replace("{player}", player.getName())));
						if (this.plugin.getEggDataFile().getInt("Players." + uuidStr + ".Count") == this.plugin
								.getEggDataFile().getInt("EggCount")
								&& this.plugin.getConfig().getBoolean("CommandRewardsForAllFound.Enabled"))
							for (String cmd : this.plugin.getConfig()
									.getStringList("CommandRewardsForAllFound.Commands"))
								Bukkit.dispatchCommand((CommandSender) Bukkit.getConsoleSender(),
										cmd.replace("{player}", player.getName()));
					} else {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&',
								this.plugin.getConfig().getString("Messages.AlreadyFound")));
					}
				this.plugin.saveEggDataFile();
			}
			}
		}
	}
}
