package frc.autonomous;

public class CrossBaseline extends AutoMode {

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
                driveBase.setDrivetrainVoltage(.05, .05);

        }
    }

}
