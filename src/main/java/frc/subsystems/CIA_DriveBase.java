package frc.subsystems;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
//import edu.wpi.first.wpilibj.Encoder;

import frc.sensors.CIA_SparkMax; //Our own SparkMax Code

//TODO - Add Encoders to DriveBase
public class CIA_DriveBase {
    private CIA_SparkMax motorZero, motorOne;
    private SpeedControllerGroup leftGroup, rightGroup;
    private Solenoid shifter;
    private double lowSpeed, highSpeed, deadband, mathLeft, mathRight, overrideSpeed;
    private boolean inHighState = false;
    private boolean rightReverse, allReverse;
    private Encoder leftEncoder, rightEncoder;

    /*
    Below is a contructor that takes in the follwoing in order:
    Left Motor Port, Right Motor Port, Shifter Solenoid Port, Left Encoder Ports, Right Encoder Ports,
    The Deadband, The Low Speed, The High, The Override Speed, Right Side Reverse, All Sides Revesered
    */
    public CIA_DriveBase(int leftMotorsPort, int rightMotorsPort, int shifterSolenoidPort, int leftEncoderPortZero, int leftEncoderPortOne, int rightEncoderPortZero, int rightEncoderPortOne, double newDeadband, 
                        double newLowSpeed, double newHighSpeed, double newOverrideSpeed, boolean newRightReverse,
                        boolean newAllReverse){

        //Below creates the motor objects
        motorZero = new CIA_SparkMax(leftMotorsPort);
        motorOne = new CIA_SparkMax(rightMotorsPort);

        //Below sets each of the motors into groups
        leftGroup = new SpeedControllerGroup(motorZero);
        rightGroup = new SpeedControllerGroup(motorOne);

        //Below creates the encoders
        leftEncoder = new Encoder(leftEncoderPortZero, leftEncoderPortOne);
        rightEncoder = new Encoder(rightEncoderPortZero, rightEncoderPortOne);

        shifter = new Solenoid(shifterSolenoidPort); //Creates the solenoid object for shifting

        deadband = newDeadband; //Takes in the deadband variable

        lowSpeed = newLowSpeed; //Takes in the low speed variable
        highSpeed = newHighSpeed; //Takes in the high speed variable

        overrideSpeed = newOverrideSpeed; //Takes in the override speed variable
    }

    //Used to set the state of the gears
    private void updateGears(){
        if(inHighState){
            shifter.set(true);
        } else {
            shifter.set(false);
        }
    }

    /*
    Below is used for driving in arcade style. Switch Gears must be used with the 
    .getRawButtonPressed(int button) method for joysticks. This is to prevent it from tripping
    multiple times
    */
    public void arcadeDrive(double yAxis, double xAxis, boolean switchGears, boolean override){
        if(switchGears){ //Checks to see if gears need to switch
            inHighState = !inHighState; //Switches States
        }

        this.updateGears(); //Updates the gears solenoid

	    mathLeft = yAxis - xAxis; //Calculates the math for the left side
        mathRight = yAxis + xAxis; //Calculates the math for the right side
    
        //Below checks to see if we are under the deadband
	    if (Math.abs(mathLeft) <= deadband && Math.abs(mathRight) <= deadband){ 
            //Below sets the motors to zero if under the deadband
            leftGroup.set(0); 
		    rightGroup.set(0); 
	    } else { //USed if above deadband
            if(inHighState){ //Checks if it is in high state
                if(override){ //Checks if it is overriden
                    //Below sets the math speeds
                    mathLeft = mathLeft*overrideSpeed; 
                    mathRight = mathRight*overrideSpeed;
                } else { //USed if in high and not overriden
                    //Below sets the math speeds
                    mathLeft = mathLeft*highSpeed;
                    mathRight = mathRight*highSpeed;
                }
	        } else { //Used if in low speed
                if(override){ //Checks to see if its overriden
                    //Belows sets the math speeds
                    mathLeft = mathLeft*overrideSpeed; 
                    mathRight = mathRight*overrideSpeed;
                } else { //Used if its in low and not overriden
                    //Below sets the math speeds
                    mathLeft = mathLeft*lowSpeed;
                    mathRight = mathRight*lowSpeed;
                }
            }
        }
        
        if(rightReverse){ //Checks the right reverse
            mathRight = -mathRight; //Switches the value
        }

        if(allReverse){ //Chcks the all reverse
            //Below switches the value
            mathLeft = -mathLeft; 
            mathRight = -mathRight;
        }

        //Below sets the motors
        leftGroup.set(this.mathLeft);
        rightGroup.set(this.mathRight);
    }

    public void update(){
        SmartDashboard.putNumber("Left Drive Base", this.mathLeft); //Sends the left drive to the dashbaord
        SmartDashboard.putNumber("Right Drive Base", this.mathRight); //Sends the right drive to the dashboard
        SmartDashboard.putBoolean("Is High Gear", this.inHighState); //Shows if its in high gear to the dashboard
    }
}