package frc.autonomous;

import frc.robot.Subsystems;

public abstract class AutoMode implements Subsystems {

    protected static TrajectoryFollower trajectoryFollower;
            
    public abstract void autoInit();

    public abstract void autoRun();

}
