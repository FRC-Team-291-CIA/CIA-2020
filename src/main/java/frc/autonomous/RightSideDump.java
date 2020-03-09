package frc.autonomous;

public class RightSideDump extends AutoMode{

    public RightSideDump(){

    } 

    public static enum rightSideDumpState{
        START_TO_POWERPORT,
        DUMP,
        EXECUTE
    }

    @Override
    public void autoInit() {
    
    }

    @Override
    public void autoRun() {
        setAutoState(rightSideDumpState.EXECUTE);
    }

    public void setAutoState(rightSideDumpState wantedState){
        switch(wantedState){
            case EXECUTE:
            case START_TO_POWERPORT:
                setTrajectory("paths/output/Right_DumpBalls.wpilib.json");
            case DUMP:
                break;

        }
    }
    
}