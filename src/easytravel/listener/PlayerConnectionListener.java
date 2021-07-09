package easytravel.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import easytravel.Main;
import easytravel.io.PrivateWaypointLoader;
import easytravel.io.PrivateWaypointSaver;
import easytravel.io.WaypointIO;
import easytravel.util.ExtendedBukkitRunnable;
import easytravel.util.ThreadFinishListener;
import net.md_5.bungee.api.ChatColor;

public class PlayerConnectionListener implements Listener {

	// Save the waypoints of the players when they disconnect
	@EventHandler
	public void onDisconnect(PlayerQuitEvent e) {
		Player p = e.getPlayer();

		if (Main.getManager().hasWaypoints(p.getUniqueId())) {
			ExtendedBukkitRunnable saver = new ExtendedBukkitRunnable(new PrivateWaypointSaver(p.getUniqueId()));
			saver.addFinishListener(new ThreadFinishListener() {
				@Override
				public void onFinish() {
					Main.getManager().removePlayer(p.getUniqueId());
				}

			});
			saver.runTaskAsynchronously(Main.getPlugin());
		}

		// Remove travel
		if (Main.getTravelManager().hasTravel(p.getUniqueId())) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&',
					"&7Your travel to &r&b&l" + Main.getTravelManager().getTravel(p.getUniqueId()).getDestinationName()
							+ " &r&7has been stopped!"));
			Main.getTravelManager().removeTravel(p.getUniqueId());
		}
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();

		if (!WaypointIO.getSaveFileForPlayer(p.getUniqueId()).exists()) {
			Main.LogToConsole("No waypoints where loaded for " + p.getName());
			return;
		}

		// Start a new BukkitRunnable to load the waypoints of the player asynchronously
		ExtendedBukkitRunnable loader = new ExtendedBukkitRunnable(new PrivateWaypointLoader(p.getUniqueId()));
		loader.runTaskAsynchronously(Main.getPlugin());
	}

}
