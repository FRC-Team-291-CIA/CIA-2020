package frc.autonomous;

import java.util.HashMap;

import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

public class AutoMode {
    // Strings here are for labeling our starting position on the field in the
    // smartdashboard
    private static final String left = "Left";
    private static final String right = "Right";
    private static final String center = "Center";

    // Strings here are for determining which autonomous mode we want which will
    // also be selectable through the smartdashboard
    private static final String doNothing = "Do Nothing";//
    private static final String crossBaseline = "Cross Baseline";// makes the robot cross the baseline, nothing else
    private static final String dumpBallsOnce = "Dump Balls Once";// makes the robot go to the powerport to dump balls
    private static final String dumpBallsAndPickUp = "Dump Balls And Pick Up More";// dumpBallsOnce plus some more

    /*
     * using sendable choosers for reference,
     * https://first.wpi.edu/FRC/roborio/beta/docs/java/edu/wpi/first/wpilibj/
     * smartdashboard/SendableChooser.html, Sendable choosers will let us determine
     * which position we are in on the field and also which auto mode we want to do
     */
    private SendableChooser<String> fieldPosition = new SendableChooser<>();
    private SendableChooser<String> autoChooser = new SendableChooser<>();
    // hashmaps will be used to identify automodes and run them
    private HashMap<String, Object> autoMap = new HashMap<String, Object>();

    public AutoMode() {

    }

    public void start() {
        select();
    }

    private void select() {

    }

}