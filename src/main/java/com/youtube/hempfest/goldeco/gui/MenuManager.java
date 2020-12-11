package com.youtube.hempfest.goldeco.gui;

import com.youtube.hempfest.hempcore.gui.GuiLibrary;
import org.bukkit.entity.Player;

/*
Companion class to all menus. This is needed to pass information across the entire
 menu system no matter how many inventories are opened or closed.

 Each player has one of these objects, and only one.
 */

public class MenuManager extends GuiLibrary {

	String playerName;

	String accountID;

	int page;

	public MenuManager(Player p) {
		super(p);
	}

	public Player getOwner() {
		return getViewer();
	}

	public String getPlayerName() {
		return playerName;
	}

	public String getItemToEdit() {
		return getData();
	}

	public String getItemTooEdit() {
		return getData2();
	}

	public String getAccountID() {
		return accountID;
	}

	public int getPage() {
		return page;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public void setAccountID(String accountID) {
		this.accountID = accountID;
	}

	public void setItemToEdit(String itemToEdit) {
		setData(itemToEdit);
	}

	public void setItemTooEdit(String itemTooEdit) {
		setData2(itemTooEdit);
	}

}
