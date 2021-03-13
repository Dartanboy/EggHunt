/**
 * JavaPlugin class for the entire EggHunt plugin
 * @author Dartanman (Austin Dart)
 */

package main.dartanman.egghunt;

import com.deanveloper.skullcreator.SkullCreator;
import com.deanveloper.skullcreator.SkullCreator1_16;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import main.dartanman.egghunt.commands.EggHuntCmd;
import main.dartanman.egghunt.events.BlockBreak;
import main.dartanman.egghunt.events.BlockPlace;
import main.dartanman.egghunt.events.Interact;
import main.dartanman.egghunt.events.Join;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	public FileConfiguration eggData = (FileConfiguration) new YamlConfiguration();

	public List<String> placing = new ArrayList<>();

	public File eggDataf;

	/**
	 * Enables the plugin by registering the several events and the /egghunt command. Also calls several methods to create/load YAML files.
	 */
	public void onEnable() {
		getConfig().options().copyDefaults(true);
		saveConfig();
		createFiles();
		getServer().getPluginManager().registerEvents((Listener) new BlockBreak(this), (Plugin) this);
		getServer().getPluginManager().registerEvents((Listener) new BlockPlace(this), (Plugin) this);
		getServer().getPluginManager().registerEvents((Listener) new Interact(this), (Plugin) this);
		getServer().getPluginManager().registerEvents((Listener) new Join(this), (Plugin) this);
		getCommand("egghunt").setExecutor((CommandExecutor) new EggHuntCmd(this));
	}

	/**
	* Would disable the plugin, but nothing has to happen to disable EggHunt. The server handles it automatically.
	**/
	public void onDisable() {
	}

	/**
	 * Saves the EggData file (EggData.yml)
	 */
	public void saveEggDataFile() {
		try {
			this.eggData.save(this.eggDataf);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("[EggHunt] Failed to save EggData.yml");
		}
	}

	/**
	 * Public access to EggData.yml
	 * @return EggData file
	 */
	public FileConfiguration getEggDataFile() {
		return this.eggData;
	}

	/**
	 * Creates all files I need to create. In this case, just EggData.yml
	 */
	private void createFiles() {
		this.eggDataf = new File(getDataFolder(), "EggData.yml");
		saveRes(this.eggDataf, "EggData.yml");
		this.eggData = (FileConfiguration) new YamlConfiguration();
		try {
			this.eggData.load(this.eggDataf);
		} catch (IOException | org.bukkit.configuration.InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Saves a resource
	 * @param file
	 *  The file we are saving
	 * @param name
	 *  The name we give it
	 */
	private void saveRes(File file, String name) {
		if (!file.exists()) {
			file.getParentFile().mkdirs();
			saveResource(name, false);
		}
	}

	/**
	 * Returns the base Minecraft version, or 0 if the version would be incompatible with EggHunt.
	 * @return
	 */
	private double getMinecraftVersion() {
		String ver = Bukkit.getVersion();
		if (ver.contains("1.15")) {
			return 1.15;
		} else if (ver.contains("1.16")) {
			return 1.16;
		} else {
			return 0d;
		}
	}

	/**
	 * Gives the player an Easter Egg
	 * @param player
	 *  The player to receive an Easter Egg
	 */
	public void giveEgg(Player player) {
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.GetEgg")));
		ItemStack egg;
		if (getMinecraftVersion() == 1.15) {
			egg = SkullCreator.itemFromBase64(
					"eyJ0aW1lc3RhbXAiOjE1ODY0ODk3MDY5MDksInByb2ZpbGVJZCI6IjczZWVlMmYwNDcxNzRhNmE4OWZkYjVmNzQzMDA1MWIxIiwicHJvZmlsZU5hbWUiOiJEYXJ0YW5tYW4iLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWU4YWM1NmZmZDEyNTgzNTA2NmIxNjZmZDhlZWFiMmYyYzNhNDIxNzQ2ZjYxM2NiMDcwOTA1ODlkNTQyZTEzZiJ9fX0");
		}else {
			egg = SkullCreator1_16.itemFromBase64(
					"eyJ0aW1lc3RhbXAiOjE1ODY0ODk3MDY5MDksInByb2ZpbGVJZCI6IjczZWVlMmYwNDcxNzRhNmE4OWZkYjVmNzQzMDA1MWIxIiwicHJvZmlsZU5hbWUiOiJEYXJ0YW5tYW4iLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWU4YWM1NmZmZDEyNTgzNTA2NmIxNjZmZDhlZWFiMmYyYzNhNDIxNzQ2ZjYxM2NiMDcwOTA1ODlkNTQyZTEzZiJ9fX0");
		}
		ItemMeta eggMeta = egg.getItemMeta();
		SkullMeta skullMeta = (SkullMeta) eggMeta;
		skullMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', getConfig().getString("EggInfo.EggName")));
		List<String> lore = new ArrayList<>();
		lore = getConfig().getStringList("EggInfo.EggLore");
		skullMeta.setLore(lore);
		egg.setItemMeta((ItemMeta) skullMeta);
		player.getInventory().addItem(new ItemStack[] { egg });
		if (!this.placing.contains(player.getName()))
			this.placing.add(player.getName());
	}
}
