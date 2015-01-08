package firsttry;

import battlecode.common.*;
import java.util.*;

public class RobotPlayer {
	static RobotController rc;
	static Team myTeam;
	static Team enemyTeam;
	static int myRange;
	static Random rand;
	static Direction[] directions = {Direction.NORTH, Direction.NORTH_EAST, Direction.EAST, Direction.SOUTH_EAST, Direction.SOUTH, Direction.SOUTH_WEST, Direction.WEST, Direction.NORTH_WEST};
	static RobotType type;
	
	public static void run(RobotController tomatojuice) {
		rc = tomatojuice;
		type = rc.getType();
		
		MoveModule.init();
		C.init();
		
		if(type == RobotType.HQ){
			HQ.runOnce();
			while(true)
				HQ.run();
		}
		if(type == RobotType.BEAVER) {
			Beav.runOnce();
			while(true)
				Beav.run();
		}
		
		if(type == RobotType.TOWER) {
			Tow.runOnce();
			while(true)
				Tow.run();
		}
		if(type == RobotType.DRONE) {
			Drone.runOnce();
			while(true)
				Drone.run();
		}
		
		if(type == RobotType.HELIPAD) {
			Heli.runOnce();
			while(true)
				Heli.run();
		}
		
		if(type == RobotType.BARRACKS) {
			Barracks.runOnce();
			while(true)
				Barracks.run();
		}
		
		if(type == RobotType.SOLDIER) {
			Soldier.runOnce();
			while(true)
				Soldier.run();
		}
	}
}
