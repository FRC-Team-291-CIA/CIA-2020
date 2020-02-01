package frc.robot;

public class RobotMap {
    //All ports used by the robot are in this class to condense them

    //Motors
        //Motors for Drive Base
        public static final int leftDriveMotorsPort = 0;
        public static final int rightDriveMotorsPort = 1;

        //Motors for Intake
        public static final int leftIntakeMotorPort = 2;
        public static final int rightIntakeMotorPort = 3;

    //Solenoids
        //Solenoid for Drive Base
        public static final int shifterSolenoidPort = 0;  
        
        //Solenoid for Dump
        public static final int dumpSolenoidZeroPort = 1;
        public static final int dumpSolenoidOnePort = 2; 

    //Sensors
        //Encoders for the Drive Train
        public static final int leftEncoderZeroPort = 0;
        public static final int leftEncoderOnePort = 1;
        public static final int rightEncoderZeroPort = 2;
        public static final int rightEncdoerOnePort = 3;
}