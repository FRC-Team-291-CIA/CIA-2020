package frc.robot;

public class Controls {
    //This class contains all of the buttons and axis assignments

    //Driver - Must be in "X" mode
        public static final int driverYAxis = 1; //Left Stick - Up and Down
        public static final int driverXAxis = 4; //Right Stick - Left and Right
        public static final int driverDumpOpenButton = 1; //Green "A"
        public static final int driverShifterButton = 5; //Left Top Bumper
        public static final int driverDriveOverrideButton = 6; //Right Top Bumper
        public static final int driverCameraSwitchButton = 2; //Red "B"
        public static final int driverClimberClimbZeroButton = 7; //"Back" Button
        public static final int driverClimberClimbOneButton = 8; //"Start" Button

    //Operator - Must be in "D" mode
        public static final int operatorBallIntakeButton = 2; //Green "A"
        public static final int operatorBallOuttakeButton = 3; //Red "B"
        public static final int operatorDumpCloseButton = 12; //Right Joystick Button
        public static final int operatorClimberStore = 5; //Left Top Bumper
        public static final int operatorClimberUp = 6; //Right Top Bumper
        public static final int operatorCameraSwitchButton = 8; //TODO - Find Button
}