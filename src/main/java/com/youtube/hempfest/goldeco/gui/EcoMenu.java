package com.youtube.hempfest.goldeco.gui;

import com.youtube.hempfest.hempcore.gui.Menu;
import org.bukkit.Material;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;


public abstract class EcoMenu extends Menu implements InventoryHolder {

	protected MenuManager manager;
	protected ItemStack COLORED_GLASS = makeItem(Material.BLUE_STAINED_GLASS_PANE, " ");
//	protected ItemStack FILLER_GLASS_LIGHT = makeItem(Material.RED_STAINED_GLASS_PANE, " ");

	{
		FILLER_GLASS_LIGHT = makeItem(Material.RED_STAINED_GLASS_PANE, " ");
	}

	public EcoMenu(MenuManager manager) {
		super(manager);
		this.manager = manager;
	}

//	public abstract String getMenuName();

//	public abstract int getSlots();

//	public abstract void handleMenu(InventoryClickEvent e);

//	public abstract void setMenuItems();

/*	public void open() {

		inventory = Bukkit.createInventory(this, getSlots(), getMenuName());

		this.setMenuItems();

		manager.getOwner().openInventory(inventory);
	}*/

/*	public Inventory getInventory() {
		return inventory;
	}*/

/*	public void setFillerGlass() {
		for (int i = 0; i < getSlots(); i++) {
			if (inventory.getItem(i) == null) {
				inventory.setItem(i, FILLER_GLASS);
			}
		}
	}*/

/*	protected List<String> color(String... text) {
		ArrayList<String> convert = new ArrayList<>();
		for (String t : text) {
			convert.add(ChatColor.translateAlternateColorCodes('&', t));
		}
		return convert;
	}*/

/*	public ItemStack makeItem(Material material, String displayName, String... lore) {

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
	}*/

}
