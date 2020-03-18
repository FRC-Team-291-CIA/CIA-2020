package frc.autonomous;

/*
author @likhithb
an adaptation of the commandbased codebases "ramsetecommand" class but designed to be easily 
implemented into the timedRobot frc codebase
*/

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;

import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryUtil;

import frc.robot.Subsystems;
import frc.robot.Constants;

import java.io.IOException;
import java.nio.file.Path;
import java.util.function.BiConsumer;
import java.util.function.Supplier;



public class TrajectoryFollower implements Subsystems{

    private final Timer m_timer = new Timer();
    private final boolean m_usePID;
    private final BiConsumer<Double, Double> m_output;
    private DifferentialDriveWheelSpeeds m_prevSpeeds;
    private double m_prevTime;

    private final PIDController leftController = new PIDController(Constants.kP, 0, 0);
    private final PIDController rightController = new PIDController(Constants.kP, 0, 0);

    private final SimpleMotorFeedforward m_feedforward = new SimpleMotorFeedforward(Constants.kS, Constants.kV, Constants.kA);

    private final RamseteController m_follower = new RamseteController();

    private Trajectory m_trajectory;

    private final DifferentialDriveKinematics m_kinematics = new DifferentialDriveKinematics(Constants.trackWidth);

    private final Supplier<Pose2d> m_pose = driveBase::getPoseInches;

    private final Supplier<DifferentialDriveWheelSpeeds> m_speeds = driveBase::getWheelSpeeds;
    

    public TrajectoryFollower(String trajectoryRoot, Supplier<DifferentialDriveWheelSpeeds> wheelSpeeds, 
    BiConsumer<Double, Double> outputVolts) {
        
        
        m_trajectory = setTrajectory(trajectoryRoot);
        m_output = outputVolts;

        m_usePID = true;

    }

    public Trajectory setTrajectory(String trajectoryJSON) {
        try {
            Path trajectoryPath = Filesystem.getDeployDirectory().toPath().resolve(trajectoryJSON);
            m_trajectory = TrajectoryUtil.fromPathweaverJson(trajectoryPath);
            return m_trajectory;
        } catch (IOException ex) {
            DriverStation.reportError("Unable to open trajectory: " + trajectoryJSON, ex.getStackTrace());
            return null;
        }
    }

    public void initTrajectory() {
        m_prevTime = 0;
        var initialState = m_trajectory.sample(0);
        m_prevSpeeds = m_kinematics.toWheelSpeeds(
            new ChassisSpeeds(initialState.velocityMetersPerSecond,
                0,
                initialState.curvatureRadPerMeter
                    * initialState.velocityMetersPerSecond));
        m_timer.reset();
        m_timer.start();
        if (m_usePID) {
            leftController.reset();
            rightController.reset();
        }
    }

    public void followTrajectory() {
        double curTime = m_timer.get();
        double dt = curTime - m_prevTime;

        var targetWheelSpeeds = m_kinematics.toWheelSpeeds(m_follower.calculate(m_pose.get(), m_trajectory.sample(curTime)));

        var leftSpeedSetpoint = targetWheelSpeeds.leftMetersPerSecond;
        var rightSpeedSetpoint = targetWheelSpeeds.rightMetersPerSecond;

        double leftOutput;
        double rightOutput;

        if (m_usePID) {
            double leftFeedforward =
                m_feedforward.calculate(leftSpeedSetpoint,
                    (leftSpeedSetpoint - m_prevSpeeds.leftMetersPerSecond) / dt);

            double rightFeedforward =
                m_feedforward.calculate(rightSpeedSetpoint,
                    (rightSpeedSetpoint - m_prevSpeeds.rightMetersPerSecond) / dt);

            leftOutput = leftFeedforward
                + leftController.calculate(m_speeds.get().leftMetersPerSecond,
                leftSpeedSetpoint);

            rightOutput = rightFeedforward
                + rightController.calculate(m_speeds.get().rightMetersPerSecond,
                rightSpeedSetpoint);
        } else {
            leftOutput = leftSpeedSetpoint;
            rightOutput = rightSpeedSetpoint;
        }

        m_output.accept(leftOutput, rightOutput);

        m_prevTime = curTime;
        m_prevSpeeds = targetWheelSpeeds;
    }

 
    public void endTrajectory() {
        m_timer.stop();
    }
  
}
    
