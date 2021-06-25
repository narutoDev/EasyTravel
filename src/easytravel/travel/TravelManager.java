package easytravel.travel;

import java.util.HashMap;
import java.util.UUID;

public class TravelManager {

	private HashMap<UUID, Travel> travelMap = new HashMap<UUID, Travel>();
	
	
	public void registerTravel(UUID playerID, Travel travel) {
		travelMap.put(playerID, travel);
	}
	
	public boolean hasTravel(UUID playerID) {
		return travelMap.get(playerID) != null;
	}
	
	public void removeTravel(UUID playerID) {
		travelMap.get(playerID).stopUpdaters();
		travelMap.remove(playerID);
	}
	
	public Travel getTravel(UUID playerID) {
		return travelMap.get(playerID);
	}

}
