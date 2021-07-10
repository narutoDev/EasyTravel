package easytravel.command.waypoint;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import easytravel.Main;
import easytravel.util.Utils;
import easytravel.waypoint.Waypoint;
import net.md_5.bungee.api.ChatColor;

public class EditWaypointCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			Main.LogToConsole("The command /editwaypoint is only available for players!");
			return true;
		}
		Player p = (Player) sender;

		if (!sender.hasPermission("easytravel.waypoint.private.edit")) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
					"&cYou don't have permission to excecute this command!"));
			return true;

		}

		if (args.length < 3) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Utils.getUsageMessage("EditWaypoint")));
			return true;
		}

		if (!Main.getManager().hasWaypoint(p.getUniqueId(), args[1])) {
			sender.sendMessage(
					ChatColor.translateAlternateColorCodes('&', "&cWaypoint &r&e" + args[1] + "&r&c doesn't exist!"));
			return true;
		}

		switch (args[0]) {

		case ("name"):
			if (Main.getManager().hasWaypoint(p.getUniqueId(), args[2])) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						"&cYou already have a waypoint named &r&e" + args[2]));
				return true;
			}
			if (!Utils.isValidName(args[2])) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						"&cInvalid waypoint name! Can only contain letters, - and _! It can be between 3 and 16 characters long!"));
				return true;
			}
			Main.getManager().renameWaypoint(p.getUniqueId(), args[1], args[2]);
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
					"&2Waypoint &r&9" + args[1] + "&r&2 has been renamed to &r&6&l" + args[2] + "&r&2!"));
			return true;

		case ("position"):
			if (args.length < 3) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Utils.getUsageMessage("EditWaypoint")));
				return true;
			}
			Waypoint wpToEdit = Main.getManager().getWaypoint(p.getUniqueId(), args[1]);

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
					"&2Waypoint &r&6&l" + args[1] + "&r&2 edited successfully!"));
			Utils.printWaypoint(p, wpToEdit);

			return true;
		}

		// Invalid edit mode was given
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Utils.getUsageMessage("EditWaypoint")));
		return true;

	}
}
