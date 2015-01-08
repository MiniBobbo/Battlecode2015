package firsttry;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.RobotType;

public class Heli {

	static RobotController rc;

	public static void runOnce() {
		rc = RobotPlayer.rc;

	}

	public static void run() {
		try {

			int cDrones = rc.readBroadcast(C.numDrones);
			int wantDrones = rc.readBroadcast(C.buildDrones);
			
			rc.setIndicatorString(0, "Have: " + cDrones + "   Want: " + wantDrones);
			
			if (cDrones < wantDrones && rc.getTeamOre() > 300 && rc.isCoreReady()) {
				trySpawn(Direction.NORTH, RobotType.DRONE);
			}

		} catch (GameActionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		rc.yield();
	}

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
