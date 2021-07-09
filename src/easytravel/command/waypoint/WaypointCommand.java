package easytravel.command.waypoint;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import easytravel.Main;
import easytravel.util.Utils;
import net.md_5.bungee.api.ChatColor;

public class WaypointCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			Main.LogToConsole("The command /waypoint is only available for players!");
			return true;
		}

		Player p = (Player) sender;

		if (!sender.hasPermission("easytravel.waypoint.private.see")) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
					"&cYou don't have permission to excecute this command!"));
			return true;

		}

		// no parameter given -> invalid usage
		if (args.length != 1) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Utils.getUsageMessage("Waypoint")));
			return true;
		}

		// Check if executing player has a waypoint named args[0]
		if (!Main.getManager().hasWaypoint(p.getUniqueId(), args[0])) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
					"&cYou don't have a waypoint with name &r&e" + args[0] + "&r&c!"));
			return true;
		}
		Utils.printWaypoint((Player) sender, Main.getManager().getWaypoint(p.getUniqueId(), args[0]));

		return true;
	}

}

/*
 * Template for new command class
 * @Override public boolean onCommand(CommandSender sender, Command command,
 * String label, String[] args) { return false; }
 */