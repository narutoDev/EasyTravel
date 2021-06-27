package easytravel.travel;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import easytravel.util.TimeConstants;
import easytravel.util.Utils;

public class ScoreboardUpdater extends BukkitRunnable {

	private Travel travel;
	private long travelStarted = System.currentTimeMillis();

	// Only recalculate things when the player has moved
	private Location lastLocationCalculated;
	private int totalDistance;
	private int distanceLeft;
	private float precentageDone;

	public ScoreboardUpdater(Travel travel) {
		this.travel = travel;
		this.totalDistance = getTotalDistance(this.travel);
	}

	@Override
	public void run() {
		Player p = this.travel.getTraveler();
		if (p == null) {
			return;
		}
		
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard board = manager.getNewScoreboard();
		String title = ChatColor.translateAlternateColorCodes('&', "&6&lEasyTravel");
		Objective obj = board.registerNewObjective("easytravel", "dummy", title);
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);

		// line
		String line = "&7" + "-".repeat(15);
		String scoreName = ChatColor.translateAlternateColorCodes('&', line);
		Score score = obj.getScore(scoreName);
		score.setScore(15);
		
		// destination
		line = "&9Destination:";
		scoreName = ChatColor.translateAlternateColorCodes('&', line);
		score = obj.getScore(scoreName);
		score.setScore(14);
		line = travel.getDestinationName();
		scoreName = ChatColor.translateAlternateColorCodes('&', line);
		score = obj.getScore(scoreName);
		score.setScore(13);
		
		score = obj.getScore("    ");
		score.setScore(12);

		// Total distance
		line = "&9Total distance to travel:";
		scoreName = ChatColor.translateAlternateColorCodes('&', line);
		score = obj.getScore(scoreName);
		score.setScore(11);
		line = "&r&b" + this.totalDistance + "&9 (blocks) ";
		scoreName = ChatColor.translateAlternateColorCodes('&', line);
		score = obj.getScore(scoreName);
		score.setScore(10);
		
		score = obj.getScore("   ");
		score.setScore(9);

		// Distance left
		line = "&9Distance to travel left:";
		scoreName = ChatColor.translateAlternateColorCodes('&', line);
		score = obj.getScore(scoreName);
		score.setScore(8);
		int distanceLeft = getDistanceLeft(this.travel);
		line = "&r&b" + distanceLeft + "&9 (blocks)";
		scoreName = ChatColor.translateAlternateColorCodes('&', line);
		score = obj.getScore(scoreName);
		score.setScore(7);
		
		score = obj.getScore("  ");
		score.setScore(6);

		// Percentage done
		line = "&9Already traveled:";
		scoreName = ChatColor.translateAlternateColorCodes('&', line);
		score = obj.getScore(scoreName);
		score.setScore(5);
		float percentageDone = getPercentageDone(this.travel);
		line = "&r&b" + percentageDone + "&r&9%";
		scoreName = ChatColor.translateAlternateColorCodes('&', line);
		score = obj.getScore(scoreName);
		score.setScore(4);
		
		score = obj.getScore(" ");
		score.setScore(3);

		// Time traveled
		long timeTraveled = System.currentTimeMillis() - travelStarted;
		timeTraveled = Math.round(timeTraveled / 1000); // milliseconds to seconds

		// hours
		int hours = (int) Math.ceil(timeTraveled / TimeConstants.SECONDS_PER_HOUR);
		timeTraveled -= hours * TimeConstants.SECONDS_PER_HOUR;

		// minutes
		int minutes = (int) Math.ceil(timeTraveled / TimeConstants.SECONDS_PER_MINUTE);
		timeTraveled -= minutes * TimeConstants.SECONDS_PER_MINUTE;

		// seconds
		int seconds = (int) timeTraveled;

		scoreName = ChatColor.translateAlternateColorCodes('&', "&9Time traveled:");
		score = obj.getScore(scoreName);
		score.setScore(2);

		String timeSring = "&b" + addLeading0(hours) + "&r&7:&r&b" + addLeading0(minutes) + "&r&7:&r&b"
				+ addLeading0(seconds);
		scoreName = ChatColor.translateAlternateColorCodes('&', timeSring);
		score = obj.getScore(scoreName);
		score.setScore(1);

		p.setScoreboard(board);
	}

	private int getTotalDistance(Travel travel) {
		int x1 = this.travel.getStartingPoint().getX();
		int z1 = this.travel.getStartingPoint().getZ();
		int x2 = this.travel.getDestinationPoint().getX();
		int z2 = this.travel.getDestinationPoint().getZ();
		int totalDistance = (int) Math.sqrt(Utils.square(x1 - x2) + Utils.square(z1 - z2));

		return totalDistance;
	}

	private int getDistanceLeft(Travel travel) {
		if (!hasMoved()) {
			return this.distanceLeft;
		}
		Player p = travel.getTraveler();

		int x1 = p.getLocation().getBlockX();
		int z1 = p.getLocation().getBlockZ();
		int x2 = this.travel.getDestinationPoint().getX();
		int z2 = this.travel.getDestinationPoint().getZ();
		int distanceLeft = (int) Math.sqrt(Utils.square(x1 - x2) + Utils.square(z1 - z2));

		this.distanceLeft = distanceLeft;
		return this.distanceLeft;
	}

	private float getPercentageDone(Travel travel) {
		if (!hasMoved()) {
			return this.precentageDone;
		}
		int distanceTraveled = Math.max(0, totalDistance - distanceLeft);

		float percentageDone = 0;
		if (distanceTraveled > 0) {
			percentageDone = ((float) distanceTraveled / (float) totalDistance) * 100f;
			percentageDone = Math.round(percentageDone * 10f) / 10f;
		}

		this.precentageDone = percentageDone;
		return this.precentageDone;
	}

	private boolean hasMoved() {
		Player p = this.travel.getTraveler();
		Location loc = this.lastLocationCalculated;

		if (p == null || loc == null) {
			return true;
		}

		Location currentLocation = p.getLocation();
		boolean x = currentLocation.getBlockX() == loc.getBlockX();
		boolean z = currentLocation.getBlockZ() == loc.getBlockZ();

		if (x && z) {
			return false;
		} else {
			return true;
		}
	}

	private String addLeading0(int i) {
		String s = i + "";
		if (s.length() < 2) {
			s = "0" + s;
		}
		return s;
	}

}

// character limit per line = 40 inclusive color format codes
// IMPORTANT when adding blank lines: multiple lines cannot have the same value
