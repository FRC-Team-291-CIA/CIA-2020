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
        RAISE_UP, //Used to raise the climber
        HOLD_UP, //Used to hold the climber up
        CLIMB, //Used to lower the climber and climb
        STORE, //Used to store the climber
        WINCH_REVERSE, //Used to reverse the winch in the pits
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
        
        this.setInvertedMotors(newIsRightReversed, newIsAllReversed);

        motorSpeed = newMotorSpeed;

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
            case RAISE_UP: //Used if its the climber is up
                lastState = climbState.RAISE_UP;
                hasGoneUp = true;
                currentState = "UP"; //Sets the data that goes to smartdashboard
                winch.set(0.00);
                piston.set(Value.kForward);
                break; 
            case CLIMB: //Used if its bringing the climber down
                lastState = climbState.CLIMB;

                if (hasGoneUp){

                    currentState = "CLIMB"; //Sets the data that goes to smartdashboard
                    winch.set(this.motorSpeed);
                    piston.set(Value.kReverse);

                } else {

                    currentState = "YOU NEED TO GO UP FIRST!"; //Sets the data that goes to smartdashboard
                    winch.set(0.00);
                    piston.set(Value.kForward);

                }
                break;
            case STORE: //Used to store the climber
                lastState = climbState.STORE;
                hasGoneUp = false;
                currentState = "STORE"; //Sets the data that goes to smartdashboard
                winch.set(0.00);
                piston.set(Value.kReverse);
                break;
            case HOLD_UP:
                lastState = climbState.HOLD_UP;
                currentState = "HOLD UP";
                hasGoneUp = true;
                winch.set(0.00);
                piston.set(Value.kReverse);
                break;
            case WINCH_REVERSE:
                lastState = climbState.STORE;
                currentState = "WINCH_REVERSE";
                hasGoneUp = false;
                winch.set(0.25);
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
        SmartDashboard.putString("Climber Current State", currentState);
    }
}