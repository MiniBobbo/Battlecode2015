package firsttry;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.RobotType;

public class Barracks {
	static RobotController rc;

	public static void runOnce() {
		rc = RobotPlayer.rc;
	}

	public static void run() {
		try {

			if (rc.isCoreReady() && rc.getTeamOre() > 40) {
				int numSoldier = rc.readBroadcast(C.numSoldiers);
				int wantSoldier = rc.readBroadcast(C.buildSoldiers);
				
				if(numSoldier < wantSoldier)
					trySpawn(Direction.NORTH, RobotType.SOLDIER);
					
			}

		} catch (GameActionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		rc.yield();
		
	}
	// This method will attempt to spawn in the given direction (or as close to
	// it as possible)
	static void trySpawn(Direction d, RobotType type)
			throws GameActionException {
		int offsetIndex = 0;
		int[] offsets = { 0, 1, -1, 2, -2, 3, -3, 4 };
		int dirint = C.directionToInt(d);
		while (offsetIndex < 8
				&& !rc.canSpawn(
						C.directions[(dirint + offsets[offsetIndex] + 8) % 8],
						type)) {
			offsetIndex++;
		}
		if (offsetIndex < 8) {
			rc.spawn(C.directions[(dirint + offsets[offsetIndex] + 8) % 8],
					type);
		}
	}

}
