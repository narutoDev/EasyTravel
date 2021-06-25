package easytravel.command.waypoint;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import easytravel.Main;
import easytravel.util.Utils;
import easytravel.waypoint.Waypoint;
import net.md_5.bungee.api.ChatColor;

public class EditPublicWaypointCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			Main.LogToConsole("The command /editpublicwaypoint is only available for players!");
			return true;
		}

		Player p = (Player) sender;

		if (!sender.hasPermission("easytravel.waypoint.public.edit")) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
					"&cYou don't have permission to excecute this command!"));
			return true;

		}

		if (args.length < 3) {
			sender.sendMessage(
					ChatColor.translateAlternateColorCodes('&', Utils.getUsageMessage("EditPublicWaypoint")));
			return true;
		}

		if (!Main.getPublicManager().existWaypoint(args[1])) {
			sender.sendMessage(
					ChatColor.translateAlternateColorCodes('&', "&cWaypoint &r&e" + args[1] + "&r&c doesn't exist!"));
			return true;
		}

		if (!Main.getPublicManager().hasWaypoint(p.getUniqueId(), args[1])) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
					"&c Public Waypoint &r&e" + args[2] + "&r&c doesn't belong to you!"));
			return true;
		}

		switch (args[0]) {

		// user wants to change the name of a waypoint
		case ("name"):
			if (Main.getPublicManager().existWaypoint(args[2])) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						"&cThere is already a public waypoint named &r&e" + args[2] + "&r&c!"));
				return true;
			}
			if (!Utils.isValidName(args[2])) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						"&cInvalid waypoint name! Can only contain letters, - and _! It can be between 3 and 16 characters long!"));
				return true;
			}
			Main.getPublicManager().renameWaypoint(p.getUniqueId(), args[1], args[2]);
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
					"&2Public Waypoint &r&9" + args[1] + "&r&2 has been renamed to &r&6&l" + args[2] + "&r&2!"));
			return true;

		case ("position"):
			if (args.length < 3) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						"&cUsage:/editpublicwaypoint [name | position] [new waypoint name | <x> <y> <z> or \"self\" to set the position to your current location]"));
				return true;
			}
			Waypoint wpToEdit = Main.getPublicManager().getWaypoint(args[1]);

			if (args[2].equalsIgnoreCase("self")) {
				wpToEdit.setX(p.getLocation().getBlockX());
				wpToEdit.setY(p.getLocation().getBlockY());
				wpToEdit.setZ(p.getLocation().getBlockZ());

			} else if (Utils.isInt(args[2]) && Utils.isInt(args[3]) && Utils.isInt(args[4])) {
				wpToEdit.setX(Integer.parseInt(args[2]));
				wpToEdit.setY(Integer.parseInt(args[3]));
				wpToEdit.setZ(Integer.parseInt(args[4]));

			} else { // Invalid parameters where given by the user
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						"&cPlease enter normal numbers like 0, 3, -8 as coordinates or use \"self\" to set the position to your current location"));
				return true;
			}

			// The edit was performed successfully
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
					"&2Public Waypoint &r&6&l" + args[1] + "&r&2 edited successfully!"));
			Utils.printWaypoint(p, wpToEdit, true);

			return true;
		}
		return true;
	}

}
