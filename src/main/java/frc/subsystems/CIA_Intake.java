package frc.subsystems;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

//TODO - Add Comments
public class CIA_Intake {
    private Spark motorZero, motorOne;
    private double intakePower;
    private boolean isReversed;
    private String currentState = "State_Not_Set_Yet";

    public CIA_Intake(int newMotorPortZero, int newMotorPortOne, double newIntakePower, boolean newIsReversed){
       motorZero = new Spark(newMotorPortZero);
       motorOne = new Spark(newMotorPortOne);

       intakePower = newIntakePower;

       isReversed = newIsReversed;
    }

    public static enum intakeState {
        INTAKING,
        OUTTAKING,
        STOP
    }

    public void setMotors(double power){
        if(isReversed){
            motorZero.set(-power);
            motorOne.set(power);
        }else{
            motorZero.set(power);
            motorOne.set(-power);
        }
    }

    public void setIntakeState(intakeState wantedState){
        switch(wantedState){
            case INTAKING:
                currentState = "Intaking";
                this.setMotors(intakePower);
                break;
            case OUTTAKING:
                currentState = "Outtaking";
                this.setMotors(-intakePower);
                break;
            case STOP:
                currentState = "Stopped";
                this.setMotors(0.00);
                break;
        }
    }

    public void update(){
        SmartDashboard.putString("Current State:", currentState);
    }
}