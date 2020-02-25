package frc.robot;

public class RobotMap {
    //All ports used by the robot are in this class to condense them

    //Motors
        //Motors for Drive Base
        public static final int leftDriveMotorsPort = 0;
        public static final int rightDriveMotorsPort = 1;

        //Motors for Intake
        public static final int intakeMotorPort = 2;

        //Motors for Climber
        public static final int climberMotorLeftPort = 3;
        public static final int climberMotorRightPort = 4;

        //Motor for Control Panel
        public static final int controlPanelMotorPort = 5;

    //Solenoids
        //Solenoid for Drive Base
        public static final int shifterSolenoidPort = 1;  
        
        //Solenoid for Dump
        public static final int dumpSolenoidZeroPort = 0;

        //Double Solenoid for Climber
        public static final int climberSolenoidForwardPort = 2;
        public static final int climberSolenoidReversePort = 3;

    //Sensors
        //Encoders for the Drive Train
        public static final int leftEncoderZeroPort = 0;
        public static final int leftEncoderOnePort = 1;
        public static final int rightEncoderZeroPort = 2;
        public static final int rightEncoderOnePort = 3;
}