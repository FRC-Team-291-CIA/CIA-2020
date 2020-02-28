package frc.robot;

public class Constants {
    //This class holds all the constants used by the robot

    //Below is used for Drive Base
    public static final double driveDeadband = 0.02; //If joysticks are equal or lower than this the motors dont move.
    public static final double driveLowSpeed = 0.15; //Speed used if in low speed.
    public static final double driveHighSpeed = 0.4; //Speed used if in high speed.
    public static final double driveOverride = 0.8; //Speed used if the override is used.
    public static final boolean driveRightReverse = true; //Reverses the right side motors.
    public static final boolean driveAllReverse = true; //Reverses all motors.

    //Below is used for intake
    public static final double intakePower = 0.75; //Power Used On The Intake
    public static final boolean intakeIsReversed = false; //Boolean Used To Reverse It

    //Below is used for dump
    public static final boolean dumpIsReversed = false; //Used to reverse the dump solenoid

    //Below is used for the climber
    public static final double climberPower = 0.5; //Used for the climber power
    public static final boolean climberRightReversed = true; //Used for reversing right side of climber
    public static final boolean climberAllReversed = false; //Used for reversing the entire climber
}