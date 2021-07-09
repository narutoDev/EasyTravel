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

public class WaypointsCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			Main.LogToConsole("The command /waypoints is only available for players!");
			return true;
		}

		Player p = (Player) sender;

		if (!sender.hasPermission("easytravel.waypoint.public.waypoints")) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
					"&cYou don't have permission to excecute this command!"));
			return true;
		}

		int amount = Main.getManager().getWaypointAmount(p.getUniqueId());
		if (amount < 1) {
			sender.sendMessage("You dont have any waypoints yet. Add some with /addwaypoint");
			return true;
		}

		String maxNumber = "∞";
		// As long as there is no real config file, this statement will get removed while building
		if (Settings.MAX_PRIVATE_WAYPOINTS_PER_PLAYER > -1) {
			maxNumber = Settings.MAX_PRIVATE_WAYPOINTS_PER_PLAYER + "";
		}
		
		String message = "&7 You have &r&9" + amount + " / &r&9" + maxNumber + "&r&7 Waypoints:";
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));

		for (Waypoint w : Main.getManager().getAllWaypoints(p.getUniqueId())) {
			Utils.printWaypoint(p, w);
		}

		return true;
	}

}
