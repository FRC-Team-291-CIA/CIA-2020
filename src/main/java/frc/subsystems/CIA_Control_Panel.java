package frc.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DriverStation;

import frc.sensors.CIA_SparkMax;
import frc.sensors.CIA_ColorSensor;

public class CIA_Control_Panel {
    String gameData;
    String currentState = "CONTROL_STATE_NOT_SET";
    CIA_ColorSensor sensor;
    CIA_SparkMax motor;
    boolean spinNotComplete = true;
    boolean isOnGreen = false;
    boolean alreadyOnGreen = false;
    double motorSpeed;
    double counter = 0;
    boolean error = false;

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
    public CIA_Control_Panel(int motorPort, double newMotorSpeed){
        sensor = new CIA_ColorSensor(); 
        motor = new CIA_SparkMax(motorPort);
        motorSpeed = newMotorSpeed;
    }
    
    //Below is used to take in the wanted state and set the climber to it
    public void setControlState(controlPanelState wantedState){
        switch(wantedState){ //Checks to see which state it wants to use
            case GO_TO_COLOR:
                currentState = "GO_TO_COLOR";
                gameData = DriverStation.getInstance().getGameSpecificMessage();
                if(gameData.length() > 0) {
                    switch (gameData.charAt(0)){
                        case 'B':
                            //Blue case code
                            break;
                        case 'G':
                            //Green case code
                            break;
                        case 'R':
                            //Red case code
                            break;
                        case 'Y':
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
                currentState = "SPIN"; //Shows the current state
                spinNotComplete = true;
                //motor.set(0.4); // Temporary
                
                while(spinNotComplete){ //Checks if the spin is done
                    error = false;
                    alreadyOnGreen = false;

                    //Below checks to see if the sensor is detecting the color green
                    if(sensor.getColor().equals("Green")){
                        isOnGreen = true;
                    } else if (sensor.getColor() == null) {
                        error = true;
                    } else {
                        isOnGreen = false;
                    }
                    
                    if(!error){
                        //Below is an if statement that checks to see where the color sensor is
                        if (isOnGreen && !alreadyOnGreen){
                            motor.set(motorSpeed);
                            counter++;
                            alreadyOnGreen = true;
                        } else if (isOnGreen && alreadyOnGreen){
                            motor.set(motorSpeed);
                        } else if(counter >= 8){
                            motor.set(0.00);
                            spinNotComplete = true;
                        } else {
                            motor.set(motorSpeed);
                            alreadyOnGreen = false;
                        }
                    } else {
                       System.out.println("ERROR WITH CONTROL PANEL!!!");
                    }   
                }                
                break;
            case STOP:
                currentState = "STOP";
                spinNotComplete = false;
                error = false;
                motor.set(0.0);
                break;
        }
    }

    public void update(){
        sensor.update();
        SmartDashboard.putString("Control Panel State: ", currentState);
    }
}