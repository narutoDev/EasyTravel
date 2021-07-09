package easytravel.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import easytravel.Main;
import easytravel.waypoint.Waypoint;

public class PrivateWaypointLoader implements Runnable {

	private UUID playerID;

	public PrivateWaypointLoader(UUID playerID) {
		this.playerID = playerID;
	}

	@Override
	public void run() {
		File saveFile = WaypointIO.getSaveFileForPlayer(playerID);

		if (!saveFile.exists()) {
			System.out.println("Private waypoint file " + saveFile.getAbsolutePath() + " doesn't exist!");
			return;
		}

		String saveFileContent = null;
		FileInputStream fis;
		try {
			fis = new FileInputStream(saveFile);
			byte[] saveFileContentsRaw = fis.readAllBytes();
			saveFileContent = new String(saveFileContentsRaw);
		} catch (IOException e) {
			System.out.println("IOException while loading " + saveFile.getAbsolutePath());
			e.printStackTrace();
		}
		if (saveFileContent == null) {
			System.out.println("Loading result for " + saveFile.getAbsolutePath() + " is null!");
			return;
		}

		JsonParser jsonParser = new JsonParser();
		JsonArray array = jsonParser.parse(saveFileContent).getAsJsonObject().get("waypoints").getAsJsonArray();

		if (array.size() < 1) {
			System.out.println("Parsed JSON waypoint array had a length of 0 for file " + saveFile.getAbsolutePath() + "!");
			return;
		}

		for (int i = 0; i < array.size(); i++) {
			Main.getManager().addWaypoint(playerID, Waypoint.fromJSON(array.get(i).getAsJsonObject(), playerID));
		}

		System.out.println(array.size() + " waypoint(s) where loaded from " + saveFile.getAbsolutePath() + "!");

	}

}
