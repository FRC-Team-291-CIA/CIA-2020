package frc.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DriverStation;

import frc.sensors.CIA_SparkMax;

import frc.sensors.CIA_ColorSensor;

//TODO - Create the control panel class
public class CIA_Control_Panel {
    String gameData;
    CIA_ColorSensor sensor;
    CIA_SparkMax motor;

    //Below is the states that the control panel can be in
    public static enum controlPanelState {
       GO_TO_COLOR, //Will be used to go to go to a color
       SPIN, //Will be used to spin the control panel
       STOP //Will be used to keep it stopped and check for game data
    }
    
    /*
    Below is a constructor that takes in the following in order:
    The Motor Port
    These values come from the robot.java class
    */
    public CIA_Control_Panel(int motorPort){
        sensor = new CIA_ColorSensor(); 
        motor = new CIA_SparkMax(motorPort);
    }
    
    //Below is used to take in the wanted state and set the climber to it
    public void setControlState(controlPanelState wantedState){
        switch(wantedState){ //Checks to see which state it wants to use
            case GO_TO_COLOR:
                gameData = DriverStation.getInstance().getGameSpecificMessage();
                if(gameData.length() > 0) {
                    switch (gameData.charAt(0)){
                        case 'B' :
                            //Blue case code
                            break;
                        case 'G' :
                            //Green case code
                            break;
                        case 'R' :
                            //Red case code
                            break;
                        case 'Y' :
                            //Yellow case code
                            break;
                        default :
                            //This is corrupt data
                            break;
                        }
                } else {
                    //Code for no data received yet
                    break;
                }
            case SPIN:
                motor.set(0.5);
                break;
            case STOP:
                motor.set(0.0);
                break;
        }
    }

    public void update(){
        sensor.update();
    }
}