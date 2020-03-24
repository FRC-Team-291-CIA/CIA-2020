/* 
File Name: CIA_DriveBase.java
Use: This is used as a drive base controller.
Reuse: This file can be reused from year to year but will needed edited for which sensors are used
       constants will need changed for that years robot.
Reuse Tips: If needed to add more motor, add more objects, then add them to the speed controll groups.
            Highly reccomended to hard code disabled anti - tip until you are on the practice field
Files Directly Used / Is In: Robot.java and Subsystem_Variables.java
Sensors Used: Two magnetic encoders locatd in the gearbox, Gyro mounted on the RoboRio
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
    private CIA_SparkMax motorZero, motorOne; //Individual Motor Objects
    private SpeedControllerGroup leftGroup, rightGroup; //Left and Right Side Motor groups
    private Solenoid shifter; //Pneumatic solenoid used to shift the gears from high and low
    private double lowSpeed, highSpeed, overrideSpeed, tiltSpeedCorrection; //Different speeds used on the motors
    private double deadband; //Used for the controller deadband (For when the stick is at center but doesn't read zero)
    private double mathLeft, mathRight; //Used in calculations for the motor speeds in the drive train
    private double maxTiltAngle; //Used for how much the robot can tilt before anti - tip actives
    private double rightSpeed, rightDistance, leftSpeed, leftDistance;
    private boolean inHighState = false; //Used in the smartdashbaord / class to display the solenoid state and change it
    private boolean override = false; //Used in the smartdashboard / class to display the override speed state and change it
    private Encoder leftEncoder, rightEncoder; //Left and Right encoder projects
    private CIA_Gyro gyro; //Gyro obect
    private DifferentialDriveOdometry odometry; //Used for auto driving
    
    //Below is an expierimental feature, it muse be hard coded to prevent any accidental use
    private boolean tiltCorrectionEnabled = false; 

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
        rightEncoder.setDistancePerPulse(1);

        shifter = new Solenoid(shifterSolenoidPort); //Creates the solenoid object for shifting

        deadband = newDeadband; //Takes in the deadband variable

        lowSpeed = newLowSpeed; //Takes in the low speed variable
        highSpeed = newHighSpeed; //Takes in the high speed variable

        overrideSpeed = newOverrideSpeed; //Takes in the override speed variable

        maxTiltAngle = newMaxTiltAngle; //Takes in the max tilt

        tiltSpeedCorrection = newTiltSpeedCorrect; //Takes in the tilt speed correct

        gyro = new CIA_Gyro();  //Creates the gyro
    }

    //The below method sets the inverted motor groupds based on the data from the constructor
    private void setInvertedGroups(boolean isRightReversed, boolean isAllReversed){
        if (isRightReversed){ //Checks is the right group is inverted
            rightGroup.setInverted(true); //Inverts the right group if true
        }

        if (isAllReversed){ //Checks if the entire bot is inverted
            leftGroup.setInverted(true); //Invertes the left group if true
            rightGroup.setInverted(true); //Inverts the right group if true
            
            if (rightGroup.getInverted()){ //Checks if the right group is inverted if true
                rightGroup.setInverted(false); //Invertes the right group, negating the original invert, if true
            }
        }
    }

    //Used to set the state of the gears
    private void updateGears(){
        if(inHighState){ //Checks if the robot should be in the high state
            shifter.set(false); //If true places the robot in high state
        } else {
            shifter.set(true); //Otherwise it places it in low state
        }
    }

    //TODO - Rewrite tikt correction
    /*
    -Will need to only run the wheels in one rotation (Will need failsafe)
    -Adjustable Power based on tilt
    -Will need to negate when its turning due to gyro not being centered
    */
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

    //All encoder readings are in feet
    private void getEncoders(){
        //Values are initally in ticks, divded by 217.3 to return in feet
        leftDistance = (leftEncoder.getDistance()/217.3);
        leftSpeed = (leftEncoder.getRate()/217.3);
        rightDistance = (rightEncoder.getDistance()/217.3);
        rightSpeed = (rightEncoder.getRate()/217.3);
    }

    /*
    Below is used for driving in arcade style. Switch Gears must be used with the 
    .getRawButtonPressed(int button) method for joysticks. This is to prevent it from tripping
    multiple times because the code will run multiple times in a second
    */
    public void arcadeDrive(double yAxis, double xAxis, boolean switchGears, boolean newOverride){
        if(tiltCorrectionEnabled){ //Checks to see if tilt correction is enabled
            double tilt = gyro.getTilt(); //Gets the tilt
            if (Math.abs(tilt) > maxTiltAngle){ //Checks to see if the robot is tilting
                if(this.correctTilt(tilt)){ //If true, runs the correction method
                    return; //Stops the rest from running
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

        override = newOverride; //Udates the override

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

        //Below sets the motor speeds
        leftGroup.set(mathLeft);
        rightGroup.set(mathRight);
    }

    //The below method is used to update the auto
    public void odometryUpdate(){
        odometry.update(Rotation2d.fromDegrees(gyro.getHeading()), leftEncoder.getDistance(), rightEncoder.getDistance());
    }

    //The below method sets the drivebase motors using voltage
    public void setDrivetrainVoltage(double leftVoltage, double rightVoltage){
        leftGroup.setVoltage(leftVoltage);
        rightGroup.setVoltage(rightVoltage);
    }

    //The below method returns the wheel speeds of the drivebase from the encoders
    public DifferentialDriveWheelSpeeds getWheelSpeeds(){
        return new DifferentialDriveWheelSpeeds(this.leftSpeed, this.rightSpeed);
    }

    //The below method returns the robots posistions in meters
    public Pose2d getPoseInMeters(){
        return odometry.getPoseMeters();
    }

    //The below method returns the robots posistion in inches
    public Pose2d getPoseInches(){
        double x = getPoseInMeters().getTranslation().getX() * 39.37;
        double y = getPoseInMeters().getTranslation().getY() * 39.37;
        return new Pose2d(new Translation2d(x, y), getPoseInMeters().getRotation());
    }

    
    public void update(){
        gyro.update(); //Updates the gyro values
        odometryUpdate(); //Updates the auto drivebase controller
        this.getEncoders(); //Updates the encoders
        SmartDashboard.putNumber("Left Drive Base", this.mathLeft); //Sends the left drive to the dashboard
        SmartDashboard.putNumber("Right Drive Base", this.mathRight); //Sends the right drive to the dashboard
        SmartDashboard.putBoolean("Is High Gear", this.inHighState); //Shows if its in high gear to the dashboard
        SmartDashboard.putBoolean("Drive Is Unrestricted", this.override); //Shows if the driver took off the restriction
        SmartDashboard.putNumber("Left Encoder Distance", this.leftDistance); //Shows the distance of the left encoder
        SmartDashboard.putNumber("left Encoder Speed", this.leftSpeed); //Shows the speed of the left encoder
        SmartDashboard.putNumber("Right Encoder Distance", this.rightDistance); //Shows the distance of the right encoder
        SmartDashboard.putNumber("Right Encoder Speed", this.rightSpeed); //Shows the speed of the right encoder
    }
}