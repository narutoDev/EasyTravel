package easytravel.waypoint;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.World;

import com.google.gson.JsonObject;

import easytravel.util.Point3D;

public class Waypoint {

	// Coordinates and world
	Point3D point;
	private World world;

	private String waypointName;

	private UUID ownerID;

	public Waypoint(UUID playerID, int x, int y, int z, World world, String waypointName) {
		this.ownerID = playerID;

		this.waypointName = waypointName;

		Point3D point = new Point3D(x, y, z);
		this.point = point;
		this.world = world;
	}

	public static Waypoint fromJSON(JsonObject json, UUID playerID) {
		int x = json.get("x").getAsInt();
		int y = json.get("y").getAsInt();
		int z = json.get("z").getAsInt();
		World world = Bukkit.getWorld(json.get("world").getAsString());
		String name = json.get("name").getAsString();

		Waypoint wp = new Waypoint(playerID, x, y, z, world, name);
		return wp;
	}

	public static JsonObject toJSON(Waypoint waypoint) {
		JsonObject object = new JsonObject();

		object.addProperty("name", waypoint.waypointName);

		object.addProperty("x", waypoint.point.getX());
		object.addProperty("y", waypoint.point.getY());
		object.addProperty("z", waypoint.point.getZ());
		object.addProperty("world", waypoint.world.getName());

		return object;

	}

	public static boolean isValidWaypoint(JsonObject json) {
		return 
		json.get("x") != null &&
		json.get("y") != null &&
		json.get("z") != null &&
		json.get("world") != null &&
		json.get("name") != null;
	}

	public int getX() {
		return point.getX();
	}

	public void setX(int x) {
		this.point.setX(x);
	}

	public int getY() {
		return this.point.getY();
	}

	public void setY(int y) {
		this.point.setY(y);
	}

	public int getZ() {
		return this.point.getZ();
	}

	public void setZ(int z) {
		this.point.setZ(z);
	}

	public String getWorldName() {
		return this.world.getName();
	}

	public UUID getOwnerID() {
		return this.ownerID;
	}

	public String getWaypointName() {
		return this.waypointName;
	}

	public void setWaypointName(String waypointName) {
		this.waypointName = waypointName;
	}

}
