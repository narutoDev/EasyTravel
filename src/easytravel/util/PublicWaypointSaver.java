package easytravel.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import easytravel.Main;
import easytravel.waypoint.Waypoint;

public class PublicWaypointSaver implements Runnable {

	@Override
	public void run() {
		Waypoint[] waypoints = Main.getPublicManager().getAllWaypoints();
		if (waypoints == null || waypoints.length < 1) {
			if (!WaypointIO.getPublicSaveFile().exists()) {
				return;
			}
			WaypointIO.getPublicSaveFile().delete();
			return;
		}

		JsonArray array = new JsonArray();

		for (Waypoint wp : waypoints) {
			JsonObject currentObject = Waypoint.toJSON(wp);
			currentObject.addProperty("uuid", wp.getOwnerID().toString());
			array.add(currentObject);
		}

		File saveFile = WaypointIO.getPublicSaveFile();

		FileWriter fileWriter;
		try {
			if (!saveFile.exists()) {
				saveFile.getParentFile().mkdirs();
				saveFile.createNewFile();
			}
			fileWriter = new FileWriter(saveFile);
			fileWriter.write(array.toString());
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Main.LogToConsole("Public waypoints saved!");
	}

}
