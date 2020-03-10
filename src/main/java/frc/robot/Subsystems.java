package frc.robot;

import frc.autonomous.AutoController;
import frc.autonomous.AutoMode;
import frc.subsystems.CIA_Climber;
import frc.subsystems.CIA_Control_Panel;
import frc.subsystems.CIA_DriveBase;
import frc.subsystems.CIA_Dump;
import frc.subsystems.CIA_Intake;

public interface Subsystems{
    //initializing the climber
    public static final CIA_Climber climber = new CIA_Climber(RobotMap.climberMotorLeftPort, RobotMap.climberMotorRightPort, 
    RobotMap.climberSolenoidForwardPort, RobotMap.climberSolenoidReversePort, Constants.climberPower, 
    Constants.climberRightReversed, Constants.climberAllReversed);
    //initializing the drivetrain
    public static final CIA_DriveBase driveBase = new CIA_DriveBase(RobotMap.leftDriveMotorsPort, RobotMap.rightDriveMotorsPort, 
    RobotMap.shifterSolenoidPort, RobotMap.leftEncoderZeroPort, RobotMap.leftEncoderOnePort, 
    RobotMap.rightEncoderZeroPort, RobotMap.rightEncoderOnePort ,Constants.driveDeadband, 
    Constants.driveLowSpeed, Constants.driveHighSpeed, Constants.driveOverride, 
    Constants.driveRightReverse, Constants.driveAllReverse, Constants.driveMaxAngle, 
    Constants.driveTiltSpeedCorrect);
    //initializing the control panel mechanism
    public static final CIA_Control_Panel controlPanel = new CIA_Control_Panel(RobotMap.controlPanelMotorPort, 
    Constants.controlPanelMotorSpeed);
    //initializing the dumpster mechanism
    public static final CIA_Dump dump = new CIA_Dump(RobotMap.dumpSolenoidZeroPort, Constants.dumpIsReversed);
    //initialiizing the intake mechanism 
    public static final CIA_Intake intake = new CIA_Intake(RobotMap.intakeMotorPort, Constants.intakePower, Constants.outtakePower, 
    Constants.intakeIsReversed);

    public static final AutoController autoController = new AutoController();



}