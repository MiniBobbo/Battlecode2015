package firsttry;

import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;

public class Tow {
	static RobotController rc;

	public static void runOnce() {
		rc = RobotPlayer.rc;

	}

	public static void run() {
		if (rc.isWeaponReady()) {
			RobotInfo target = getTarget();

			try {
				if (target != null)
					rc.attackLocation(getTarget().location);
			} catch (GameActionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static RobotInfo getTarget() {
		RobotInfo[] nearby = rc.senseNearbyRobots(C.myRange, C.enemyTeam);
		if (nearby.length == 0)
			return null;
		RobotInfo target = null;
		int distance = 999;
		for (RobotInfo r : nearby) {
			if (r.location.distanceSquaredTo(rc.getLocation()) < distance) {
				distance = r.location.distanceSquaredTo(rc.getLocation());
				target = r;
			}
		}
		return target;
	}

}
