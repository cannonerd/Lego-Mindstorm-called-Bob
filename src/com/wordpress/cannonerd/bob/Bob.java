package com.wordpress.cannonerd.bob;

import lejos.nxt.*;
import lejos.util.Delay;
import lejos.util.PilotProps;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.*;

/**
 * Luokka <code>Bob</code> Bob on pieni robotti joka suorittaa pallojen etsimistä ja niiden nostelua
 * tai siirtelyä
 * @author Susanna Huhtanen
 * @version 1.0 (2.3.2012)
 */
public class Bob
{
	public static RegulatedMotor leftMotor;
	public static RegulatedMotor rightMotor;
	public static RegulatedMotor clawMotor;	
	public static UltrasonicSensor sonic;
	public static DifferentialPilot robot ;
	public static Logger log;
	public static int collected = 0;
	public static int i = 0;
	public static int noticeDistance = 30;
	    
   /**
    * Ensimmäinen pallon siirtely metodeista, nostaa pallon ja kääntyy 90 astetta ja 
    * menee vähän eteenpäin, laskee pallon ja peruuttaa pois
    */
    
	public static void ball(){
		log.write("We seez an object!");
		robot.stop();
		Delay.msDelay(500);
		System.out.println("Saw it!");
		clawMotor.setSpeed(200);
		clawMotor.rotate(28);
		clawMotor.rotate(-28);
		clawMotor.rotate(28);
		clawMotor.rotate(-28);
		clawMotor.setSpeed(50);
		Delay.msDelay(500);
		System.out.println("Only " + sonic.getDistance() + "cm to get it!");
		robot.travel(noticeDistance + 4,false);
		clawMotor.rotate(120);
		log.write("We haz it!");
		collected++;
		log.write("We haz collected" + collected + " ballz");
		Delay.msDelay(1000);
		robot.rotate(90);
		robot.travel(10,false);
		clawMotor.rotate(-120);
		robot.backward();
		Delay.msDelay(1000);;
		robot.rotate(90);
		i++;
	}
	
	  /**
    * Toinen pallonsiirtämis metodi. Nostaa pallon ja menee eteenpäin
    * 
    */

	  
	public static void baller(){
		log.write("We seez an object!");
		robot.stop();
		Delay.msDelay(500);
		System.out.println("Saw it!");
		clawMotor.setSpeed(200);
		clawMotor.rotate(28);
		clawMotor.rotate(-28);
		clawMotor.rotate(28);
		clawMotor.rotate(-28);
		clawMotor.setSpeed(50);
		Delay.msDelay(500);
		System.out.println("Only " + sonic.getDistance() + "cm to get it!");
		robot.travel(noticeDistance + 4,false);
		clawMotor.rotate(120);
		log.write("We haz it!");
		collected++;
		log.write("We haz collected" + collected + " ballz");
		Delay.msDelay(1000);
		robot.travel(30,false);
		clawMotor.rotate(-120);
		robot.backward();
		Delay.msDelay(1000);;
		robot.rotate(90);
		i++;
	}
	
    /**
    * Kolmas pallonsiirtämis metodi. Nostaa pallon ja laskee sen alas 
    * 
    */

	public static void ballest(){
		log.write("We seez an object!");
		robot.stop();
		Delay.msDelay(500);
		System.out.println("Saw it!");
		clawMotor.setSpeed(200);
		clawMotor.rotate(28);
		clawMotor.rotate(-28);
		clawMotor.setSpeed(50);
		Delay.msDelay(500);
		System.out.println("Only " + sonic.getDistance() + "cm to get it!");
		robot.travel(noticeDistance + 4,false);
		clawMotor.rotate(120);
		log.write("We haz it!");
		collected++;
		log.write("We haz collected" + collected + " ballz");
		Delay.msDelay(1000);
		clawMotor.rotate(-120);
		robot.backward();
		Delay.msDelay(1000);;
		robot.rotate(-90);
		i++;
	}
	
	public static void main(String[] args ) throws Exception{
	
   /**
    * Tulostaa ruudulle Bobin nimen ja sydämmen
    * 
    */

	
		System.out.println("I'm Bob! <3!");
		
		/**
    * Alustetaan kaikki mahdolliset robotin tarvitsemat ohjaukseen ja havainnointiin käytettävät
    * osat oikeilla arvoilla
    */

		
		log = new Logger();
		PilotProps pp = new PilotProps();
   	pp.loadPersistentValues();
   	float wheelDiameter = Float.parseFloat(pp.getProperty(PilotProps.KEY_WHEELDIAMETER, "5.96"));
   	float trackWidth = Float.parseFloat(pp.getProperty(PilotProps.KEY_TRACKWIDTH, "15.0"));
   	leftMotor = PilotProps.getMotor(pp.getProperty(PilotProps.KEY_LEFTMOTOR, "C"));
   	rightMotor = PilotProps.getMotor(pp.getProperty(PilotProps.KEY_RIGHTMOTOR, "A"));
   	clawMotor = PilotProps.getMotor(pp.getProperty(PilotProps.KEY_LEFTMOTOR , "B"));
   	boolean reverse = Boolean.parseBoolean(pp.getProperty(PilotProps.KEY_REVERSE,"true"));
   	sonic = new UltrasonicSensor(SensorPort.S4);
		robot = new DifferentialPilot(wheelDiameter,trackWidth, leftMotor,rightMotor,reverse);	
    	
		robot.setAcceleration(4000);
		robot.setTravelSpeed(10); // cm/sec
		robot.setRotateSpeed(90); // deg/sec
		clawMotor.setSpeed(50);
		sonic.continuous();

		Behavior b1 = new DriveForward();
    Behavior b2 = new PickUp();
    Behavior[] behaviorList =
    {
     		b1, b2
    };
    Button.waitForPress();
    	
		Arbitrator arbitrator = new Arbitrator(behaviorList);
    arbitrator.start();
		
  	}
}

/**
 * Suoraan BumberCarista lainattu luokka joka suorittaa eteenpäin menon. 
 * 
 */

class DriveForward implements Behavior
{

  private boolean _suppressed = false;

  public boolean takeControl()
  {
    return true;  // this behavior always wants control.
  }

  public void suppress()
  {
    _suppressed = true;// standard practice for suppress methods
  }

  public void action()
  {
    _suppressed = false;
    Bob.robot.forward();
    while (!_suppressed)
    {
      Thread.yield(); //don't exit till suppressed
    }
    Bob.robot.stop();
  }
}

/**
 * BumberCarin mallin mukaisesti rakenenttu luokka, joka suorittaa pallojen havainnoinnin ja niiden
 * sattumanvaraisen käsittelyn.
 */

class PickUp implements Behavior
{		
	public boolean takeControl()
  {
	Bob.sonic.ping();
    return Bob.sonic.getDistance() <= Bob.noticeDistance;
  }

  public void suppress()
  {
    //Since  this is highest priority behavior, suppress will never be called.
  }

  public void action()
  {
	  int k = (int) (Math.random() * 2 + 1.5);
	  switch (k) {
	  case 1:
		  Bob.ball();
		  break;
	  case 2:
		  Bob.baller();
		  break;
	  case 3:
		  Bob.ballest();
		  break;
	  default: Bob.ball();
		  break;
	  }
}
}
