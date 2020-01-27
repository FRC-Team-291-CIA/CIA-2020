package frc.subsystems;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

//TODO - Add Comments
public class CIA_Dump {
    private Solenoid pistonZero, pistonOne;
    private boolean isReversed;
    private String currentState = "State_Not_Set_Yet";

    public CIA_Dump(int newPistonPortZero, int newPistonPortOne, boolean newIsReversed){
        pistonZero = new Solenoid(newPistonPortZero);
        pistonOne = new Solenoid(newPistonPortOne);

        isReversed = newIsReversed;
    }  

    public static enum dumpState {
        OPEN,
        CLOSED
    }

    public void setPiston(boolean pistonValue){
        if(isReversed){
            pistonZero.set(!pistonValue);
            pistonOne.set(!pistonValue);
        }else{
            pistonZero.set(pistonValue);
            pistonOne.set(pistonValue);
        }
    }

    public void setDumpState(dumpState wantedState){
        switch(wantedState){
            case OPEN:
                currentState = "OPEN";
                this.setPiston(true);
                break;
            case CLOSED:
                currentState = "CLOSED";
                this.setPiston(false);
                break;
        }
    }

    public void update(){
        SmartDashboard.putString("Current State:", currentState);
    }
}