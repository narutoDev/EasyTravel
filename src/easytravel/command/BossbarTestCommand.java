package easytravel.command;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import easytravel.Main;

public class BossbarTestCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			Main.LogToConsole("The command /bossbartest is only available for players!");
			return true;
		}

		Player p = (Player) sender;

		/*
		 * Calculates the yaw needed int targetX = Integer.parseInt(args[0]); int
		 * targetZ = Integer.parseInt(args[1]);
		 * 
		 * int distX = p.getLocation().getBlockX() - targetX; int distZ =
		 * p.getLocation().getBlockZ() - targetZ;
		 * 
		 * float yaw = (float) Math.atan2(distZ, distX); yaw = (float) (yaw * (180 /
		 * Math.PI)); // convert radians to degrees yaw += 90; // the yaw in minecraft
		 * has a 90 degree offset. Don't ask why
		 * 
		 * p.sendMessage("Yaw = " + yaw);
		 */

		// 50% of the bar is y so 100% is 2*y
		double y = Double.parseDouble(args[0]);
		y = Math.abs(y * 2.0);

		// map the yaw from 0-360 to 0-2*y
		double yaw = p.getLocation().getYaw();
		yaw = (y / 360.0) * yaw;

		// how many percent of y is yaw
		double progress = Math.abs(yaw / y);

		p.sendMessage(progress + "");

		/*
		 * BossBar bossBar = Bukkit.createBossBar("BossBar", BarColor.GREEN,
		 * BarStyle.SOLID); bossBar.addPlayer(p); bossBar.setProgress(progress);
		 */

		return true;
	}
}
