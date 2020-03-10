package frc.autonomous;

import frc.subsystems.CIA_Dump.dumpState;

public class RightSideDump extends AutoMode{
    
    private String trajectoryDir = "src/main/java/frc/autonomous/paths/output/Right_DumpBalls.wpilib.json";

    public RightSideDump(){

    } 

    private enum rightSideDumpState{
        INIT,
        START_TO_POWERPORT,
        DUMP
    }

    private rightSideDumpState state = rightSideDumpState.INIT;

    @Override
    public void autoInit() {
        setTrajectory(trajectoryDir);
    }

    @Override
    public void autoRun() {
        switch(state){
            case INIT:
                autoInit();
                state = rightSideDumpState.START_TO_POWERPORT;
                break;
            case START_TO_POWERPORT:
                followTrajectory();
                state = rightSideDumpState.DUMP;
                break;
            case DUMP:
                dump.setDumpState(dumpState.OPEN);
                break;
        }
    }
    
}