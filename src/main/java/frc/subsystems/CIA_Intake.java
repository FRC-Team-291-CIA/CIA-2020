/* 
File Name: CIA_Intake.java
Use: This is used as a intake controller.
Reuse: This file can be reused from year to year but will needed edited for which sensors are used
       constants will need changed for that years robot.
Reuse Tips: If needed to add more motor, add more objects, then implement then in the code
Files Directly Used / Is In: Robot.java
Sensors Used: No sensors are used
*/

package frc.subsystems;

//The below import is used for the smartdashboard
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

//The below import is used to control motors
import frc.sensors.CIA_SparkMax;

public class CIA_Intake {
    private CIA_SparkMax motor; //Creates the spark object name
    private double intakePower, outtakePower; //Used to set the powers to the motors
    private boolean isReversed; //Used to reverse the motors if needed
    private String currentState = "Intake_State_Not_Set_Yet"; //Used for displaying the current state to dashboard

    /*
    Below is a constructor that takes in the following in order: 
    The motors, The Intake Power of Motors, The outtake power of motors, If it is reversed
    These values come from the robot.java class which grab values from RobotMap.java and Constants.java
    */
    public CIA_Intake(int newMotorPort, double newIntakePower, double newOuttakePower, boolean newIsReversed){
        //Below creates the spark
        motor = new CIA_SparkMax(newMotorPort); //Creates the sparks
      
        intakePower = newIntakePower; //Takes in the power variable
        outtakePower = newOuttakePower; //Takes in the outtake speed
      
        isReversed = newIsReversed; //Takes in if its reversed
    }

    //Below is the states that the intake can be in
    public static enum intakeState {
        INTAKING, //Used to intake balls
        OUTTAKING, //Used to reverse (Only used if clogged or has issues)
        STOP //Used to keep it stationary
    }

    //Below is used to set the motor powers
    private void setMotors(double power){
        if(isReversed){ //Checks to see if it is reversed
            //Below sets the power to the motors
            motor.set(power); 
        }else{ //Used if it is not reversed
            //Below sets the power to the motors
            motor.set(power); 
        }
    }

    //Below is used to take in the wanted state and set the intake to it
    public void setIntakeState(intakeState wantedState){
        switch(wantedState){ //Checks to see which state it wants to use
            case INTAKING: //Used if its intaking
                currentState = "Intaking"; //Sets the data that goes to smartdashboard
                this.setMotors(intakePower); //Uses a method to set the motor power
                break; 
            case OUTTAKING: //Used if its outtaking
                currentState = "Outtaking"; //Sets the data that goes to smartdashboard
                this.setMotors(outtakePower); //Uses a method to set the motor power
                break;
            case STOP: //Used if its stopped
                currentState = "Stopped"; //Sets the data that goes to smartdashboard
                this.setMotors(0.00); //Uses a method to stop the motors
                break;
        }
    }

    //Below is used to update the smartdashboard
    public void update(){
        SmartDashboard.putString("Intake Current State:", currentState); //Displays the current state
    }
}