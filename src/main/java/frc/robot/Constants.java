package frc.robot;

public class Constants {
    //Below is used for Drive Base //TODO - Add drive to each constant
    public static final double deadband = 0.02; //If joysticks are equal or lower than this the motors dont move.
    public static final double lowSpeed = 0.75; //Speed used if in low speed.
    public static final double highSpeed = 0.5; //Speed used if in high speed.
    public static final double override = 1; //Speed used if the override is used.
    public static final boolean rightReverse = true; //Reverses the right side motors.
    public static final boolean allReverse = false; //Reverses all motors.

    //Below is used for intake
    public static final double intakePower = 0.5; //Power Used On The Intake
    public static final boolean intakeIsReversed = false; //Boolean Used To Reverse It

    //Below is used for dump
    public static final boolean dumpIsReversed = false;
}