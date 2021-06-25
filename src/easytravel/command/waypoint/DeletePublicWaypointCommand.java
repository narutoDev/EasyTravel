package easytravel.command.waypoint;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import easytravel.Main;
import easytravel.util.Utils;
import net.md_5.bungee.api.ChatColor;

public class DeletePublicWaypointCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			Main.LogToConsole("The command /waypoint is only available for players!");
			return true;
		}

		Player p = (Player) sender;

		if (!sender.hasPermission("easytravel.waypoint.public.delete")) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
					"&cYou don't have permission to excecute this command!"));
			return true;

		}

		if (args.length < 1) {
			sender.sendMessage(
					ChatColor.translateAlternateColorCodes('&', Utils.getUsageMessage("DeletePublicWaypoint")));
			return true;
		}

		if (!Main.getPublicManager().existWaypoint(args[0])) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
					"&cPublic Waypoint &r&e" + args[0] + "&r&c doesn't exist!"));
			return true;
		}

		if (!Main.getPublicManager().hasWaypoint(p.getUniqueId(), args[0])) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
					"&c Public Waypoint &r&e" + args[0] + "&r&c doesn't belong to you!"));
			return true;
		}

		if (args.length < 2 || !args[1].equalsIgnoreCase("confirm")) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
					"&c Warning! The public waypoint &r&9" + args[0] + "&r&c will be deleted permanently!"));
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
					"&7Are you sure? Please confirm with typing &r&e/deletepublicwaypoint " + args[0] + " confirm"));
			return true;
		}

		sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
				"&cPublic Waypoint &r&6&l" + args[0] + "&r&c deleted successfully!"));
		Main.getPublicManager().removeWaypoint(p.getUniqueId(), args[0]);
		return true;
	}

}
