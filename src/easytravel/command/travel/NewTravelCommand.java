package easytravel.command.travel;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import easytravel.Main;
import easytravel.travel.Travel;
import easytravel.util.Point3D;
import easytravel.util.Utils;
import net.md_5.bungee.api.ChatColor;

public class NewTravelCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			Main.LogToConsole("The command /newtravel is only available for players!");
			return true;
		}

		Player p = (Player) sender;

		if (!sender.hasPermission("easytravel.travel")) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
					"&cYou don't have permission to excecute this command!"));
			return true;

		}

		// not enough parameters to be a valid command
		if (args.length < 2) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Utils.getUsageMessage("NewTravel")));
			return true;
		}

		// player is already travelling
		if (Main.getTravelManager().hasTravel(p.getUniqueId())) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&',
					"&cYou are already travelling! You can stop your travel with /travel stop"));
			return true;
		}

		switch (args[0].toLowerCase()) {

		case "private":
			if (!Main.getManager().hasWaypoint(p.getUniqueId(), args[1])) {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&',
						"&cWaypoint &r&e" + args[1] + "&r&c doesn't exist!"));
				return true;
			}
			Point3D destinationPoint = Utils.waypointToPoint3D(Main.getManager().getWaypoint(p.getUniqueId(), args[1]));
			Point3D startingPoint = Utils.locationToPoint3D(p.getLocation());
			new Travel(p, destinationPoint, startingPoint, Utils.getDestinationName(args[1], 0));
			return true;

		case "public":
			if (!Main.getPublicManager().existWaypoint(args[1])) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						"&cWaypoint &r&e" + args[1] + "&r&c doesn't exist!"));
				return true;
			}
			Point3D destination = Utils.waypointToPoint3D(Main.getPublicManager().getWaypoint(args[1]));
			Point3D start = Utils.locationToPoint3D(p.getLocation());
			new Travel(p, destination, start, Utils.getDestinationName(args[1], 1));
			return true;

		case "coords":
			if (args.length != 4) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Utils.getUsageMessage("NewTravel")));
				return true;
			}
			if (!(Utils.isInt(args[1]) && Utils.isInt(args[2]) && Utils.isInt(args[3]))) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						"&cPlease enter normal numbers like 0, 3, -8 as coordinates"));
				return true;
			}
			Point3D dest = new Point3D(Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
			Point3D startPoint = Utils.locationToPoint3D(p.getLocation());
			String coords = "&r&6&l" + args[1] + " " + args[2] + " " + args[3];
			new Travel(p, dest, startPoint, coords);
			return true;

		}
		
		// invalid first argument
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Utils.getUsageMessage("NewTravel")));
		return true;
	}

}
