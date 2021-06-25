package easytravel.command.waypoint;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import easytravel.Main;
import easytravel.util.Utils;
import net.md_5.bungee.api.ChatColor;

public class PublicWaypointCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (!(sender instanceof Player)) {
			Main.LogToConsole("The command /publicwaypoint is only available for players!");
			return true;
		}

		if (!sender.hasPermission("easytravel.waypoint.public.see")) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
					"&cYou don't have permission to excecute this command!"));
			return true;

		}

		// no parameter given -> invalid usage
		if (args.length != 1) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Utils.getUsageMessage("PublicWaypoint")));
			return true;
		}

		// Check if executing player has a waypoint named args[0]
		if (!Main.getPublicManager().existWaypoint(args[0])) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
					"&cThere is no public waypoint named &r&e" + args[0] + "&r&c!"));
			return true;
		}
		Utils.printWaypoint((Player) sender, Main.getPublicManager().getWaypoint(args[0]), true);

		return true;
	}

}
