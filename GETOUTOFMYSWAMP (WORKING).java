package SWEN101_04;
import robocode.*;
import robocode.util.Utils;

import java.awt.Color;
import java.util.Vector;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/**
 * Test1 - a robot by (your name here)
 */
public class GETOUTOFMYSWAMP extends AdvancedRobot
{
	private MovementState moveState=MovementState.STANDBY;
	
	//different states of the robot
	private enum MovementState{
		TRAVEL, //needs to get back to the corner
		STANDBY, //normal patrolling routine
		INTRUDER, //someone has come very close to the corner
		ASSAULT //in the case of 1 robot remaining, where the enemy will win if no action is taken
	}
	
	private ScannedRobotEvent currentTarget=null;
	
	public void run() {
		out.println("height: "+this.getBattleFieldHeight()+" width: "+this.getBattleFieldWidth());
		//setAdjustGunForRobotTurn(true);
		setColors(Color.red,Color.blue,Color.green); // body,gun,radar
		
		//super.setAhead(10000);
		
		while(true) {
			switch(moveState){
			case INTRUDER:
				//turnGunRight(1);
				track();
				break;
			default:
				//turnGunRight(10);
				turnRight(10);
				break;
			}
			
		}
	}
	
	//changes state
	private void setState(MovementState state){
		moveState=state;
	}
	
	//executes expected tracking behavior, which is an aggressive pursuit state
	private void track(){
		
		//we want to find others' angle relative to the radar
		double normal=getHeading();
		double targetAngle=normal+currentTarget.getBearing();
		double angleFromGun=Utils.normalRelativeAngleDegrees(targetAngle-getGunHeading());
		
		//super.setTurnGunRight(angleFromGun);
		super.setTurnRight(angleFromGun);
		
		
		super.setAhead(1000);
		
		if(angleFromGun<=1){//&&super.getGunHeat()==0) {
			fire(2);
		}
		scan();
	}
	
	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		setState(MovementState.INTRUDER);
		
		
		if(currentTarget==null||e.getDistance()<=currentTarget.getDistance()||e.getName().equals(currentTarget.getName()));
			currentTarget=e;
			
			
		
	}

	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
	public void onHitByBullet(HitByBulletEvent e) {
		// Replace the next line with any behavior you would like
		//back(10);
	}
	
	/**
	 * onHitWall: What to do when you hit a wall
	 */
	public void onHitWall(HitWallEvent e) {
		
	}	
	
	public void onRobotDeath(RobotDeathEvent e){
		setState(MovementState.STANDBY);
	}
}
