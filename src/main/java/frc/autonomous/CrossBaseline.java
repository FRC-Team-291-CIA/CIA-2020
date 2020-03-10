package frc.autonomous;

import edu.wpi.first.wpilibj.Timer;

public class CrossBaseline extends AutoMode {

    private Timer gameTimer = new Timer();

    public CrossBaseline(){

    }

    private enum crossBaselineState{
        INIT,
        CROSS
    }

    private crossBaselineState state = crossBaselineState.INIT;

    @Override
    public void autoInit() {

    }

    @Override
    public void autoRun() {
        switch(state){
            case INIT:
                autoInit();
                state = crossBaselineState.CROSS;
                break;
            case CROSS: 
                if(gameTimer.get() < 4.0){
                    driveBase.setDrivetrainVoltage(.05, .05);
                }else{                    
                    driveBase.setDrivetrainVoltage(0, 0);
                }

        }
    }

}
