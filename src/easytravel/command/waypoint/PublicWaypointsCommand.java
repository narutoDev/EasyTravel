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

public class PublicWaypointsCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			Main.LogToConsole("The command /publicwaypoints is only available for players!");
			return true;
		}

		Player p = (Player) sender;

		if (!sender.hasPermission("easytravel.waypoint.public.waypoints")) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
					"&cYou don't have permission to excecute this command!"));
			return true;

		}
		String maxNumber;
		if (Settings.MAX_PUBLIC_WAYPOINTS_PER_PLAYER > -1) {
			maxNumber = Settings.MAX_PUBLIC_WAYPOINTS_PER_PLAYER + "";
		}else {
			maxNumber = "∞";
		}
		String message = "&7 You have &r&9" + Main.getPublicManager().getWaypointAmount(p.getUniqueId()) + " / &r&9"
				+ maxNumber + "&r&7 public waypoints:";
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));

		Waypoint[] waypoints = Main.getPublicManager().getAllWaypoints();
		if (waypoints == null || waypoints.length < 1) {
			sender.sendMessage("There are no public waypoints yet. Add some with /addpublicwaypoint");
			return true;
		}

		for (Waypoint wp : waypoints) {
			Utils.printWaypoint(p, wp, true);
		}

		return true;
	}

}
