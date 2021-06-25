package easytravel.waypoint;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class PublicWaypointManager {

	// Map from public waypoints name to waypoint
	private HashMap<String, Waypoint> waypointMap = new HashMap<String, Waypoint>();

	// Map from player UUID to their amount of waypoints
	private HashMap<UUID, Integer> amountMap = new HashMap<UUID, Integer>();

	public boolean hasWaypoints(UUID playerID) {
		if (!amountMap.containsKey(playerID) || amountMap.get(playerID) < 1) {
			return false;
		}
		return true;
	}

	public boolean existWaypoint(String waypointName) {
		return waypointMap.containsKey(waypointName);
	}

	public int getPublicWaypointsTotalAmount() {
		return waypointMap.size();
	}

	public boolean hasWaypoint(UUID playerID, String waypointName) {
		if (!hasWaypoints(playerID) || !waypointMap.containsKey(waypointName)
				|| !waypointMap.get(waypointName).getOwnerID().equals(playerID)) {
			return false;
		}
		return true;
	}

	public int getWaypointAmount(UUID playerID) {
		if (!hasWaypoints(playerID)) {
			return 0;
		}
		return amountMap.get(playerID);
	}

	public Waypoint getWaypoint(String waypointName) {
		if (!existWaypoint(waypointName)) {
			return null;
		}

		return waypointMap.get(waypointName);
	}

	// returns if the waypoint could be added to the list
	public synchronized boolean addWaypoint(UUID playerID, Waypoint waypoint) {
		if (existWaypoint(waypoint.getWaypointName())) {
			return false;
		}

		waypointMap.put(waypoint.getWaypointName(), waypoint);
		if (!hasWaypoints(playerID)) {
			amountMap.put(playerID, 1);
		} else {
			amountMap.replace(playerID, amountMap.get(playerID) + 1);
		}
		return true;

	}

	public void removeWaypoint(UUID playerID, String waypointName) {
		if (!hasWaypoint(playerID, waypointName)) {
			return;
		}
		waypointMap.remove(waypointName);
		amountMap.replace(playerID, Math.max(amountMap.get(playerID) - 1, 0));
	}

	public void removePlayer(UUID playerID) {
		amountMap.remove(playerID);
	}

	public synchronized void renameWaypoint(UUID playerID, String waypointName, String newWaypointName) {
		if (waypointName == null || newWaypointName == null) {
			return;
		}
		if (waypointName.equals(newWaypointName)) {
			return;
		}
		if (waypointName.isBlank() || newWaypointName.isBlank()) {
			return;
		}
		if (!hasWaypoint(playerID, waypointName)) {
			return;
		}

		Waypoint newWaypoint = waypointMap.get(waypointName);
		newWaypoint.setWaypointName(newWaypointName);

		waypointMap.remove(waypointName);
		waypointMap.put(newWaypointName, newWaypoint);

	}

	public Waypoint[] getAllWaypoints() {
		if (waypointMap.keySet().size() < 1) {
			return null;
		}

		Collection<Waypoint> collection = waypointMap.values();
		Waypoint[] waypoints = new Waypoint[collection.size()];
		collection.toArray(waypoints);

		return waypoints;
	}

}
