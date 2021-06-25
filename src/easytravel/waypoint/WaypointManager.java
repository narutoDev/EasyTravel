package easytravel.waypoint;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class WaypointManager {

	// Map from player uuid to map of their waypoints name to waypoint
	private HashMap<UUID, HashMap<String, Waypoint>> waypointMap = new HashMap<UUID, HashMap<String, Waypoint>>();

	public boolean hasWaypoints(UUID playerID) {
		if (waypointMap.get(playerID) == null) {
			return false;
		}
		return waypointMap.get(playerID).size() > 0;
	}

	public boolean hasWaypoint(UUID playerID, String waypointName) {
		if (!hasWaypoints(playerID)) {
			return false;
		}
		return waypointMap.get(playerID).containsKey(waypointName);
	}

	public int getWaypointAmount(UUID playerID) {
		if (!hasWaypoints(playerID)) {
			return 0;
		}
		return waypointMap.get(playerID).size();
	}

	public Waypoint getWaypoint(UUID playerID, String waypointName) {
		if (!hasWaypoint(playerID, waypointName)) {
			return null;
		}

		return waypointMap.get(playerID).get(waypointName);
	}

	public Waypoint[] getAllWaypoints(UUID playerID) {
		if (!hasWaypoints(playerID)) {
			return null;
		}

		Collection<Waypoint> collection = waypointMap.get(playerID).values();
		Waypoint[] waypoints = new Waypoint[collection.size()];
		collection.toArray(waypoints);

		return waypoints;
	}

	public synchronized void addWaypoint(UUID playerID, Waypoint waypoint) {
		if (waypointMap.get(playerID) == null) {
			waypointMap.put(playerID, new HashMap<String, Waypoint>());
		}
		waypointMap.get(playerID).put(waypoint.getWaypointName(), waypoint);
	}

	public synchronized void removeWaypoint(UUID playerID, String waypointName) {
		if (!hasWaypoints(playerID)) {
			return;
		}
		waypointMap.get(playerID).remove(waypointName);
	}

	public void removePlayer(UUID playerID) {
		waypointMap.remove(playerID);
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

		HashMap<String, Waypoint> playerMap = waypointMap.get(playerID);
		Waypoint wp = playerMap.get(waypointName);
		playerMap.remove(waypointName);
		wp.setWaypointName(newWaypointName);
		playerMap.put(newWaypointName, wp);
		

	}

}
