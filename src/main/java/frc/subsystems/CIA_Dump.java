package frc.subsystems;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class CIA_Dump {
    private Solenoid piston; //Creates the object names
    private boolean isReversed; //Used if it needs reversed
    private String currentState = "Dump_State_Not_Set_Yet"; //Used for the smartdashboard
    private dumpState lastState;
    //Below is the states that the dump can be in
    public static enum dumpState {
        OPEN, //Used to lower the gate and let balls out
        CLOSED, //Used to raise the gate and keep balls in
        CURRENT_STATE //Used to keep it in the current state
    }
    /*
    Below is a constructor that takes in the following:
    First Solenoid Port, Second Solenoid Port, If it is reversed
    These values come from the Robot.java class
    */
    public CIA_Dump(int newPistonPortZero, boolean newIsReversed){
        //Below creates the solenoids
        piston = new Solenoid(newPistonPortZero);

        isReversed = newIsReversed; //Takes in if it is reversed

        lastState = dumpState.CLOSED;
    }  
    //Below is used to set the solenoids into position
    private void setPiston(boolean pistonValue){
        if(isReversed){ //Checks to see if its reversed
            //Below sets the solenoids to the value they need to be at
            piston.set(!pistonValue); 
        }else{ //Used if its not reversed
            //Below sets the solenoids to the value they need to be at
            piston.set(pistonValue);
        }
    }
    //Below is used to the set the state of the dump
    public void setDumpState(dumpState wantedState){
        switch(wantedState){ //Checks to see which state is the wanted state
            case OPEN: //Used if its wanted to be open
                lastState = dumpState.OPEN;
                currentState = "OPEN"; //Sets the data used in smartdashboard
                this.setPiston(true); //Uses a method to set the piston state
                break;
            case CLOSED: //Used if its wanted to be closed
                lastState = dumpState.CLOSED;
                currentState = "CLOSED"; //Sets the data used in smartdashboard
                this.setPiston(false); //Uses a method to set the piston state
                break;
            case CURRENT_STATE: //Used to keep its current state
                if (dumpState.CLOSED == this.lastState){ //Checks to see if the last state was closed
                    this.setDumpState(dumpState.CLOSED); //Sets it to closed
                } else { //Used if the last state was not closed
                    this.setDumpState(dumpState.OPEN); //Sets it to open
                }
                break;
        }
    }

    public void update(){
        SmartDashboard.putString("Dump Current State:", currentState); //Sends data to the smartdashboard
    }
}