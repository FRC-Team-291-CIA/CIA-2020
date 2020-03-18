package frc.autonomous;

import frc.robot.Subsystems;
//deprecated framework of autoFollower because it sucked and didnt work - likhith 3/11/2020
@Deprecated
public class AutoFollower extends Subsystems{
    private static RamseteController ramseteController = new RamseteController();
    private static Trajectory trajectory;

    private DifferentialDriveKinematics autoKinematics = new DifferentialDriveKinematics(0.5715);
    private CIA_PID leftDrivePID = new CIA_PID(.1, 0.0, 1.0, 1.0);
    private CIA_PID rightDrivePID = new CIA_PID(.1, 0.0, 1.0, 1.0);

    private double metersToInches = 39.37;//39.37
    private double inchesPerSecondToMotorPower = 0.3 / 54.0;

    private int state = 0;
    private SimpleMotorFeedforward driveFeedforward = new SimpleMotorFeedforward(Constants.kS, Constants.kV, Constants.kA);


    public void setTrajectory(String trajectoryJSON) {
        try {
            Path trajectoryPath = Filesystem.getDeployDirectory().toPath().resolve(trajectoryJSON);
            trajectory = TrajectoryUtil.fromPathweaverJson(trajectoryPath);
        } catch (IOException ex) {
            DriverStation.reportError("Unable to open trajectory: " + trajectoryJSON, ex.getStackTrace());
        }
    }

    public void followTrajectory(){
        
        if(state < trajectory.getStates().size()-1){

                State currState = trajectory.getStates().get(state);
                State nextState = trajectory.getStates().get(state+1);
                DifferentialDriveWheelSpeeds driveWheelSpeeds = autoKinematics.toWheelSpeeds(ramseteController.calculate(currState.poseMeters, nextState));
                double leftSpeed = (driveWheelSpeeds.leftMetersPerSecond)*(metersToInches)*(inchesPerSecondToMotorPower);
                double rightSpeed = (driveWheelSpeeds.rightMetersPerSecond)*(metersToInches)*(inchesPerSecondToMotorPower);
                //leftDrivePID.setDesiredValue(driveWheelSpeeds.leftMetersPerSecond*39.37);
                //rightDrivePID.setDesiredValue(driveWheelSpeeds.rightMetersPerSecond*39.37);
                driveBase.setDrivetrainVoltage(leftSpeed, rightSpeed);
                state += 1;

        } else {
            driveBase.setDrivetrainVoltage(0, 0);
        }
    }
}