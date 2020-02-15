package frc.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import frc.sensors.CIA_ColorSensor;

//TODO - Create the control panel class
public class CIA_Control_Panel {

    //Below is the states that the control panel can be in
    public static enum controlPanelState {
       
    }
    
    /*
    Below is a constructor that takes in the following in order:

    These values come from the robot.java class
    */
    public CIA_Control_Panel(){ 
    }
    
    //Below is used to take in the wanted state and set the climber to it
    public void setClimbState(controlPanelState wantedState){
        switch(wantedState){ //Checks to see which state it wants to use
        
        }
    }

    public void update(){

    }
}