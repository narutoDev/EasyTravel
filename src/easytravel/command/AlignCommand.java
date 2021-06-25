package easytravel.command;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import easytravel.Main;

public class AlignCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			Main.LogToConsole("The command /align is only available for players!");
			return true;
		}

		Player p = (Player) sender;

		int targetX = Integer.parseInt(args[0]);
		int targetZ = Integer.parseInt(args[1]);

		int distX = p.getLocation().getBlockX() - targetX;
		int distZ = p.getLocation().getBlockZ() - targetZ;
		
		float yaw = (float) Math.atan2(distZ, distX);
		yaw =  (float) (yaw * (180 / Math.PI));	// convert radians to degrees
		yaw += 90; 	// the yaw in minecraft has a 90 degree offset. Don't ask why

		p.sendMessage("Yaw = " + yaw);
		Location loc = p.getLocation();
		loc.setYaw(yaw);
		p.teleport(loc);

		return true;
	}
}
