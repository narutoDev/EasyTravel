package easytravel.command.waypoint;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import easytravel.Main;
import easytravel.Settings;
import easytravel.util.Utils;
import easytravel.waypoint.Waypoint;
import net.md_5.bungee.api.ChatColor;

public class AddPublicWaypointCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			Main.LogToConsole("The command /addpublicwaypoint is only available for players!");
			return true;
		}

		Player p = (Player) sender;

		if (!sender.hasPermission("easytravel.waypoint.public.add")) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
					"&cYou don't have permission to excecute this command!"));
			return true;

		}

		if (args.length != 1 && args.length != 4) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Utils.getUsageMessage("AddPublicWaypoint")));
			return true;
		}

		if (Main.getPublicManager().existWaypoint(args[0])) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
					"&cThere is already a public waypoint named &r&e" + args[0] + "&r&c!"));
			return true;
		}

		// sender has the permission to bypass the global limit
		boolean hasGlobalLimitBypassPermission = sender.hasPermission("easytravel.waypoint.public.bypassgloballimit");

		// is the global limit more than -1
		boolean isLimitSet = Settings.MAX_PUBLIC_WAYPOINTS_GLOBAL > -1;

		// is the global maximum reached or it's not set
		boolean hasGlobalLimitPassed = (Settings.MAX_PUBLIC_WAYPOINTS_GLOBAL >= Main.getPublicManager()
				.getPublicWaypointsTotalAmount()) && isLimitSet;

		if (hasGlobalLimitPassed && !hasGlobalLimitBypassPermission) {
			sender.sendMessage(
					ChatColor.translateAlternateColorCodes('&', "&cThe maximum global limit of public waypoints("
							+ Settings.MAX_PUBLIC_WAYPOINTS_GLOBAL + ") is reached!"));
			return true;
		}

		// sender has the permission to bypass the limit per player
		boolean hasBypassLimitPermission = sender.hasPermission("easytravel.waypoint.public.bypassplayerspecificlimit");

		// is the player specific limit more than -1
		isLimitSet = Settings.MAX_PUBLIC_WAYPOINTS_PER_PLAYER > -1;

		boolean hasPlayerspecificLimitPassed = (Settings.MAX_PUBLIC_WAYPOINTS_PER_PLAYER >= Main.getPublicManager()
				.getWaypointAmount(p.getUniqueId()) && isLimitSet);

		if (hasPlayerspecificLimitPassed && !hasBypassLimitPermission) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
					"&cYou have reached the maximum amount of public waypoints per player("
							+ Settings.MAX_PUBLIC_WAYPOINTS_PER_PLAYER + ")!"));
			return true;
		}

		if (!Utils.isValidName(args[0])) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
					"&cInvalid waypoint name! Can only contain letters, - and _! It can be between 3 and 16 characters long!"));
			return true;
		}

		int x;
		int y;
		int z;

		if (args.length == 1) {
			x = p.getLocation().getBlockX();
			y = p.getLocation().getBlockY();
			z = p.getLocation().getBlockZ();
		} else if (Utils.isInt(args[1]) && Utils.isInt(args[2]) && Utils.isInt(args[3])) {
			x = Integer.parseInt(args[1]);
			y = Integer.parseInt(args[2]);
			z = Integer.parseInt(args[3]);
		} else {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
					"&cPlease enter normal numbers like 0, 3, -8 as coordinates!"));
			return true;
		}

		Waypoint wp = new Waypoint(p.getUniqueId(), x, y, z, p.getWorld(), args[0]);
		Main.getPublicManager().addWaypoint(p.getUniqueId(), wp);
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
				"&2Public Waypoint &r&6&l" + args[0] + "&r&2 added successfully!"));
		Utils.printWaypoint(p, wp, true);

		return true;
	}

}
