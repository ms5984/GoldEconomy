package com.youtube.hempfest.goldeco;

//import com.youtube.hempfest.goldeco.commands.*;
import com.youtube.hempfest.goldeco.data.BankData;
import com.youtube.hempfest.goldeco.data.independant.Config;
import com.youtube.hempfest.goldeco.gui.MenuManager;
import com.youtube.hempfest.goldeco.listeners.BankListener;
import com.youtube.hempfest.goldeco.listeners.PlayerListener;
import com.youtube.hempfest.goldeco.listeners.vault.VaultEconomy;
import com.youtube.hempfest.goldeco.listeners.vault.VaultListener;
import com.youtube.hempfest.goldeco.structure.EconomyStructure;
import com.youtube.hempfest.goldeco.util.Metrics;
import com.youtube.hempfest.goldeco.util.libraries.StringLibrary;
import com.youtube.hempfest.hempcore.command.CommandBuilder;
import com.youtube.hempfest.hempcore.event.EventBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
//import org.bukkit.command.CommandMap;
//import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
//import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;
import java.util.stream.Collectors;


public class GoldEconomy extends JavaPlugin {

	//Instance
	private static GoldEconomy instance;
	private final Logger log = Logger.getLogger("Minecraft");
	private static final HashMap<Player, MenuManager> GuiMap = new HashMap<Player, MenuManager>();
	public VaultEconomy eco;
	private final PluginManager pm = getServer().getPluginManager();

	//Start server
	public void onEnable() {
		log.info(String.format("[%s] - Loading economy files.", getDescription().getName()));
//		registerCommands();
		loadStringLibrary();
		new CommandBuilder(this).compileFields("com.youtube.hempfest.goldeco.commands");
		registerMetrics(9063);
		new EventBuilder(this).compileFields("com.youtube.hempfest.goldeco.listeners.bukkit");
		setInstance(this);
		loadConfiguration();
		loadDefaults();
		registerVault();
	}
	
	public void onDisable() {
		log.info(String.format("[%s] - Goodbye friends...", getDescription().getName()));
		if (usingVault()) {
			VaultListener listener = new VaultListener(this);
			listener.unhook();
		}
		GuiMap.clear();
	}

	public static GoldEconomy getInstance() {
		return instance;
	}

	private void setInstance(GoldEconomy instance) {
		GoldEconomy.instance = instance;
	}

	private void registerVault() {
		if (usingVault()) {
			if (pm.isPluginEnabled("Vault")) {
				eco = new VaultEconomy();
				VaultListener listener = new VaultListener(this);
				listener.hook();
			} else {
				log.severe(String.format("[%s] - Vault not found. Disable " + '"' + "check-for-vault" + '"' + " in " + '"' + "shop_config.yml" + '"', getDescription().getName()));
				pm.disablePlugin(this);
			}
		}
	}

	private void loadStringLibrary() {
		final File config = new File(getDataFolder(), "config.yml");
		FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(config);
		try {
			fileConfiguration.save(config);
		} catch (IOException e) {
			e.printStackTrace();
		}
		final String lang = fileConfiguration.getString("lang");
		if (lang != null && !lang.isEmpty()) {
			new StringLibrary(this, lang);
		} else {
			new StringLibrary(this);
		}
	}

	private void registerMetrics(int ID) {
		Metrics metrics = new Metrics(this, ID);
		metrics.addCustomChart(new Metrics.SingleLineChart("bank_accounts_made", () -> GoldEconomy.getBankAccounts().size()));
		metrics.addCustomChart(new Metrics.SingleLineChart("total_logged_players", () -> PlayerListener.getAllPlayers().size()));
		metrics.addCustomChart(new Metrics.SingleLineChart("starting_balance", () -> {
			double result = GoldEconomy.startingBalance();
			return Integer.valueOf(String.valueOf(result));
		}));
		metrics.addCustomChart(new Metrics.SimplePie("using_clans", () -> {
			String result = "No";
			if (Bukkit.getPluginManager().isPluginEnabled("Clans")) {
				if (Bukkit.getPluginManager().getPlugin("Clans").getDescription().getAuthors().contains("Hempfest")) {
					result = "Yes";
				}
			}
			return result;
		}));

	}

	public static MenuManager menuViewer(Player p) {
		MenuManager manager;
		if (!(GuiMap.containsKey(p))) {

			manager = new MenuManager(p);
			GuiMap.put(p, manager);

			return manager;
		} else {
			return GuiMap.get(p);
		}
	}

	private double purchaseDefault(String item) {
		if (item.contains("DIAMOND")) {
			return 225.0;
		}
		return 1.0;
	}

	private double sellDefault(String item) {
		if (item.contains("DIAMOND")) {
			return 115.0;
		}
		return 0.5;
	}

	public static double startingBalance() {
		Config main = Config.get("shop_config");
		FileConfiguration fc = main.getConfig();
		return fc.getDouble("Economy.starting-balance");
	}

	public static String getMainWorld() {
		Config main = Config.get("shop_config");
		FileConfiguration fc = main.getConfig();
		return fc.getString("Economy.main-world");
	}

	public static boolean usingVault() {
		Config main = Config.get("shop_config");
		if (!main.exists()) {
			InputStream m1 = GoldEconomy.getInstance().getResource("shop_config.yml");
			main.copyFromResource(m1);
		}
		FileConfiguration fc = main.getConfig();
		if (fc.getBoolean("Economy.check-for-vault") == Boolean.valueOf(true)) {
			return true;
		}
		return false;
	}

	private void loadConfiguration() {
		Config main = Config.get("shop_messages");
		if (!main.exists()) {
			InputStream m1 = getResource("shop_messages.yml");
			main.copyFromResource(m1);
		}
	}

	private void loadDefaults() {
		Config shop_items = Config.get("shop_items");
		final List<String> itemList = CompletableFuture.supplyAsync(() -> Arrays.stream(Material.values())
			.filter(Material::isItem).filter(m -> m != Material.AIR).map(Enum::name).collect(Collectors.toList())).join();
		if (!shop_items.exists()) {
			FileConfiguration fc = shop_items.getConfig();
			for (String item : itemList) {
				fc.set("Items." + item + ".purchase-price", purchaseDefault(item));
				fc.set("Items." + item + ".sell-price", sellDefault(item));
			}
			shop_items.saveConfig();
		}
	}

	public static boolean usingBanks() {
		Config main = Config.get("shop_config");
		boolean result = false;
		if (main.exists()) {
			result = main.getConfig().getBoolean("Economy.using-banks");
		}
		return result;
	}

	public static boolean usingShop() {
		Config main = Config.get("shop_config");
		boolean result = false;
		if (main.exists()) {
			result = main.getConfig().getBoolean("Economy.using-shop");
		}
		return result;
	}

	public static EconomyStructure getPlayerAccount(OfflinePlayer p) {
		return new PlayerListener(p);
	}

	/** carried from BankListener
	 * @deprecated messes with multi-world balances. Use at own risk
	 */
	@Deprecated
	public static EconomyStructure getBankAccount(OfflinePlayer p, String accountID) {
		return new BankListener(p);
	}

	public static List<String> getWorlds() throws NullPointerException {
		Config main = Config.get("shop_config");
		if (main.exists()) {
			return main.getConfig().getStringList("Economy.world-list");
		}
		return null;
	}

	public static String getBankWorld(String accountID) {
		return BankData.getBankWorld(accountID);
	}

	public static String getBankOwner(String accountID) {
		return BankData.getBankOwner(accountID);
	}

	public static List<String> getBankWorlds() {
		return BankData.getBankWorlds();
	}

	public static List<String> getBankAccounts() {
		return BankData.getBankAccounts();
	}

}
