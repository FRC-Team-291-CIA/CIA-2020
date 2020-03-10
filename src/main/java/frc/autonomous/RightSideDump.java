package frc.autonomous;

public class RightSideDump extends AutoMode{

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
    
    }

    @Override
    public void autoRun() {
        switch(state){
            case INIT:
            break;
            case START_TO_POWERPORT:
            break;
            case DUMP:
            break;
        }
    }
    
}