package easytravel.util;

import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import easytravel.Main;
import easytravel.Settings;
import easytravel.waypoint.Waypoint;
import net.md_5.bungee.api.ChatColor;

public class Utils {

	public static void printWaypoint(Player p, Waypoint w) {
		printWaypoint(p, w, false);
	}

	public static void printWaypoint(Player p, Waypoint w, boolean isPublic) {
		String border = "&8-----------------------------";
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', border));
		p.sendMessage("");

		String title;
		if (isPublic) {
			title = "&7Public Waypoint &r&6&l" + w.getWaypointName() + "&r&7: ";
		} else {
			title = "&7Waypoint &r&6&l" + w.getWaypointName() + "&r&7: ";
		}
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', title));

		String xCoord = "&7-    &7X: &r&9" + w.getX();
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', xCoord));

		String yCoord = "&7-    &7Y: &r&9" + w.getY();
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', yCoord));

		String zCoord = "&7-    &7Z: &r&9" + w.getZ();
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', zCoord));

		String world = "&7-    &7In world: &r&9" + w.getWorldName();
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', world));

		if (isPublic && Settings.DISPLAY_OWNER_OF_PUBLICWAYPOINTS) {
			String owner = "&7-    &7Owner: &r&9" + Bukkit.getOfflinePlayer(w.getOwnerID()).getName();
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', owner));
		}

		p.sendMessage("");
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', border));
	}

	public static boolean isInt(String arg) {
		try {
			Integer.parseInt(arg);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	public static float square(float f) {
		return (float) Math.pow(f, 2D);
	}

	public static int square(int i) {
		return (int) Math.pow(i, 2D);
	}

	// Allows "a-Z", "_" and "-" The name has to be 3 to 16 characters long
	public static final Pattern waypointNamePattern = Pattern.compile("^[a-zA-Z-_]{3,16}$");

	public static boolean isValidName(String name) {
		return waypointNamePattern.matcher(name).matches();
	}

	public static String getUsageMessage(String command) {
		return "&cUsage: " + Main.getUsageForCommand(command);
	}

	// mode defines if the destination is a
	// 0 = private waypoint
	// 1 = public waypoint
	// 2 = player
	public static final String[] modeNames = { "&3privatewaypoint", "&3publicwaypoint", "&3player" };

	public static String getDestinationName(String destination, int mode) {
		return modeNames[mode] + "&r&6&l " + destination;
	}

	public static Point3D waypointToPoint3D(Waypoint wp) {
		return new Point3D(wp.getX(), wp.getY(), wp.getZ());
	}

	public static Point3D locationToPoint3D(Location loc) {
		return new Point3D(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
	}
}
