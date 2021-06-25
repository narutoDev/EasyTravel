package easytravel.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import easytravel.Main;
import easytravel.waypoint.Waypoint;

public class WaypointIO {
	public static void saveWaypoints(UUID playerID) {
		Waypoint[] waypoints = Main.getManager().getAllWaypoints(playerID);
		if (waypoints == null || waypoints.length < 1) {
			return;
		}

		JsonObject playerData = new JsonObject();
		JsonArray array = new JsonArray();
		playerData.add("waypoints", array);

		for (Waypoint wp : waypoints) {
			array.add(Waypoint.toJSON(wp));
		}

		File saveFile = getSaveFileForPlayer(playerID);

		FileWriter fileWriter;
		try {
			fileWriter = new FileWriter(saveFile);
			fileWriter.write(playerData.toString());
			fileWriter.close();
			Main.LogToConsole("Saved " + array.size() + " waypoint(s) for " + playerID);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void loadWaypoints(UUID playerID) {
		File saveFile = getSaveFileForPlayer(playerID);

		if (!saveFile.exists()) {
			return;
		}

		String saveFileContent = null;
		FileInputStream fis;
		try {
			fis = new FileInputStream(saveFile);
			byte[] saveFileContentsRaw = fis.readAllBytes();
			saveFileContent = new String(saveFileContentsRaw);
		} catch (IOException e) {
			Main.LogToConsole("Error while loading " + saveFile.getAbsolutePath());
			e.printStackTrace();
		}
		if (saveFileContent == null) {
			Main.LogToConsole("Error after loading " + saveFile.getAbsolutePath());
			return;
		}

		JsonParser jsonParser = new JsonParser();
		JsonArray array = jsonParser.parse(saveFileContent).getAsJsonObject().get("waypoints").getAsJsonArray();

		if (array.size() < 1) {
			return;
		}

		for (int i = 0; i < array.size(); i++) {
			Main.getManager().addWaypoint(playerID, Waypoint.fromJSON(array.get(i).getAsJsonObject(), playerID));
		}
		Main.LogToConsole("Loaded " + array.size() + " waypoint(s) for " + playerID);

	}

	public static void loadPublicWaypoints() {
		File saveFile = getPublicSaveFile();

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
			String playerIDString = currentObject.get("uuid").getAsString();
			UUID playerID = UUID.fromString(playerIDString);
			Waypoint currentWaypoint = Waypoint.fromJSON(array.get(i).getAsJsonObject(), playerID);
			Main.getPublicManager().addWaypoint(playerID, currentWaypoint);
		}

		Main.LogToConsole(Main.getPublicManager().getPublicWaypointsTotalAmount() + " public waypoint(s) loaded!");

	}

	public static void savePublicWaypoints() {
		Waypoint[] waypoints = Main.getPublicManager().getAllWaypoints();
		if (waypoints == null || waypoints.length < 1) {
			if (!getPublicSaveFile().exists()) {
				return;
			}
			getPublicSaveFile().delete();
			return;
		}

		JsonArray array = new JsonArray();

		for (Waypoint wp : waypoints) {
			JsonObject currentObject = Waypoint.toJSON(wp);
			currentObject.addProperty("uuid", wp.getOwnerID().toString());
			array.add(currentObject);
		}

		File saveFile = getPublicSaveFile();

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

		Main.LogToConsole(array.size() + " public waypoint(s) saved");
	}

	public static File getSaveFileForPlayer(UUID playerID) {
		File saveDirectory = new File(Main.getDataRootFolder(), "/waypoints");
		String saveFileName = playerID.toString() + ".json";
		File saveFile = new File(saveDirectory, saveFileName);

		return saveFile;
	}

	public static File getPublicSaveFile() {
		File saveDirectory = new File(Main.getDataRootFolder(), "/publicwaypoints");
		String saveFileName = "/publicWaypoints.json";
		File saveFile = new File(saveDirectory, saveFileName);

		return saveFile;
	}
}
