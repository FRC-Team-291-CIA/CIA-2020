package frc.robot;

import frc.autonomous.AutoController;
import frc.subsystems.CIA_Climber;
import frc.subsystems.CIA_Control_Panel;
import frc.subsystems.CIA_DriveBase;
import frc.subsystems.CIA_Dump;
import frc.subsystems.CIA_Intake;

public interface Subsystems{
    /*
    Below is a constructor that takes in the following in order:
    Left Motor Port, Right Motor Port, Shifter Solenoid Port, Left Encoder Ports, Right Encoder Ports,
    The Deadband, The Low Speed, The High, The Override Speed, Right Side Reverse, All Sides Reversed
    The max tilt angle, The tilt angle correct speed
    */
    public static final CIA_DriveBase driveBase = new CIA_DriveBase(RobotMap.leftDriveMotorsPort, RobotMap.rightDriveMotorsPort, 
    RobotMap.shifterSolenoidPort, RobotMap.leftEncoderZeroPort, RobotMap.leftEncoderOnePort, 
    RobotMap.rightEncoderZeroPort, RobotMap.rightEncoderOnePort ,Constants.driveDeadband, 
    Constants.driveLowSpeed, Constants.driveHighSpeed, Constants.driveOverride, 
    Constants.driveRightReverse, Constants.driveAllReverse, Constants.driveMaxAngle, 
    Constants.driveTiltSpeedCorrect);

    /*
    Below is a constructor that takes in the following in order: 
    The first motor port, The second motor port, The Power of Motors, If it is reversed
    */
    public static final CIA_Intake intake = new CIA_Intake(RobotMap.intakeMotorPort, Constants.intakePower, Constants.outtakePower, Constants.intakeIsReversed);
    
    /*
    Below is a constructor that takes in the following:
    First Solenoid Port, If it is reversed
    */
    public static final CIA_Dump dump = new CIA_Dump(RobotMap.dumpSolenoidZeroPort, Constants.dumpIsReversed);

    /*
    Below is a constructor that takes in the following in order:
    The first motor, the second motor, the first and second double solenoid port, the motor speed,
    right reversed boolean, all reversed all
    */
    public static final CIA_Climber climber = new CIA_Climber(RobotMap.climberMotorLeftPort, RobotMap.climberMotorRightPort, 
    RobotMap.climberSolenoidForwardPort, RobotMap.climberSolenoidReversePort, Constants.climberPower, 
    Constants.climberRightReversed, Constants.climberAllReversed);

    /*
    Below is a constructor that takes in the following in order:
    The Motor Port
    */
    public static final CIA_Control_Panel controlPanel = new CIA_Control_Panel(RobotMap.controlPanelMotorPort, Constants.controlPanelMotorSpeed);

    public static final AutoController autoController = new AutoController();
}