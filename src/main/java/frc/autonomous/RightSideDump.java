package frc.autonomous;

import frc.subsystems.CIA_Dump.dumpState;

public class RightSideDump extends AutoMode{
    
    private String trajectoryDir = "output/Right_DumpBalls.wpilib.json";


    public RightSideDump(){
        
    } 

    private enum rightSideDumpState{
        START_TO_POWERPORT,
        DUMP
    }

    private rightSideDumpState state = rightSideDumpState.START_TO_POWERPORT;

    @Override
    public void autoInit() {
        trajectoryFollower.setTrajectory(trajectoryDir);
        trajectoryFollower.initTrajectory();
    }

    @Override
    public void autoRun() {
        switch(state){
            case START_TO_POWERPORT:
                
                trajectoryFollower.followTrajectory();
                state = rightSideDumpState.DUMP;
                break;
            case DUMP:
                dump.setDumpState(dumpState.OPEN);
                break;
        }
    }
    
}