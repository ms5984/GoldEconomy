package com.youtube.hempfest.goldeco.gui;

import com.youtube.hempfest.goldeco.GoldEconomy;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;


public abstract class Menu implements InventoryHolder {

	protected MenuManager manager;
	protected Inventory inventory;
	protected ItemStack FILLER_GLASS = makeItem(Material.BLACK_STAINED_GLASS_PANE, " ", "");
	protected ItemStack COLORED_GLASS = makeItem(Material.BLUE_STAINED_GLASS_PANE, " ", "");
	protected ItemStack FILLER_GLASS_LIGHT = makeItem(Material.RED_STAINED_GLASS_PANE,
			" ", "");

	public Menu(MenuManager manager) {
		this.manager = manager;
	}

	public abstract String getMenuName();

	public abstract int getSlots();

	public abstract void handleMenu(InventoryClickEvent e);

	public abstract void setMenuItems();

	public void open() {

		inventory = Bukkit.createInventory(this, getSlots(), getMenuName());

		this.setMenuItems();

		manager.getOwner().openInventory(inventory);
	}

	public Inventory getInventory() {
		return inventory;
	}

	public void setFillerGlass() {
		for (int i = 0; i < getSlots(); i++) {
			if (inventory.getItem(i) == null) {
				inventory.setItem(i, FILLER_GLASS);
			}
		}
	}

	protected String color(String text) {
		return ChatColor.translateAlternateColorCodes('&', text);
	}

	protected List<String> color(String... text) {
		ArrayList<String> convert = new ArrayList<>();
		for (String t : text) {
			convert.add(ChatColor.translateAlternateColorCodes('&', t));
		}
		return convert;
	}

	public ItemStack makeItem(Material material, String displayName, String... lore) {

		ItemStack item = new ItemStack(material);
		ItemMeta itemMeta = item.getItemMeta();
		assert itemMeta != null;
		itemMeta.setDisplayName(color(displayName));

		itemMeta.setLore(color(lore));
		item.setItemMeta(itemMeta);

		return item;
	}

	public ItemStack makeItem(Material material, String displayName, PersistentDataType type, String key, Object data, String... lore) {

		ItemStack item = new ItemStack(material);
		ItemMeta itemMeta = item.getItemMeta();
		assert itemMeta != null;
		itemMeta.setDisplayName(color(displayName));
		itemMeta.getPersistentDataContainer().set(new NamespacedKey(GoldEconomy.getInstance(), key), type, data);
		itemMeta.setLore(color(lore));
		item.setItemMeta(itemMeta);

		return item;
	}

	public ItemStack makeItem(ItemStack item, String displayName, PersistentDataType type, String key, Object data, String... lore) {

		ItemMeta itemMeta = item.getItemMeta();
		assert itemMeta != null;
		itemMeta.setDisplayName(color(displayName));
		itemMeta.getPersistentDataContainer().set(new NamespacedKey(GoldEconomy.getInstance(), key), type, data);
		itemMeta.setLore(color(lore));
		item.setItemMeta(itemMeta);

		return item;
	}

}
