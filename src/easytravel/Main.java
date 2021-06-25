package easytravel;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import easytravel.command.travel.NewTravelCommand;
import easytravel.command.travel.TravelCommand;
import easytravel.command.waypoint.AddPublicWaypointCommand;
import easytravel.command.waypoint.AddWaypointCommand;
import easytravel.command.waypoint.DeletePublicWaypointCommand;
import easytravel.command.waypoint.DeleteWaypointCommand;
import easytravel.command.waypoint.EditPublicWaypointCommand;
import easytravel.command.waypoint.EditWaypointCommand;
import easytravel.command.waypoint.PublicWaypointCommand;
import easytravel.command.waypoint.PublicWaypointsCommand;
import easytravel.command.waypoint.WaypointCommand;
import easytravel.command.waypoint.WaypointsCommand;
import easytravel.listener.PlayerConnectionListener;
import easytravel.travel.TravelManager;
import easytravel.util.WaypointIO;
import easytravel.waypoint.PublicWaypointManager;
import easytravel.waypoint.WaypointManager;

public class Main extends JavaPlugin {

	private static final WaypointManager manager = new WaypointManager();
	private static final PublicWaypointManager publicManager = new PublicWaypointManager();
	private static final TravelManager travelManager = new TravelManager();
	private static Logger logger;
	private static File dataFolder;
	private static Plugin plugin;
	private static Main main;

	@Override
	public void onEnable() {
		Main.logger = this.getLogger();
		Main.dataFolder = this.getDataFolder();
		Main.plugin = this;
		Main.main = this;

		WaypointIO.loadPublicWaypoints();
		for (Player p : Bukkit.getOnlinePlayers()) {
			WaypointIO.loadWaypoints(p.getUniqueId());
		}

		registerCommands();
		registerListeners();

		Main.LogToConsole("Verion " + this.getDescription().getVersion() + " infdev - Not finished yet");
		Main.LogToConsole("Please check frequently for updates manually until an auto-updater gets added.");
	}

	@Override
	public void onDisable() {
		WaypointIO.savePublicWaypoints();

		for (Player p : Bukkit.getOnlinePlayers()) {
			if (!Main.getManager().hasWaypoints(p.getUniqueId())) {
				continue;
			}
			WaypointIO.saveWaypoints(p.getUniqueId());
		}

	}

	private void registerCommands() {
		// waypoint commands
		this.getCommand("Waypoint").setExecutor(new WaypointCommand());
		this.getCommand("AddWaypoint").setExecutor(new AddWaypointCommand());
		this.getCommand("Waypoints").setExecutor(new WaypointsCommand());
		this.getCommand("DeleteWaypoint").setExecutor(new DeleteWaypointCommand());
		this.getCommand("EditWaypoint").setExecutor(new EditWaypointCommand());
		this.getCommand("PublicWaypoint").setExecutor(new PublicWaypointCommand());
		this.getCommand("PublicWaypoints").setExecutor(new PublicWaypointsCommand());
		this.getCommand("AddPublicWaypoint").setExecutor(new AddPublicWaypointCommand());
		this.getCommand("DeletePublicWaypoint").setExecutor(new DeletePublicWaypointCommand());
		this.getCommand("EditPublicWaypoint").setExecutor(new EditPublicWaypointCommand());

		// travel commands
		this.getCommand("Travel").setExecutor(new TravelCommand());
		this.getCommand("NewTravel").setExecutor(new NewTravelCommand());
	}

	private void registerListeners() {
		this.getServer().getPluginManager().registerEvents(new PlayerConnectionListener(), this);
	}

	public static WaypointManager getManager() {
		return Main.manager;
	}

	public static PublicWaypointManager getPublicManager() {
		return Main.publicManager;
	}

	public static void LogToConsole(String msg) {
		logger.info(msg);
	}

	public static File getDataRootFolder() {
		return Main.dataFolder;
	}

	public static Plugin getPlugin() {
		return Main.plugin;
	}

	public static TravelManager getTravelManager() {
		return Main.travelManager;
	}

	public static String getUsageForCommand(String command) {
		return getMain().getCommand(command).getUsage();
	}

	public static Main getMain() {
		return Main.main;
	}

}
