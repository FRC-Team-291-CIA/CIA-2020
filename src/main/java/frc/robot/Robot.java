package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Joystick;

import edu.wpi.first.cameraserver.CameraServer;

import frc.robot.Controls;

import frc.subsystems.CIA_Intake.intakeState;
import frc.subsystems.CIA_Dump.dumpState;
import frc.subsystems.CIA_Climber.climbState;
import frc.subsystems.CIA_Control_Panel.controlPanelState;

import frc.sensors.CIA_Limelight;

public class Robot extends TimedRobot implements Subsystems {
  private Joystick driver, operator;
  private CIA_Limelight camera;
  private boolean isUSBCamStarted = false;
  private AutoMode autoMode;

  @Override
  public void robotInit() {
    //Below the joysticks are created
    driver = new Joystick(0);
    operator = new Joystick(1);
    
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
    autoMode = autoController.select();
  }

  @Override
  public void autonomousPeriodic() {
    autoMode.autoRun();
  }

  @Override
  public void teleopInit() {
    intake.setIntakeState(intakeState.STOP);
    dump.setDumpState(dumpState.CURRENT_STATE);
    climber.setClimbState(climbState.STORE);
    controlPanel.setControlState(controlPanelState.STOP);
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

      if(operator.getRawButtonPressed(Controls.operatorPanelColor)){

        if(!isUSBCamStarted){

        CameraServer.getInstance().startAutomaticCapture();

        isUSBCamStarted = true;

        }
        
      }

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