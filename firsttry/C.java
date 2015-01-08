package firsttry;

import java.util.Random;

import battlecode.common.Direction;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.Team;

public class C {
	private static MapLocation eBaseLoc;
	public static RobotController r;

	private static Random random;
	
	//The channel that the HQ will record the gamestate.
	public static int gamestateChannel = 1000;
	
	//Order channels
	public static int orderChannel = 2000;
	public static int numOrderChannels = 10;
	
	//Channels
	public static int numBeavers = 100;
	public static int numSoldiers = 101;
	public static int numBashers = 102;
	public static int numBarracks = 103;
	public static int numMiner = 104;
	public static int numDrones = 105;
	public static int numHelis = 106;
	public static int numTech= 107;
	public static int numTrain= 108;
	
	public static int chargeOrder = 1000;
	
	public static int buildBeav = 200;
	public static int buildSoldiers = 201;
	public static int buildBashers= 202;
	public static int buildBarracks = 203;
	public static int buildMiner = 204;
	public static int buildDrones = 205;
	public static int buildHelis = 206;
	public static int buildTech = 207;
	public static int buildTrain = 208;
	
	public static int buildOrderChannel = 300;
	
	private static int towerCount = -1;
	private static MapLocation[] eTowerLoc;
	private static MapLocation[] towerLoc;
	
	
	static Team myTeam;
	static Team enemyTeam;

	static int myRange;
	static int mySense;
	public static Direction[] directions = {Direction.NORTH, Direction.NORTH_EAST, Direction.EAST, Direction.SOUTH_EAST, Direction.SOUTH, Direction.SOUTH_WEST, Direction.WEST, Direction.NORTH_WEST};

	
	public static void init() {
		r = RobotPlayer.rc;
		myTeam = r.getTeam();
		enemyTeam = myTeam.opponent();
		myRange = r.getType().attackRadiusSquared;
		mySense = r.getType().sensorRadiusSquared;

	}
	
	public static MapLocation eBase() {
		if(eBaseLoc == null)
			eBaseLoc = RobotPlayer.rc.senseEnemyHQLocation();
		return eBaseLoc;
	}
	
	/**
	 * Gets the number of towers.  Maybe remaining.  I'd have to check the JavaDocs...
	 * @return
	 */
	public static int towerCount() {
		if(towerCount == -1) {
			eTowerLoc();
		}
		return towerCount;
			
	}
	
	public static MapLocation[] myTowers() {
		if(towerLoc == null)
			towerLoc = r.senseTowerLocations();
		return towerLoc;
	}
	
	/**
	 * Gets the map locations of all the enemy towers.  Might get only the living ones.  I'll have to check.
	 * @return
	 */
	public static MapLocation[] eTowerLoc() {
		if(eTowerLoc == null) {
			eTowerLoc = r.senseEnemyTowerLocations();
			towerCount = eTowerLoc.length;
		}
		return eTowerLoc;
	}
	
	public static RobotController rc() {
		if (r == null)
			r = RobotPlayer.rc;
		return r;
	}
	
	static int directionToInt(Direction d) {
		switch(d) {
			case NORTH:
				return 0;
			case NORTH_EAST:
				return 1;
			case EAST:
				return 2;
			case SOUTH_EAST:
				return 3;
			case SOUTH:
				return 4;
			case SOUTH_WEST:
				return 5;
			case WEST:
				return 6;
			case NORTH_WEST:
				return 7;
			default:
				return -1;
		}
	}

	static Direction intToDirection(int i) {
		return directions[i];
	}
	
	static Random rand() {
		if(random == null) 
			random = new Random(r.getID());
		return random;
	}
	
	static Direction wander(Direction d, int wander) {
		int dir = directionToInt(d);
		if(rand().nextInt(2)==0)
			wander *=-1;
		dir+=wander;
		if(dir > 7)
			dir -=8;
		if(dir < 0)
			dir+=8;
		return directions[dir];
		
	}
}
