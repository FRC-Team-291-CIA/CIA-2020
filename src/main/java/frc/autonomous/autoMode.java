package frc.autonomous;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryUtil;

import frc.robot.Subsystems;
import java.io.IOException;
import java.nio.file.Path;

public abstract class AutoMode implements Subsystems {

    public static RamseteController ramseteController = new RamseteController();
    public static Trajectory trajectory;
    
    
    public void setTrajectory(String trajectoryJSON){
        try {
            Path trajectoryPath = Filesystem.getDeployDirectory().toPath().resolve(trajectoryJSON);
            trajectory = TrajectoryUtil.fromPathweaverJson(trajectoryPath);
        } catch (IOException ex) {
            DriverStation.reportError("Unable to open trajectory: " + trajectoryJSON, ex.getStackTrace());
        }
        
    }
    
    public abstract void autoInit();

    public abstract void autoRun();

}
