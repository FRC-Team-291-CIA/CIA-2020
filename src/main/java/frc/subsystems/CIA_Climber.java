package frc.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

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
        UP, //state in which the climber is extended up but the winch is not activated
        CLIMB, //state in which the climber is up and the winch is activated
        STORE, //state in which the climber is not active
        CURRENT_STATE //used to keep the climber in its current state
    }  
    /*
    Below is a constructor that takes in the following in order:
    The first motor, the second motor, the first and second double solenoid port, the motor speed,
    right reversed boolean, all reversed all
    These values come from the robot.java class
    */
    public CIA_Climber(int newMotorLeftPort, int newMotorRightPort, int newPistonZeroPort, int newPistonOnePort, 
                       double newMotorSpeed, boolean newIsRightReversed, boolean newIsAllReversed){ 
        //Below initializes the motors                 
        motorLeft = new Spark(newMotorLeftPort);
        motorRight = new Spark(newMotorRightPort); 
        this.setInvertedMotors(newIsRightReversed, newIsAllReversed);
        motorSpeed = newMotorSpeed;
        //Initializing a speed controller group and the solenoid used for the elevator actuation
        winch = new SpeedControllerGroup(motorLeft, motorRight);
        piston = new DoubleSolenoid(newPistonZeroPort, newPistonOnePort);
    }

    //The method below checks to see which motors need reversed
    private void setInvertedMotors(boolean isRightReversed, boolean isAllReversed){
        if (isRightReversed){
            motorRight.setInverted(true);
        }
        if (isAllReversed){
            motorLeft.setInverted(true);
            if (motorRight.getInverted()){
                motorRight.setInverted(false);
            } else {
                motorRight.setInverted(true);
            }
        }
    }   
    //Below is used to take in the wanted state and set the climber to it
    public void setClimbState(climbState wantedState){
        switch(wantedState){ //Checks to see which state it wants to use
            case UP: //Used if its the climber is up
                lastState = climbState.UP;
                hasGoneUp = true;
                currentState = "UP"; //Sets the data that goes to smartdashboard
                winch.set(0.00);
                piston.set(Value.kForward);
                break; 
            case CLIMB: //Used if its bringing the climber down
                lastState = climbState.CLIMB;
                currentState = hasGoneUp ? "CLIMB" : "YOU NEED TO GO UP FIRST!"; //Sets the data that goes to smartdashboard
                winch.set(hasGoneUp ? this.motorSpeed : 0.00);//using a ternary operator to determine if the winch motor moves
                piston.set(hasGoneUp ? Value.kReverse : Value.kForward);//using another ternary to determine the piston position
                break;
            case STORE: //Used to store the climber
                lastState = climbState.STORE;
                hasGoneUp = false;
                currentState = "STORE"; //Sets the data that goes to smartdashboard
                winch.set(0.00);
                piston.set(Value.kForward);
                break;    
            case CURRENT_STATE: //Used to keep it in its current state
                if (lastState == climbState.STORE){
                    this.setClimbState(climbState.STORE);
                } else if (lastState == climbState.UP){
                    this.setClimbState(climbState.UP);
                } else {
                    this.setClimbState(climbState.CLIMB);
                }
                break;
        }
    }
    
    public void update(){
        SmartDashboard.putString("Climber Current State", currentState);
    }
}