package easytravel.command.waypoint;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import easytravel.Main;
import easytravel.Settings;
import easytravel.util.Utils;
import easytravel.waypoint.Waypoint;
import net.md_5.bungee.api.ChatColor;

public class AddWaypointCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			Main.LogToConsole("The command /addwaypoint is only available for players!");
			return true;
		}

		Player p = (Player) sender;

		if (!sender.hasPermission("easytravel.waypoint.private.add")) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
					"&cYou don't have permission to excecute this command!"));
			return true;

		}

		if (args.length != 1 && args.length != 4) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Utils.getUsageMessage("AddWaypoint")));
			return true;
		}

		// player has the permission to bypass the limit
		boolean hasBypassPermission = sender.hasPermission("easytravel.waypoint.private.bypasslimit");

		// number of waypoints is greater than MAX_WAYPOINTS_PER_PLAYER or
		// MAX_WAYPOINTS_PER_PLAYER is smaller than 0
		boolean hasPassedLimit = (Main.getManager()
				.getWaypointAmount(p.getUniqueId()) >= Settings.MAX_PRIVATE_WAYPOINTS_PER_PLAYER)
				|| Settings.MAX_PRIVATE_WAYPOINTS_PER_PLAYER < 0;

		if (!hasBypassPermission && hasPassedLimit) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
					"&cYou have reached the maximal amount of waypoints per player ("
							+ Settings.MAX_PRIVATE_WAYPOINTS_PER_PLAYER + ")!"));
			return true;
		}

		if (Main.getManager().hasWaypoint(p.getUniqueId(), args[0])) {
			sender.sendMessage(
					ChatColor.translateAlternateColorCodes('&', "&cYou already have a waypoint named &r&e" + args[0]));
			return true;
		}

		if (!Utils.isValidName(args[0])) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
					"&cInvalid waypoint name! Can only contain letters, - and _! It can be between 3 and 16 characters long!"));
			return true;
		}

		Location loc = p.getLocation();

		int x = loc.getBlockX();
		int y = loc.getBlockY();
		int z = loc.getBlockZ();

		if (args.length == 4) {
			if (!Utils.isInt(args[1]) || !Utils.isInt(args[2]) || !Utils.isInt(args[3])) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						"&cPlease enter normal numbers like 0, 3, -8 as coordinates!"));
				return true;
			}

			x = Integer.parseInt(args[1]);
			y = Integer.parseInt(args[2]);
			z = Integer.parseInt(args[3]);
		}

		Waypoint wp = new Waypoint(p.getUniqueId(), x, y, z, p.getWorld(), args[0]);
		Main.getManager().addWaypoint(p.getUniqueId(), wp);
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
				"&2Waypoint &r&6&l" + args[0] + "&r&2 added successfully!"));
		Utils.printWaypoint(p, wp);

		return true;

	}
}
