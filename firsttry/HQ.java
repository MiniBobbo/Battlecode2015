package firsttry;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;

public class HQ {
	static RobotController rc;

	/**
	 * Build order Helipad - 300 ore
	 * 
	 */

	static int stage = 0;
	static RobotType[] buildOrder = { RobotType.BARRACKS, RobotType.BARRACKS,
			RobotType.BARRACKS, RobotType.TECHNOLOGYINSTITUTE,
			RobotType.TRAININGFIELD };
	static int[] cutoffs = { 200, 300, 300, 200, 200 };

	public static void runOnce() {
		rc = RobotPlayer.rc;
		rc.setIndicatorString(2, "Build Stage: 0");
	}

	public static void run() {
		RobotInfo[] myRobots = rc.senseNearbyRobots(999999, C.myTeam);
		int numSoldiers = 0;
		int numBashers = 0;
		int numBeavers = 0;
		int numBarracks = 0;
		int numDrones = 0;
		int numHelis = 0;
		int numTech = 0;
		int numTrain = 0;

		for (RobotInfo r : myRobots) {
			RobotType type = r.type;
			if (type == RobotType.SOLDIER) {
				numSoldiers++;
			} else if (type == RobotType.BASHER) {
				numBashers++;
			} else if (type == RobotType.BEAVER) {
				numBeavers++;
			} else if (type == RobotType.BARRACKS) {
				numBarracks++;
			} else if (type == RobotType.DRONE) {
				numDrones++;
			} else if (type == RobotType.HELIPAD)
				numHelis++;
			else if (type == RobotType.TECHNOLOGYINSTITUTE)
				numTech++;
			else if (type == RobotType.TRAININGFIELD)
				numTrain++;
		}

		try {
			rc.broadcast(C.numBeavers, numBeavers);
			rc.broadcast(C.numSoldiers, numSoldiers);
			rc.broadcast(C.numBashers, numBashers);
			rc.broadcast(C.numBarracks, numBarracks);
			rc.broadcast(C.numDrones, numDrones);
			rc.broadcast(C.numHelis, numHelis);
			rc.broadcast(C.numTrain, numTrain);
			rc.broadcast(C.numTech, numTech);
			
			rc.broadcast(C.buildSoldiers, 100);
			rc.setIndicatorString(1, "Build Order : " + rc.readBroadcast(C.buildOrderChannel));

			//HQ gives supplies to beavers around itself.
			if(rc.getSupplyLevel() > 0) {
				RobotInfo[] nearby = rc.senseNearbyRobots(15, C.myTeam);
				for(RobotInfo r: nearby) {
					//rc.transferSupplies((int) (rc.getSupplyLevel()/nearby.length), r.location);
				}
			}
			
			//If we have over 30 soldiers, signal them to attack the towers.
			if(numSoldiers >= 30)
				rc.broadcast(C.chargeOrder, 1);
			else
				rc.broadcast(C.chargeOrder, 0);
			
			
			if (rc.isCoreReady()) {
				if (numBeavers < 15)
					trySpawn(Direction.EAST, RobotType.BEAVER);
			}

			if (rc.isWeaponReady()) {
				attackSomething();
			}

			if (stage != cutoffs.length && rc.getTeamOre() >= cutoffs[stage]) {
				if (addToQueue(buildOrder[stage])) {
					stage++;
					rc.setIndicatorString(2, "Build stage : " + stage);
				}

			}
		} catch (GameActionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		rc.yield();

	}

	// This method will attack an enemy in sight, if there is one
	static void attackSomething() throws GameActionException {
		RobotInfo[] enemies = rc.senseNearbyRobots(C.myRange, C.enemyTeam);
		if (enemies.length > 0) {
			rc.attackLocation(enemies[0].location);
		}
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

	static boolean addToQueue(RobotType rt) {
		try {
			if (rt == RobotType.BARRACKS) {
				rc.broadcast(C.buildBarracks,
						rc.readBroadcast(C.buildBarracks) + 1);
				rc.broadcast(C.buildOrderChannel, 1);
				return true;
			} else if (rt == RobotType.TECHNOLOGYINSTITUTE) {
				rc.broadcast(C.buildTech, rc.readBroadcast(C.buildTech) + 1);
				rc.broadcast(C.buildOrderChannel, 2);
				return true;
			} else if (rt == RobotType.TRAININGFIELD) {
				rc.broadcast(C.buildTrain, rc.readBroadcast(C.buildTrain) + 1);
				rc.broadcast(C.buildOrderChannel, 3);
				return true;
			}
		} catch (GameActionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
