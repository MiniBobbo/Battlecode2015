package firsttry;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;

public class Soldier {

	public static RobotController rc;
	public static RobotInfo[] enemiesNearby;

	public static boolean defend = true;

	public static void runOnce() {
		rc = RobotPlayer.rc;
	}

	public static void run() {
		enemiesNearby = rc.senseNearbyRobots(C.mySense, C.enemyTeam);

		if (enemiesNearby.length > 0)
			micro();
		else
			macro();
	}

	public static void macro() {
		try {

			if (rc.isCoreReady()) {
				if (defend) {
					if(rc.readBroadcast(C.chargeOrder)==1)
						defend = false;
					if (rc.isCoreReady()) {
						MoveModule.tryMove(rc.getLocation().directionTo(
								C.myTowers()[0]));
					}

				} else {
					Direction attackDir = rc.getLocation().directionTo(
							rc.senseEnemyTowerLocations()[0]);
					if (attackDir == null)
						attackDir = rc.getLocation().directionTo(C.eBase());
					MoveModule.tryMove(attackDir);
				}
			}
		} catch (GameActionException e) {
			e.printStackTrace();
		}

		rc.yield();
	}

	public static void micro() {
		if (rc.isWeaponReady()) {
			RobotInfo target = null;
			double lowHP = 99999;
			for (RobotInfo r : enemiesNearby) {
				if(rc.canAttackLocation(r.location)) {
					if(r.health < lowHP) {
						target = r;
						lowHP = r.health;
					}
				}
			}
			if(target!= null) {
				try {
					rc.attackLocation(target.location);
				} catch (GameActionException e) {
					e.printStackTrace();
				}
			} else {
				try {
					if(rc.isCoreReady())
					MoveModule.tryMove(rc.getLocation().directionTo(enemiesNearby[0].location));
				} catch (GameActionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
