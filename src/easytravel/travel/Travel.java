package easytravel.travel;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import easytravel.Main;
import easytravel.Settings;
import easytravel.util.Point3D;

public class Travel {

	private Player traveler;
	private Point3D destinationPoint;
	private Point3D startingPoint;
	private String destinationName;

	private ScoreboardUpdater scoreboardUpdater;

	public Travel(Player p, Point3D destinationPoint, Point3D startingPoint, String destinationName) {
		this.traveler = p;
		this.destinationPoint = destinationPoint;
		this.startingPoint = startingPoint;
		this.destinationName = destinationName;

		this.scoreboardUpdater = new ScoreboardUpdater(this);

		this.registerToManager();
		this.startUpdaters();
		this.sendStartMessage();
	}

	public void registerToManager() {
		Main.getTravelManager().registerTravel(getTravelerID(), this);
	}

	public void startUpdaters() {
		scoreboardUpdater.runTaskTimer(Main.getPlugin(), 1, Settings.TRAVEL_SCOREBOARD_UPDATE_DELAY);
	}

	public void stopUpdaters() {
		scoreboardUpdater.cancel();
	}

	public void sendStartMessage() {
		this.traveler.sendMessage(ChatColor.translateAlternateColorCodes('&',
				"&2Your travel to &r" + this.destinationName + "&r&2 has started now!"));
	}

	public UUID getTravelerID() {
		return traveler.getUniqueId();
	}

	public Point3D getDestinationPoint() {
		return destinationPoint;
	}

	public void setDestinationPoint(Point3D destinationPoint) {
		this.destinationPoint = destinationPoint;
	}

	public Point3D getStartingPoint() {
		return startingPoint;
	}

	public void setStartingPoint(Point3D startingPoint) {
		this.startingPoint = startingPoint;
	}

	public Player getTraveler() {
		return this.traveler;
	}

	public String getDestinationName() {
		return this.destinationName;
	}

}
