package com.youtube.hempfest.goldeco.listeners;

import com.youtube.hempfest.goldeco.GoldEconomy;
import com.youtube.hempfest.goldeco.data.BankData;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;

public class BankAccount {

	private String accountID;
	private String world;

	public BankAccount(String accountID) {
		this.accountID = accountID;
	}

	public BankAccount queryWorld() {
		String result = "";
		for (String world : GoldEconomy.getBankWorlds()) {
			BankData data = new BankData(world);
			FileConfiguration fc = data.getConfig();
			for (String player : fc.getConfigurationSection("banks").getKeys(false)) {
				if (fc.getString("banks." + player + ".accountID").equals(accountID)) {
					result = world;
					break;
				}
			}
		}
		this.world = result;
		return this;
	}

	public double getBalance() {
		BankData data = new BankData(world);
		FileConfiguration fc = data.getConfig();
		return Double.parseDouble(fc.getString("banks." + Bukkit.getOfflinePlayer(getOwner()).getName() + ".balance").replace(",", ""));
	}

	public String getOwner() {
		return GoldEconomy.getBankOwner(accountID);
	}


}
