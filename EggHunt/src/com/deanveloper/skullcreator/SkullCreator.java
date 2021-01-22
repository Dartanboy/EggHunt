package com.deanveloper.skullcreator;

/**
 * Comments were inadvertently deleted in this class. I forgot to backup my code and had to decompile Version 1.1.0
 * of EggHunt in order to retrieve this code. This is the version of Deanveloper's SkullCreator class that was made
 * for Minecraft 1.15.x. His new, updated class can be found in this same package. It is SkullCreator1_16.
 */

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class SkullCreator {
	public static ItemStack itemFromUrl(String url) {
		ItemStack item = new ItemStack(Material.PLAYER_HEAD);
		return itemWithUrl(item, url);
	}

	public static ItemStack itemWithUrl(ItemStack item, String url) {
		notNull(item, "item");
		notNull(url, "url");
		return itemWithBase64(item, urlToBase64(url));
	}

	public static ItemStack itemFromBase64(String base64) {
		ItemStack item = getPlayerSkullItem();
		return itemWithBase64(item, base64);
	}

	public static ItemStack itemWithBase64(ItemStack item, String base64) {
		notNull(item, "item");
		notNull(base64, "base64");
		UUID hashAsId = new UUID(base64.hashCode(), base64.hashCode());
		return Bukkit.getUnsafe().modifyItemStack(item,
				"{SkullOwner:{Id:\"" + hashAsId + "\",Properties:{textures:[{Value:\"" + base64 + "\"}]}}}");
	}

	private static boolean newerApi() {
		try {
			Material.valueOf("PLAYER_HEAD");
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	private static ItemStack getPlayerSkullItem() {
		if (newerApi())
			return new ItemStack(Material.valueOf("PLAYER_HEAD"));
		return new ItemStack(Material.valueOf("SKULL_ITEM"), 1, (short) 3);
	}

	private static void notNull(Object o, String name) {
		if (o == null)
			throw new NullPointerException(String.valueOf(name) + " should not be null!");
	}

	private static String urlToBase64(String url) {
		URI actualUrl;
		try {
			actualUrl = new URI(url);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
		String toEncode = "{\"textures\":{\"SKIN\":{\"url\":\"" + actualUrl.toString() + "\"}}}";
		return Base64.getEncoder().encodeToString(toEncode.getBytes());
	}
}
