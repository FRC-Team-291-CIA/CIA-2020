package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Joystick;

import frc.subsystems.CIA_DriveBase;

import frc.subsystems.CIA_Intake;
import frc.subsystems.CIA_Intake.intakeState;

import frc.subsystems.CIA_Dump;
import frc.subsystems.CIA_Dump.dumpState;

public class Robot extends TimedRobot {
  private CIA_DriveBase driveBase;
  private CIA_Intake intake;
  private CIA_Dump dump;
  private Joystick driver; //, operator //Removed Operator Until Needed
  
  @Override
  public void robotInit() {
    driver = new Joystick(0);
    //operator = new Joystick(1); //Removed Operator Until Needed

    driveBase = new CIA_DriveBase(RobotMap.leftDriveMotorsPort, RobotMap.rightDriveMotorsPort, 
    RobotMap.shifterSolenoidPort, RobotMap.leftEncoderZeroPort, RobotMap.leftEncoderOnePort, 
    RobotMap.rightEncoderZeroPort, RobotMap.rightEncdoerOnePort ,Constants.driveDeadband, 
    Constants.driveLowSpeed, Constants.driveHighSpeed, Constants.driveOverride, 
    Constants.driveRightReverse, Constants.driveAllReverse);

    intake = new CIA_Intake(RobotMap.leftIntakeMotorPort, RobotMap.rightIntakeMotorPort, Constants.intakePower, 
    Constants.intakeIsReversed);

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
    The mathod takes in the following in order:
    Y Axis, X Axis, Switch Gears, Override
    */
    driveBase.arcadeDrive(driver.getRawAxis(1), driver.getRawAxis(4), driver.getRawButtonPressed(1), driver.getRawButton(4));

    //Below is used to set the intake up
    if(driver.getRawButton(6)){

      intake.setIntakeState(intakeState.INTAKING);

    } else if(driver.getRawButton(7)){

      intake.setIntakeState(intakeState.OUTTAKING);

    } else {

      intake.setIntakeState(intakeState.STOP);
    }

    //Below is used for the dump
    if(driver.getRawButton(8)){

      dump.setDumpState(dumpState.OPEN);

    } else {

      dump.setDumpState(dumpState.CLOSED);
      
    }
  }
  
  @Override
  public void testInit() {

  }

  @Override
  public void testPeriodic() {

  }
}