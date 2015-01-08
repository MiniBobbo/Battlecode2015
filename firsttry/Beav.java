package firsttry;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;

public class Beav {

	/**
	 * Basic strategy:
	 * 
	 * Macro - Mine something near the base or a tower.
	 * 
	 * Micro - If we have the advantage, attack. Otherwise, run.
	 * 
	 */

	static RobotController rc;

	static Direction wander;
	
	public static void runOnce() {
		rc = RobotPlayer.rc;
		wander = C.intToDirection(C.rand().nextInt(8));
	}

	public static void run() {
		RobotInfo[] eNearby = rc.senseNearbyRobots(C.mySense, C.enemyTeam);
		
		//Beavers will resupply other buildings near themselves.
		if(rc.getSupplyLevel() > 30) {
		RobotInfo[] fNearby = rc.senseNearbyRobots(15, C.myTeam);
		if(fNearby.length > 0) {
			for(RobotInfo r:fNearby) {
				if(r.type == RobotType.BARRACKS) {
					try {
						rc.transferSupplies((int) (rc.getSupplyLevel() - 10), r.location);
					} catch (GameActionException e) {
						System.out.println(rc.getID()+ " unable to transfer supplies to " + r.type);
					}
				}
					
			}
		}
			
			
		}
			
		if (eNearby.length > 0) {
			micro();
		} else
			macro();

		rc.yield();
	}

	public static void macro() {
		if (rc.isCoreReady()) {

			try {
				//If we have an order in the build order channel, build something.
				int num = rc.readBroadcast(C.buildOrderChannel);
				if(num != 0) {
					switch(num) {
					case 1:
						if(rc.getTeamOre() > 200 && tryBuild(Direction.NORTH, RobotType.BARRACKS))
							rc.broadcast(C.buildOrderChannel, 0);
						break;
					}
				} else {
					if(rc.senseOre(rc.getLocation()) > 0) {
						rc.mine();
					} else {
						MoveModule.tryMove(Direction.EAST);
					}
					
				}
				
			} catch (GameActionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public static void micro() {
		if(rc.isCoreReady())
			try {
				MoveModule.tryMove(rc.getLocation().directionTo(C.myTowers()[0]));
			} catch (GameActionException e) {
				e.printStackTrace();
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
	   // This method will attempt to build in the given direction (or as close to it as possible)
		static boolean tryBuild(Direction d, RobotType type) throws GameActionException {
			int offsetIndex = 0;
			int[] offsets = {0,1,-1,2,-2,3,-3,4};
			int dirint = C.directionToInt(d);
			while (offsetIndex < 8 && !rc.canMove(C.directions[(dirint+offsets[offsetIndex]+8)%8])) {
				offsetIndex++;
			}
			if (offsetIndex < 8 && rc.getTeamOre() >= type.oreCost) {
				rc.build(C.directions[(dirint+offsets[offsetIndex]+8)%8], type);
				return true;
			}
			return false;
		}
}
