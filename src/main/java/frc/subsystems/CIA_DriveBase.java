/* 
File Name: CIA_DriveBase.java
Use: This is used as a drive base controller.
Reuse: This file can be reused from year to year but will needed edited for which sensors are used
       constants will need changed for that years robot.
Files Directly Used / Is In: Robot.java and Subsystem_Variables.java
*/

package frc.subsystems;

//The imports below is used for autonomous
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;

//The imports below are used to control sensors and relay information
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;

//Used to access the Subsystem Variables
import frc.subsystems.Subsystem_Variables;

//Sensors used by this class
import frc.sensors.CIA_SparkMax; //Our own import of the SparkMax Code
import frc.sensors.CIA_Gyro;

public class CIA_DriveBase {
    private CIA_SparkMax motorZero, motorOne;
    private SpeedControllerGroup leftGroup, rightGroup;
    private Solenoid shifter;
    private double lowSpeed, highSpeed, deadband, mathLeft, mathRight, overrideSpeed, maxTiltAngle, tiltSpeedCorrection;
    private boolean inHighState = false;
    private boolean override = false;
    private Encoder leftEncoder, rightEncoder;
    private CIA_Gyro gyro;
    private boolean tiltCorrectionEnabled = false; //This is an experimental feature, It must be HardCodded
    private double rightSpeed, rightDistance, leftSpeed, leftDistance;
    private DifferentialDriveOdometry odometry;

    /*
    Below is a constructor that takes in the following in order:
    Left Motor Port, Right Motor Port, Shifter Solenoid Port, Left Encoder Ports, Right Encoder Ports,
    The Deadband, The Low Speed, The High, The Override Speed, Right Side Reverse, All Sides Reversed
    The max tilt angle, The tilt angle correct speed
    These values come from the robot.java class which grab values from RobotMap.java and Constants.java
    */
    public CIA_DriveBase(int leftMotorsPort, int rightMotorsPort, int shifterSolenoidPort, int leftEncoderPortZero, int leftEncoderPortOne, int rightEncoderPortZero, int rightEncoderPortOne, double newDeadband, 
                        double newLowSpeed, double newHighSpeed, double newOverrideSpeed, boolean newIsRightReversed,
                        boolean newIsAllReversed, double newMaxTiltAngle, double newTiltSpeedCorrect){
                            
        //Below creates the motor objects
        motorZero = new CIA_SparkMax(leftMotorsPort);
        motorOne = new CIA_SparkMax(rightMotorsPort);

        //Below sets each of the motors into groups
        leftGroup = new SpeedControllerGroup(motorZero);
        rightGroup = new SpeedControllerGroup(motorOne);

        this.setInvertedGroups(newIsRightReversed, newIsAllReversed);

        //Below creates the encoders
        leftEncoder = new Encoder(leftEncoderPortZero, leftEncoderPortOne);
        rightEncoder = new Encoder(rightEncoderPortZero, rightEncoderPortOne);
        leftEncoder.setDistancePerPulse(1);
        leftEncoder.setReverseDirection(true);
        rightEncoder.setDistancePerPulse(1);

        shifter = new Solenoid(shifterSolenoidPort); //Creates the solenoid object for shifting

        deadband = newDeadband; //Takes in the deadband variable

        lowSpeed = newLowSpeed; //Takes in the low speed variable
        highSpeed = newHighSpeed; //Takes in the high speed variable

        overrideSpeed = newOverrideSpeed; //Takes in the override speed variable

        maxTiltAngle = newMaxTiltAngle;

        tiltSpeedCorrection = newTiltSpeedCorrect;

        gyro = new CIA_Gyro();
    }

    private void setInvertedGroups(boolean isRightReversed, boolean isAllReversed){
        if (isRightReversed){
            rightGroup.setInverted(true);
        }

        if (isAllReversed){
            leftGroup.setInverted(true);
            
            if (rightGroup.getInverted()){
                rightGroup.setInverted(false);
            }
        }
    }

    //Used to set the state of the gears
    private void updateGears(){
        if(inHighState){
            shifter.set(false);
        } else {
            shifter.set(true);
        }
    }

    //TODO - Verify this works, Add a cut off after one rotation once enabled
    private boolean correctTilt(double tiltAngle){
        if(leftSpeed > 0 && rightSpeed < 0){
            return false;
        } else if (leftSpeed < 0 && rightSpeed > 0){
            return false;
        }

        if(Subsystem_Variables.isOnlyLowGear){
            inHighState = false;
        } else {
            inHighState = true;
        }
        
        this.updateGears();

        if (tiltAngle > 0){
            leftGroup.set(-tiltSpeedCorrection);
            rightGroup.set(-tiltSpeedCorrection);
            return true;
        } else {
            leftGroup.set(tiltSpeedCorrection);
            rightGroup.set(tiltSpeedCorrection);
            return true;
        }
    }

    //All encoder readings are in inches
    private void getEncoders(){
        leftDistance = (leftEncoder.getDistance()/217.3);
        leftSpeed = (leftEncoder.getRate()/217.3);
        rightDistance = (rightEncoder.getDistance()/217.3);
        rightSpeed = (rightEncoder.getRate()/217.3);
    }

    public double getRightEncoderVel(){
        getEncoders();
        return rightSpeed;
    }

    public double getLeftEncoderVel(){
        getEncoders();
        return -leftSpeed;
    }

    /*
    Below is used for driving in arcade style. Switch Gears must be used with the 
    .getRawButtonPressed(int button) method for joysticks. This is to prevent it from tripping
    multiple times
    */
    public void arcadeDrive(double yAxis, double xAxis, boolean switchGears, boolean newOverride){
        if(tiltCorrectionEnabled){
            double tilt = gyro.getTilt();
            if (Math.abs(tilt) > maxTiltAngle){
                if(this.correctTilt(tilt)){
                    return;
                }
            }
        }

        if(switchGears){ //Checks to see if gears need to switch
            inHighState = !inHighState; //Switches States
        }

        if(Subsystem_Variables.isOnlyLowGear){
            inHighState = false;
        }

        this.updateGears(); //Updates the gears solenoid

        override = newOverride;

	    mathLeft = yAxis - xAxis; //Calculates the math for the left side
        mathRight = yAxis + xAxis; //Calculates the math for the right side
    
        //Below checks to see if we are under the deadband
	    if (Math.abs(mathLeft) <= deadband && Math.abs(mathRight) <= deadband){ 
            //Below sets the motors to zero if under the deadband
            leftGroup.set(0); 
		    rightGroup.set(0); 
	    } else { //USed if above deadband
            if(inHighState){ //Checks if it is in high state
                if(override){ //Checks if it is overridden
                    //Below sets the math speeds
                    mathLeft = mathLeft*overrideSpeed; 
                    mathRight = mathRight*overrideSpeed;
                } else { //USed if in high and not overridden
                    //Below sets the math speeds
                    mathLeft = mathLeft*highSpeed;
                    mathRight = mathRight*highSpeed;
                }
	        } else { //Used if in low speed
                if(override){ //Checks to see if its overridden
                    //Belows sets the math speeds
                    mathLeft = mathLeft*overrideSpeed; 
                    mathRight = mathRight*overrideSpeed;
                } else { //Used if its in low and not overridden
                    //Below sets the math speeds
                    mathLeft = mathLeft*lowSpeed;
                    mathRight = mathRight*lowSpeed;
                }
            }
        }

        leftGroup.set(mathLeft);
        rightGroup.set(mathRight);
    }

    public void odometryUpdate(){
        odometry.update(Rotation2d.fromDegrees(gyro.getHeading()), leftEncoder.getDistance(), rightEncoder.getDistance());
    }

    public void setDrivetrainVoltage(double leftVoltage, double rightVoltage){
        leftGroup.setVoltage(leftVoltage);
        rightGroup.setVoltage(rightVoltage);
    }

    public DifferentialDriveWheelSpeeds getWheelSpeeds(){
        return new DifferentialDriveWheelSpeeds(getLeftEncoderVel(), getRightEncoderVel());
    }

    public Pose2d getPoseInMeters(){
        return odometry.getPoseMeters();
    }

    public Pose2d getPoseInches(){
        double x = getPoseInMeters().getTranslation().getX() * 39.37;
        double y = getPoseInMeters().getTranslation().getY() * 39.37;
        return new Pose2d(new Translation2d(x, y), getPoseInMeters().getRotation());
    }

    
    public void update(){
        gyro.update();
        odometryUpdate();
        this.getEncoders();
        SmartDashboard.putNumber("Left Drive Base", this.mathLeft); //Sends the left drive to the dashboard
        SmartDashboard.putNumber("Right Drive Base", this.mathRight); //Sends the right drive to the dashboard
        SmartDashboard.putBoolean("Is High Gear", this.inHighState); //Shows if its in high gear to the dashboard
        SmartDashboard.putBoolean("Drive Is Unrestricted", this.override); //Shows if the driver took off the restriction
        SmartDashboard.putNumber("Left Encoder Distance", this.leftDistance);
        SmartDashboard.putNumber("left Encoder Speed", this.leftSpeed);
        SmartDashboard.putNumber("Right Encoder Distance", this.rightDistance);
        SmartDashboard.putNumber("Right Encoder Speed", this.rightSpeed);
    }
}