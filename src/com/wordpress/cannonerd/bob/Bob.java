package com.wordpress.cannonerd.bob;

import lejos.nxt.*;
import lejos.util.Delay;
//import lejos.util.Delay;
import lejos.util.PilotProps;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.navigation.DifferentialPilot;


public class Bob
{
	static RegulatedMotor leftMotor;
	static RegulatedMotor rightMotor;
	static RegulatedMotor clawMotor;	
	public static void main(String[] args ) throws Exception{
		System.out.println("I'm Bob! <3!");
		
		PilotProps pp = new PilotProps();
    	pp.loadPersistentValues();
    	float wheelDiameter = Float.parseFloat(pp.getProperty(PilotProps.KEY_WHEELDIAMETER, "5.96"));
    	float trackWidth = Float.parseFloat(pp.getProperty(PilotProps.KEY_TRACKWIDTH, "15.0"));
    	leftMotor = PilotProps.getMotor(pp.getProperty(PilotProps.KEY_LEFTMOTOR, "C"));
    	rightMotor = PilotProps.getMotor(pp.getProperty(PilotProps.KEY_RIGHTMOTOR, "A"));
    	clawMotor = PilotProps.getMotor(pp.getProperty(PilotProps.KEY_LEFTMOTOR , "B"));
    	boolean reverse = Boolean.parseBoolean(pp.getProperty(PilotProps.KEY_REVERSE,"true"));
    	UltrasonicSensor sonic = new UltrasonicSensor(SensorPort.S4);
		
    	DifferentialPilot robot = new DifferentialPilot(wheelDiameter,trackWidth, leftMotor,rightMotor,reverse);
 		
		robot.setAcceleration(4000);
		robot.setTravelSpeed(10); // cm/sec
		robot.setRotateSpeed(90); // deg/sec
		clawMotor.setSpeed(50);
		sonic.continuous();
		Button.waitForPress();
		
		int noticeDistance = 30;
		System.out.println("Distance is 30");
		while (true){
			//Sound.beepSequenceUp();
			while (sonic.getDistance() > noticeDistance){
				robot.forward();
			}
			int sonicD = sonic.getDistance();
			System.out.println(sonicD);
			if (sonicD <= noticeDistance) {
				robot.stop();
				Delay.msDelay(500);
				clawMotor.rotate(28);
				clawMotor.rotate(-28);
				Delay.msDelay(500);
				System.out.println("PUM!!" + sonicD);
				robot.travel(noticeDistance + 4,false);
				clawMotor.rotate(130);
				Delay.msDelay(1000);
				robot.rotate(90);
				robot.travel(10,false);
				clawMotor.rotate(-130);
				robot.backward();
				Delay.msDelay(1000);;
				robot.rotate(90);
		    }
		}
	}
}

