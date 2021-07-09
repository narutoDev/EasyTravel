package easytravel.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import easytravel.Main;
import easytravel.waypoint.Waypoint;

public class PublicWaypointLoader implements Runnable {

	@Override
	public void run() {
		File saveFile = WaypointIO.getPublicSaveFile();

		if (!saveFile.exists()) {
			Main.LogToConsole("No public waypoint file found!");
			return;
		}

		String saveFileContent = null;
		FileInputStream fis;
		try {
			fis = new FileInputStream(saveFile);
			byte[] saveFileContentsRaw = fis.readAllBytes();
			saveFileContent = new String(saveFileContentsRaw);
		} catch (IOException e) {
			System.out.println("Error while loading " + saveFile.getAbsolutePath());
			e.printStackTrace();
		}
		if (saveFileContent == null) {
			System.out.println("Error after loading " + saveFile.getAbsolutePath());
			return;
		}

		JsonParser jsonParser = new JsonParser();
		JsonArray array = jsonParser.parse(saveFileContent).getAsJsonArray();

		for (int i = 0; i < array.size(); i++) {
			JsonObject currentObject = array.get(i).getAsJsonObject();
			if (!Waypoint.isValidWaypoint(currentObject) || currentObject.get("uuid") == null) {
				System.out.println("Invalid waypoint in " + saveFile.getAbsolutePath());
				continue;
			}
			String playerIDString = currentObject.get("uuid").getAsString();
			UUID playerID = UUID.fromString(playerIDString);
			Waypoint currentWaypoint = Waypoint.fromJSON(array.get(i).getAsJsonObject(), playerID);
			Main.getPublicManager().addWaypoint(playerID, currentWaypoint);
		}

		Main.LogToConsole(Main.getPublicManager().getPublicWaypointsTotalAmount() + " public waypoints loaded!");
	}

}
