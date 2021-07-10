package easytravel;

public class Settings {

	// how many a private waypoints a player is allowed to have (values less than 0 -> unlimited)
	public static final int MAX_PRIVATE_WAYPOINTS_PER_PLAYER = -1;
	
	// how many public waypoints are allowed in total for every player (values less than 0 -> unlimited)
	public static final int MAX_PUBLIC_WAYPOINTS_GLOBAL = -1;
	
	// how many public waypoints a single player is allowed to have	(values less than 0 -> unlimited)
	public static final int MAX_PUBLIC_WAYPOINTS_PER_PLAYER = -1;
	
	// if players should be able to see the creator of public waypoints
	public static final boolean DISPLAY_OWNER_OF_PUBLICWAYPOINTS = true;
	
	// the delay in ticks between the updates of the travel scoreboard
	public static final int TRAVEL_SCOREBOARD_UPDATE_DELAY = 10;
}
