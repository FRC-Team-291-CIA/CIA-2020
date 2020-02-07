package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.Joystick;

import frc.robot.RobotMap;
import frc.robot.Constants;
import frc.robot.Controls;

import frc.subsystems.CIA_DriveBase;

import frc.subsystems.CIA_Intake;
import frc.subsystems.CIA_Intake.intakeState;

import frc.subsystems.CIA_Dump;
import frc.subsystems.CIA_Dump.dumpState;


public class Robot extends TimedRobot {
  private CIA_DriveBase driveBase;
  private CIA_Intake intake;
  private CIA_Dump dump;
  private Joystick driver, operator;

  @Override
  public void robotInit() {
    LiveWindow.disableAllTelemetry();

    driver = new Joystick(0);
    operator = new Joystick(1);

    /*
    Below is a constructor that takes in the following in order:
    Left Motor Port, Right Motor Port, Shifter Solenoid Port, Left Encoder Ports, Right Encoder Ports,
    The Deadband, The Low Speed, The High, The Override Speed, Right Side Reverse, All Sides reversed
    */
    driveBase = new CIA_DriveBase(RobotMap.leftDriveMotorsPort, RobotMap.rightDriveMotorsPort, 
    RobotMap.shifterSolenoidPort, RobotMap.leftEncoderZeroPort, RobotMap.leftEncoderOnePort, 
    RobotMap.rightEncoderZeroPort, RobotMap.rightEncoderOnePort ,Constants.driveDeadband, 
    Constants.driveLowSpeed, Constants.driveHighSpeed, Constants.driveOverride, 
    Constants.driveRightReverse, Constants.driveAllReverse);

    intake = new CIA_Intake(RobotMap.intakeMotorPort, Constants.intakePower, Constants.intakeIsReversed);
    
    /*
    Below is a constructor that takes in the following:
    First Solenoid Port, Second Solenoid Port, If it is reversed
    */
    dump = new CIA_Dump(RobotMap.dumpSolenoidZeroPort, RobotMap.dumpSolenoidOnePort, Constants.dumpIsReversed);
  }

  @Override
  public void robotPeriodic() {
    driveBase.update();
    intake.update();
    dump.update();
  }

  @Override
  public void autonomousInit() {

  }

  @Override
  public void autonomousPeriodic() {
 
  }

  @Override
  public void teleopInit() {

  }

  @Override
  public void teleopPeriodic() {
    /*
    Below is the drive train running in arcade drive
    The method takes in the following in order:
    Y Axis, X Axis, Switch Gears, Override
    */
    driveBase.arcadeDrive(driver.getRawAxis(Controls.driverYAxis), driver.getRawAxis(Controls.driverXAxis), 
    driver.getRawButtonPressed(Controls.driverShifterButton), driver.getRawButton(Controls.driverDriveOverrideButton));

    //Below is used to set the intake up
    if(operator.getRawButton(Controls.operatorBallIntakeButton)){

      intake.setIntakeState(intakeState.INTAKING);

    } else if(operator.getRawButton(Controls.operatorBallOuttakeButton)){

      intake.setIntakeState(intakeState.OUTTAKING);

    } else {

      intake.setIntakeState(intakeState.STOP);
    }

    //Below is used for the dump
    if(driver.getRawButton(Controls.driverDumpOpenButton)){

      dump.setDumpState(dumpState.OPEN);

    } else if (operator.getRawButton(Controls.operatorDumpCloseButton)){

      dump.setDumpState(dumpState.CLOSED);
      
    } else {

      dump.setDumpState(dumpState.CURRENT_STATE);

    }
  }
  
  @Override
  public void testInit() {

  }

  @Override
  public void testPeriodic() {

  }
}