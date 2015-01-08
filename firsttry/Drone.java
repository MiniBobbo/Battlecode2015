package firsttry;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;

public class Drone {
	static RobotController rc;

	static Direction heading;

	/**
	 * States for drones. 1 - harrass. Fly around randomly and shoot things.
	 */
	static int state = 1;

	public static void runOnce() {
		rc = RobotPlayer.rc;
		heading = rc.getLocation().directionTo(C.eTowerLoc()[0]);
	}

	public static void run() {

		if (rc.isCoreReady()) {
			RobotInfo[] nearbyEnemies = rc.senseNearbyRobots(C.mySense,
					C.enemyTeam);

			if (nearbyEnemies.length > 0)
				micro(nearbyEnemies);
			else
				macro();

		}

		rc.yield();
	}

	public static void macro() {
		if (heading == null)
			heading = rc.getLocation().directionTo(C.eBase());
		try {
			if (!MoveModule.forceMove(heading))
				heading = C.intToDirection(C.rand().nextInt(8));
		} catch (GameActionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void micro(RobotInfo[] nearby) {

		try {

			for (RobotInfo r : nearby) {
				if (r.type.attackRadiusSquared >= C.myRange) {
					heading = r.location.directionTo(rc.getLocation());
					if (rc.isCoreReady()) {
						MoveModule.forceMove(heading);
					}

				} else if (rc.isWeaponReady()) {
					if (rc.canAttackLocation(r.location)) {
						rc.attackLocation(r.location);
					}
				}

				heading = C.intToDirection(C.rand().nextInt(8));
			}

		} catch (GameActionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}