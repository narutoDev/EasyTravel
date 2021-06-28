package easytravel.travel;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import easytravel.Main;

public class BossbarUpdater implements Listener {

	private Travel travel;
	private BossBar bossBar;

	public BossbarUpdater(Travel travel) {
		this.travel = travel;
		bossBar = Bukkit.createBossBar("BossBar", BarColor.GREEN, BarStyle.SOLID);
	}

	public void startUpdater() {
		Main.getPlugin().getServer().getPluginManager().registerEvents(this, Main.getPlugin());
		bossBar.addPlayer(travel.getTraveler());
		travel.getTraveler()
				.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThe bossbar might be a bit glitchy"));
	}

	public void stopUpdater() {
		PlayerMoveEvent.getHandlerList().unregister(this);
		bossBar.removePlayer(travel.getTraveler());
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if (p.getUniqueId() != travel.getTravelerID()) {
			return;
		}

		if (!hasToCalculateNew(e.getTo(), p.getLocation())) {
			return;
		}

		// Calculate yaw to destination
		int targetX = travel.getDestinationPoint().getX();
		int targetZ = travel.getDestinationPoint().getZ();

		double distX = p.getLocation().getX() - targetX;
		double distZ = p.getLocation().getZ() - targetZ;

		double y = Math.atan2(distZ, distX);
		y = (y * (180 / Math.PI)); // convert radians to degrees
		y += 90; // the yaw in minecraft has a 90 degree offset. Don't ask why
		if (y < 0) {
			y = 360 + y;
		}

		bossBar.setTitle(
				ChatColor.translateAlternateColorCodes('&', "&9Align the bar in the center to face your destination"));

		// 50% is y so 100% is 2*y
		y *= 2;

		double yaw = p.getLocation().getYaw();
		// getYaw apparently returns different ranges Couldn't figure that one out!
		// https://www.spigotmc.org/threads/player-getlocation-getyaw-giving-strange-results.358027
		// and
		// http://www.java2s.com/Tutorial/Java/0120__Development/Normalizesanangletoanabsoluteangle.htm
		// helped
		yaw = (yaw %= 360) >= 0 ? yaw : (yaw + 360);
		// map the yaw from 0-360 to 0-2*y
		// yaw = (y / 360.0) * yaw; // Not needed somehow No idea why nothing works with this

		// how many percent of y is yaw
		double progress = Math.abs(yaw / y);
		if (progress >= 0 && progress <= 1) {
			bossBar.setProgress(progress);
		}
	}

	private boolean hasToCalculateNew(Location loc1, Location loc2) {
		return (loc1.getBlockX() != loc2.getBlockX() || loc1.getBlockZ() != loc2.getBlockZ()
				|| loc1.getYaw() != loc2.getYaw());
	}

}
