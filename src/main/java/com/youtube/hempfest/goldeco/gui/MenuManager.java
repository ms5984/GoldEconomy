package com.youtube.hempfest.goldeco.gui;

import org.bukkit.entity.Player;

/*
Companion class to all menus. This is needed to pass information across the entire
 menu system no matter how many inventories are opened or closed.

 Each player has one of these objects, and only one.
 */

public class MenuManager {

	private Player owner;

	String itemToEdit;

	String itemTooEdit;

	String playerName;

	String accountID;

	int page;

	public MenuManager(Player p) {
		this.owner = p;
	}

	public Player getOwner() {
		return owner;
	}

	public String getPlayerName() {
		return playerName;
	}

	public String getItemToEdit() { return itemToEdit; }

	public String getItemTooEdit() { return itemTooEdit; }

	public String getAccountID() {
		return accountID;
	}

	public int getPage() { return page; }

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public void setAccountID(String accountID) {
		this.accountID = accountID;
	}

	public void setItemToEdit(String itemToEdit) { this.itemToEdit = itemToEdit; }

	public void setItemTooEdit(String itemTooEdit) { this.itemTooEdit = itemTooEdit; }

}
