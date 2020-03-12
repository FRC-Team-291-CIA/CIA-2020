package frc.autonomous;

import edu.wpi.first.wpilibj.Timer;

public class CrossBaseline extends AutoMode {

    private Timer gameTimer = new Timer();

    public CrossBaseline(){

    }

    private enum crossBaselineState{
        CROSS
    }

    private crossBaselineState state = crossBaselineState.CROSS;

    @Override
    public void autoInit() {
        gameTimer.start();
    }

    @Override
    public void autoRun() {
        switch(state){
            case CROSS: 
                if(gameTimer.get() < 2.0){
                    driveBase.setDrivetrainVoltage(.3, .3);
                }else{                    
                    driveBase.setDrivetrainVoltage(0, 0);
                }
                break;

        }
    }

}
