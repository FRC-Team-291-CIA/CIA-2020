package frc.subsystems;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class CIA_Dump {
    private Solenoid pistonZero, pistonOne; //Creates the object names
    private boolean isReversed; //Used if it needs reversed
    private String currentState = "State_Not_Set_Yet"; //Used for the smartdashboard

    /*
    Below is a constructor that takes in the following:
    First Solenoid Port, Second Solenoid Port, If it is reversed
    These values come from the Robot.java class
    */
    public CIA_Dump(int newPistonPortZero, int newPistonPortOne, boolean newIsReversed){
        //Below creates the solenoids
        pistonZero = new Solenoid(newPistonPortZero);
        pistonOne = new Solenoid(newPistonPortOne);

        isReversed = newIsReversed; //Takes in if it is reversed
    }  

    //Below is the states that the dump can be in
    public static enum dumpState {
        OPEN, //Used to lower the gate and let balls out
        CLOSED //Used to raise the gate and keep balls in
    }

    //Below is used to set the solenoids into position
    private void setPiston(boolean pistonValue){
        if(isReversed){ //Checks to see if its reversed
            //Below sets the solenoids to the value they need to be at
            pistonZero.set(!pistonValue); 
            pistonOne.set(!pistonValue);
        }else{ //Used if its not reversed
            //Below sets the solenoids to the value they need to be at
            pistonZero.set(pistonValue);
            pistonOne.set(pistonValue);
        }
    }

    //Below is used to the set the state of the dump
    public void setDumpState(dumpState wantedState){
        switch(wantedState){ //Checks to see which state is the wanted state
            case OPEN: //Used if its wanted to be open
                currentState = "OPEN"; //Sets the data used in smartdashboard
                this.setPiston(true); //Uses a method to set the piston state
                break;
            case CLOSED: //Used if its wanted to be closed
                currentState = "CLOSED"; //Sets the data used in smartdashboard
                this.setPiston(false); //Uses a method to set the piston state
                break;
        }
    }

    public void update(){
        SmartDashboard.putString("Current State:", currentState); //Sends data to the smartdashboard
    }
}