package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.Joystick;

import edu.wpi.first.cameraserver.CameraServer;

import frc.robot.RobotMap;
import frc.robot.Constants;
import frc.robot.Controls;

import frc.subsystems.CIA_DriveBase;

import frc.subsystems.CIA_Intake;
import frc.subsystems.CIA_Intake.intakeState;

import frc.subsystems.CIA_Dump;
import frc.subsystems.CIA_Dump.dumpState;

import frc.subsystems.CIA_Climber;
import frc.subsystems.CIA_Climber.climbState;

import frc.subsystems.CIA_Control_Panel;
import frc.subsystems.CIA_Control_Panel.controlPanelState;

import frc.sensors.CIA_Limelight;

public class Robot extends TimedRobot {
  private CIA_DriveBase driveBase;
  private CIA_Intake intake;
  private CIA_Dump dump;
  private CIA_Climber climber;
  private CIA_Control_Panel controlPanel;
  private Joystick driver, operator;
  private CIA_Limelight camera;

  @Override
  public void robotInit() {
    LiveWindow.disableAllTelemetry(); //Disabled due to too many can errors

    //Below the joysticks are created
    driver = new Joystick(0);
    operator = new Joystick(1);

    /*
    Below is a constructor that takes in the following in order:
    Left Motor Port, Right Motor Port, Shifter Solenoid Port, Left Encoder Ports, Right Encoder Ports,
    The Deadband, The Low Speed, The High, The Override Speed, Right Side Reverse, All Sides Reversed
    The max tilt angle, The tilt angle correct speed
    */
    driveBase = new CIA_DriveBase(RobotMap.leftDriveMotorsPort, RobotMap.rightDriveMotorsPort, 
    RobotMap.shifterSolenoidPort, RobotMap.leftEncoderZeroPort, RobotMap.leftEncoderOnePort, 
    RobotMap.rightEncoderZeroPort, RobotMap.rightEncoderOnePort ,Constants.driveDeadband, 
    Constants.driveLowSpeed, Constants.driveHighSpeed, Constants.driveOverride, 
    Constants.driveRightReverse, Constants.driveAllReverse, Constants.driveMaxAngle, 
    Constants.driveTiltSpeedCorrect);

    /*
    Below is a constructor that takes in the following in order: 
    The first motor port, The second motor port, The Power of Motors, If it is reversed
    */
    intake = new CIA_Intake(RobotMap.intakeMotorPort, Constants.intakePower, Constants.outtakePower, Constants.intakeIsReversed);
    
    /*
    Below is a constructor that takes in the following:
    First Solenoid Port, If it is reversed
    */
    dump = new CIA_Dump(RobotMap.dumpSolenoidZeroPort, Constants.dumpIsReversed);

    /*
    Below is a constructor that takes in the following in order:
    The first motor, the second motor, the first and second double solenoid port, the motor speed,
    right reversed boolean, all reversed all
    */
    climber = new CIA_Climber(RobotMap.climberMotorLeftPort, RobotMap.climberMotorRightPort, 
    RobotMap.climberSolenoidForwardPort, RobotMap.climberSolenoidReversePort, Constants.climberPower, 
    Constants.climberRightReversed, Constants.climberAllReversed);

    controlPanel = new CIA_Control_Panel(RobotMap.controlPanelMotorPort, Constants.controlPanelMotorSpeed);

    camera = new CIA_Limelight();
  }

  @Override
  public void robotPeriodic() {
    //Below uses robot periodic to update it sensors and / or smartdashboard
    driveBase.update();
    intake.update();
    dump.update();
    climber.update();
    controlPanel.update();

    //Below uses the update function to display the smartdashboard and to switch cameras
    camera.update(driver.getRawButtonPressed(Controls.driverCameraSwitchButton) || 
    operator.getRawButtonPressed(Controls.operatorCameraSwitchButton));
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

    //Below is used for the climber
    if (driver.getRawButton(Controls.driverClimberClimbZeroButton) && driver.getRawButton(Controls.driverClimberClimbOneButton)){
      
      climber.setClimbState(climbState.CLIMB);

    } else if (operator.getRawButton(Controls.operatorClimberUp)){

      climber.setClimbState(climbState.RAISE_UP);

    } else if (operator.getRawButton(Controls.operatorClimberStore)){

      climber.setClimbState(climbState.STORE);
 
    } else {

      climber.setClimbState(climbState.CURRENT_STATE);

    }

    //Below is code for the control panel
    if (operator.getRawButton(Controls.operatorPanelSpin)){

      controlPanel.setControlState(controlPanelState.SPIN);

    } else if (operator.getRawButton(Controls.operatorPanelColor)){

      controlPanel.setControlState(controlPanelState.GO_TO_COLOR);

      if(operator.getRawButtonPressed(Controls.operatorPanelColor)){
        CameraServer.getInstance().startAutomaticCapture();
      }

    } else {

      controlPanel.setControlState(controlPanelState.STOP);

    }

        /*
    Below is the drive train running in arcade drive
    The method takes in the following in order:
    Y Axis, X Axis, Switch Gears, Override
    */
    driveBase.arcadeDrive(driver.getRawAxis(Controls.driverYAxis), driver.getRawAxis(Controls.driverXAxis), 
    driver.getRawButtonPressed(Controls.driverShifterButton), driver.getRawButton(Controls.driverDriveOverrideButton));
  }
  
  @Override
  public void testInit() {

  }

  @Override
  public void testPeriodic() {
    if (driver.getRawButton(Controls.driverClimberClimbZeroButton) && driver.getRawButton(Controls.driverClimberClimbOneButton)){
      
      climber.setClimbState(climbState.WINCH_ONLY);

    } else {

      climber.setClimbState(climbState.STORE);
      
    }

  }

  @Override
  public void disabledInit() {

  }

  @Override
  public void disabledPeriodic() {

  }
}