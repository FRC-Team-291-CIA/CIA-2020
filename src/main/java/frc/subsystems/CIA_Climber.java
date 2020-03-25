/* 
File Name: CIA_Climber.java
Use: This is used the controller for a piston and hook climber.
Reuse: This file is not intended to be reused.
Reuse Tips: N/A
Files Directly Used / Is In: Robot.java and Subsystem_Variables.java
Sensors Used: A USB camera is used
*/

package frc.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

import frc.subsystems.Subsystem_Variables;

public class CIA_Climber {
    private Spark motorLeft, motorRight;
    private SpeedControllerGroup winch;
    private DoubleSolenoid piston;
    private String currentState = "CLIMBER_STATE_NOT_SET";
    private double motorSpeed;
    private boolean hasGoneUp = false; //Used to tell if the climber has gone up
    private climbState lastState = climbState.STORE;
    
    //Below is the states that the climber can be in
    public static enum climbState {
        RAISE_UP, //Used to raise the climber
        HOLD_UP, //Used to hold the climber up
        CLIMB, //Used to lower the climber and climb
        STORE, //Used to store the climber
        WINCH_ONLY, //Used to reverse the winch in the pits
        CURRENT_STATE //Used to keep it in the current state
    }
    
    /*
    Below is a constructor that takes in the following in order:
    The first motor, the second motor, the first and second double solenoid port, the motor speed,
    right reversed boolean, all reversed all
    These values come from the robot.java class
    */
    public CIA_Climber(int newMotorLeftPort, int newMotorRightPort, int newPistonZeroPort, int newPistonOnePort, 
                       double newMotorSpeed, boolean newIsRightReversed, boolean newIsAllReversed){ 

        //Below creates the motors                   
        motorLeft = new Spark(newMotorLeftPort);
        motorRight = new Spark(newMotorRightPort);
        
        this.setInvertedMotors(newIsRightReversed, newIsAllReversed); //Sets the inverted motors

        motorSpeed = newMotorSpeed; //Takes in the winch speed

        winch = new SpeedControllerGroup(motorLeft, motorRight); //Creates a speed controllor group

        piston = new DoubleSolenoid(newPistonZeroPort, newPistonOnePort); //Creates a double solenoid
    }

    //The method below checks to see which motors need reversed
    private void setInvertedMotors(boolean isRightReversed, boolean isAllReversed){
        if (isRightReversed){ //Checks if the right motor needs revered
            motorRight.setInverted(true); //Inverts the right group if true
        }

        if (isAllReversed){ //Checks if the entire climber is inverted
            motorLeft.setInverted(true); //Invertes the left group if true

            if (motorRight.getInverted()){ //Checks if the right group is inverted if true
                motorRight.setInverted(false); //Invertes the right group, negating the original invert, if true
            } else {
                motorRight.setInverted(true); //Sets the invert for right side to true
            }
        }
    }
    
    //Below is used to take in the wanted state and set the climber to it
    public void setClimbState(climbState wantedState){
        switch(wantedState){ //Checks to see which state it wants to use
            case RAISE_UP: //Used if its the climber is up
                lastState = climbState.RAISE_UP;
                Subsystem_Variables.isOnlyLowGear = true;
                hasGoneUp = true;
                currentState = "UP"; //Sets the data that goes to smartdashboard
                winch.set(0.00);
                piston.set(Value.kForward);
                break; 
            case CLIMB: //Used if its bringing the climber down while attached
                lastState = climbState.CLIMB;

                if (hasGoneUp){
                    currentState = "CLIMB"; //Sets the data that goes to smartdashboard
                    piston.set(Value.kReverse);
                        winch.set(this.motorSpeed);
                    Subsystem_Variables.isOnlyLowGear = true;

                } else {

                    currentState = "YOU NEED TO GO UP FIRST!"; //Sets the data that goes to smartdashboard
                    winch.set(0.00);
                    piston.set(Value.kReverse);
                    Subsystem_Variables.isOnlyLowGear = false;

                }
                break;
            case STORE: //Used to store the climber
                lastState = climbState.STORE;
                hasGoneUp = false;
                Subsystem_Variables.isOnlyLowGear = false;
                currentState = "STORE"; //Sets the data that goes to smartdashboard
                winch.set(0.00);
                piston.set(Value.kReverse);
                break;
            case HOLD_UP:
                lastState = climbState.HOLD_UP;
                currentState = "HOLD UP";
                Subsystem_Variables.isOnlyLowGear = true;
                hasGoneUp = true;
                winch.set(0.00);
                piston.set(Value.kReverse);
                break;
            case WINCH_ONLY:
                lastState = climbState.STORE;
                currentState = "WINCH_REVERSE";
                Subsystem_Variables.isOnlyLowGear = false;
                hasGoneUp = false;
                winch.set(-0.25);
                piston.set(Value.kReverse);
                break;        
            case CURRENT_STATE: //Used to keep it in its current state
                if (lastState == climbState.STORE){

                    this.setClimbState(climbState.STORE);

                } else if (lastState == climbState.RAISE_UP){

                    this.setClimbState(climbState.RAISE_UP);

                } else {

                    this.setClimbState(climbState.HOLD_UP);

                }
                break;
        }
    }

    public void update(){
        SmartDashboard.putString("Climber Current State", currentState); //Used to display the climber state
    }
}