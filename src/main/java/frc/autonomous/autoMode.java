package frc.autonomous;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
//import edu.wpi.first.wpilibj2.command.RamseteCommand;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryUtil;
import edu.wpi.first.wpilibj.trajectory.Trajectory.State;

import frc.robot.Subsystems;
import frc.utilities.CIA_PID;

import java.io.IOException;
import java.nio.file.Path;

public abstract class AutoMode implements Subsystems {

    private static RamseteController ramseteController = new RamseteController();
    private static Trajectory trajectory;

    private DifferentialDriveKinematics autoKinematics = new DifferentialDriveKinematics(0.5715);
    private CIA_PID leftDrivePID = new CIA_PID(1.5, 0.0, 4.0, 2.0);
    private CIA_PID rightDrivePID = new CIA_PID(1.5, 0.0, 4.0, 2.0);


    public void setTrajectory(String trajectoryJSON) {
        try {
            Path trajectoryPath = Filesystem.getDeployDirectory().toPath().resolve(trajectoryJSON);
            trajectory = TrajectoryUtil.fromPathweaverJson(trajectoryPath);
        } catch (IOException ex) {
            DriverStation.reportError("Unable to open trajectory: " + trajectoryJSON, ex.getStackTrace());
        }
    }

    public void followTrajectory(){
        
        for(State currState: trajectory.getStates()){
            DifferentialDriveWheelSpeeds driveWheelSpeeds = autoKinematics.toWheelSpeeds(ramseteController.calculate(currState.poseMeters, currState));
            leftDrivePID.setDesiredValue(driveWheelSpeeds.leftMetersPerSecond);
            rightDrivePID.setDesiredValue(driveWheelSpeeds.rightMetersPerSecond);
            driveBase.setDrivetrainVoltage(leftDrivePID.calcPID(driveBase.getLeftEncoderVel()), rightDrivePID.calcPID(driveBase.getRightEncoderVel()));

        }

    }
        
    public abstract void autoInit();

    public abstract void autoRun();

}
