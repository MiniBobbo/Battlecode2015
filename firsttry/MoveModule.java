package firsttry;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;

public class MoveModule {
	private static RobotController rc;

	public static void init() {
		rc = RobotPlayer.rc;
	}

	/**
	 * Attempts to move in a direction.
	 * 
	 * @param d
	 *            the direction to move.
	 * @return True if we moved. Otherwise false.
	 * @throws GameActionException
	 */
	static boolean tryMove(Direction d) throws GameActionException {
		int offsetIndex = 0;
		int[] offsets = { 0, 1, -1, 2, -2 };
		int dirint = C.directionToInt(d);
		while (offsetIndex < 5
				&& !rc.canMove(C.directions[(dirint + offsets[offsetIndex] + 8) % 8])) {
			offsetIndex++;
		}
		if (offsetIndex < 5) {
			rc.move(C.directions[(dirint + offsets[offsetIndex] + 8) % 8]);
			return true;
		}
		return false;
	}

	/**
	 * Forces a move in one direction.  Doesn't try to move around anything.
	 * @param d Direction to move.
	 * @return True if moved.  Otherwise false.
	 * @throws GameActionException
	 */
	static boolean forceMove(Direction d) throws GameActionException {
		if (!rc.canMove(d))
			return false;

		rc.move(d);
		return true;
	}
}
