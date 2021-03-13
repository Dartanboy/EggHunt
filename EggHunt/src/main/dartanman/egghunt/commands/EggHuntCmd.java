/**
 * EggHunt Command Executor
 * @author Dartanman (Austin Dart)
 */

package main.dartanman.egghunt.commands;

import main.dartanman.egghunt.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EggHuntCmd implements CommandExecutor {
	private Main plugin;

	/**
	 * Constructs the command with the Main class for access.
	 * @param pl
	 *  The Main class
	 */
	public EggHuntCmd(Main pl) {
		this.plugin = pl;
	}

	/**
	 * Shows the help menu to the player
	 * @param player
	 *  The player to show the help menu to
	 */
	private void showHelpMenu(Player player) {
		player.sendMessage(ChatColor.BLUE + "EggHunt Help Menu:");
		player.sendMessage(ChatColor.GOLD + "/egghunt getegg" + ChatColor.WHITE + ": " + ChatColor.YELLOW
				+ "Get an egg to place for the Egg Hunt!");
	}

	/**
	* Method that runs when a player does a command. Never returns false in EggHunt because I'd rather send my own error message, not Spigot's.
	* @param sender
	*  whoever/whatever sent the command
	* @param cmd
	*  the command that was run
	* @param label
	*  the alias of the command that was run
	* @param args
	*  array of arguments added to the end of the command that was run
	* @return
	*  the success state of the command - used to determine whether or not Spigot should send an error message to the sender
	**/
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "You must be a Player to do that!");
			return false;
		}
		Player player = (Player) sender;
		if (args.length == 0) {
			showHelpMenu(player);
			return true;
		}
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?")) {
				showHelpMenu(player);
				return true;
			}
			if (args[0].equalsIgnoreCase("getegg")) {
				if (!player.hasPermission("egghunt.setup")) {
					player.sendMessage(ChatColor.translateAlternateColorCodes('&',
							this.plugin.getConfig().getString("Messages.NoPermission")));
					return true;
				}
				this.plugin.giveEgg(player);
				return true;
			}
			player.sendMessage(ChatColor.translateAlternateColorCodes('&',
					this.plugin.getConfig().getString("Messages.InvalidArgs")));
			return true;
		}
		player.sendMessage(ChatColor.translateAlternateColorCodes('&',
				this.plugin.getConfig().getString("Messages.InvalidArgs")));
		return true;
	}
}
