package frc.subsystems;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
//import edu.wpi.first.wpilibj.Solenoid;
//import edu.wpi.first.wpilibj.Encoder;

//TODO - Add Encoders and Solenoids to DriveBase
public class CIA_DriveBase {
    private Spark motorZero, motorOne;
    private SpeedControllerGroup leftGroup, rightGroup;
    //private Solenoid shifter;
    private double lowSpeed, highSpeed, deadband, mathLeft, mathRight, overrideSpeed;
    private boolean inHighState = false;
    private boolean rightReverse, allReverse;
    //private Encoder leftEncoder, rightEncoder;

    /*
    Below is a contructor that takes in the follwoing in order:
    Left Motor Port, Right Motor Port, Shifter Solenoid Port, The Deadband,
    The Low Speed, The High, The Override Speed, Right Side Reverse, All Sides Revesered
    */
    public CIA_DriveBase(int leftMotorsPort, int rightMotorsPort, int shifterSolenoidPort, double newDeadband, 
                        double newLowSpeed, double newHighSpeed, double newOverrideSpeed, boolean newRightReverse,
                        boolean newAllReverse){
        motorZero = new Spark(leftMotorsPort);
        motorOne = new Spark(rightMotorsPort);

        leftGroup = new SpeedControllerGroup(motorZero);
        rightGroup = new SpeedControllerGroup(motorOne);

        //shifter = new Solenoid(shifterSolenoidPort);

        deadband = newDeadband;

        lowSpeed = newLowSpeed;
        highSpeed = newHighSpeed;

        overrideSpeed = newOverrideSpeed;
    }

    private void updateGears(){
        if(inHighState){
            //shifter.set(true);
        } else {
            //shifter.set(false);
        }
    }

    public void arcadeDrive(double yAxis, double xAxis, boolean switchGears, boolean override){
        if(switchGears){
            inHighState = !inHighState;
        }

        this.updateGears();

	    mathLeft = yAxis - xAxis;
        mathRight = yAxis + xAxis;
    

	    if (Math.abs(mathLeft) <= deadband && Math.abs(mathRight) <= deadband){
		    leftGroup.set(0); 
		    rightGroup.set(0); 
	    } else {
            if(inHighState){
                if(override){
                    mathLeft = mathLeft*overrideSpeed; 
                    mathRight = mathRight*overrideSpeed;
                } else {
                    mathLeft = mathLeft*highSpeed;
                    mathRight = mathRight*highSpeed;
                }
	        } else {
                if(override){
                    mathLeft = mathLeft*overrideSpeed; 
                    mathRight = mathRight*overrideSpeed;
                } else {
                    mathLeft = mathLeft*lowSpeed;
                    mathRight = mathRight*lowSpeed;
                }
            }
        }
        
        if(rightReverse){
            mathRight = -mathRight;
        }

        if(allReverse){
            mathLeft = -mathLeft;
            mathRight = -mathRight;
        }
    }

    public void update(){
        SmartDashboard.putNumber("Left Drive Base", this.mathLeft);
        SmartDashboard.putNumber("Right Drive Base", this.mathRight);
        SmartDashboard.putBoolean("Is High Gear", this.inHighState);
    }
}