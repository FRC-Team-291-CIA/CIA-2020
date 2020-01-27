package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Joystick;

import frc.subsystems.CIA_DriveBase;

import frc.subsystems.CIA_Intake;
import frc.subsystems.CIA_Intake.intakeState;

import frc.subsystems.CIA_Dump;
import frc.subsystems.CIA_Dump.dumpState;

public class Robot extends TimedRobot {
  CIA_DriveBase driveBase;
  CIA_Intake intake;
  CIA_Dump dump;
  Joystick driver; //, operator //Removed Operator Until Needed
  
  @Override
  public void robotInit() {
    driver = new Joystick(0);
    //operator = new Joystick(1); //Removed Operator Until Needed

    driveBase = new CIA_DriveBase(RobotMap.leftDriveMotors, RobotMap.rightDriveMotors, 
    RobotMap.shifterSolenoidPort, Constants.deadband, Constants.lowSpeed, Constants.highSpeed, 
    Constants.override, Constants.rightReverse, Constants.allReverse);

    intake = new CIA_Intake(RobotMap.leftDriveMotors, RobotMap.rightDriveMotors, Constants.intakePower, 
    Constants.intakeIsReversed);

    dump = new CIA_Dump(RobotMap.dumpSolenoidPort, Constants.dumpIsReversed);
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
    driveBase.arcadeDrive(driver.getRawAxis(1), driver.getRawAxis(4), driver.getRawButton(1), driver.getRawButton(4));

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