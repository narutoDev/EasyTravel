package easytravel.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import easytravel.Main;
import easytravel.waypoint.Waypoint;

public class PrivateWaypointSaver implements Runnable {

	private UUID playerID;

	public PrivateWaypointSaver(UUID playerID) {
		this.playerID = playerID;
	}

	@Override
	public void run() {
		Waypoint[] waypoints = Main.getManager().getAllWaypoints(playerID);
		if (waypoints == null || waypoints.length < 1) {
			System.out.println(waypoints);
			System.out.println(waypoints.length);
			System.out.println("No waypoints saved for player " + playerID.toString() + "!");
			return;
		}

		JsonObject playerData = new JsonObject();
		JsonArray array = new JsonArray();
		playerData.add("waypoints", array);

		for (Waypoint wp : waypoints) {
			array.add(Waypoint.toJSON(wp));
		}

		File saveFile = WaypointIO.getSaveFileForPlayer(playerID);

		FileWriter fileWriter;
		try {
			if (!saveFile.exists()) {
				saveFile.getParentFile().mkdirs();
				saveFile.createNewFile();
			}
			fileWriter = new FileWriter(saveFile);
			fileWriter.write(playerData.toString());
			fileWriter.close();
		} catch (IOException e) {
			System.out.println("Error while saving waypoints for player " + playerID.toString() + "!");
			e.printStackTrace();
			return;
		}
		System.out.println("Successfully saved " + array.size() + " waypoints for player " + playerID.toString());
	}

}
