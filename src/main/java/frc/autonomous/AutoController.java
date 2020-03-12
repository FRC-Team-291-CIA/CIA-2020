package frc.autonomous;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoController {
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
    
    public AutoController() {
        //fieldPosition.initSendable(fieldPos);
        fieldPosition.setDefaultOption("Left", left);
        fieldPosition.addOption("Right", right);
        fieldPosition.addOption("Center", center);
        SmartDashboard.putData("FIELD POSITION", fieldPosition);

        //autoChooser.initSendable(autoChoose);
        autoChooser.setDefaultOption("Do Nothing", doNothing);
        autoChooser.addOption("Cross Baseline", crossBaseline);
        autoChooser.addOption("Dump Balls Once", dumpBallsOnce);
        autoChooser.addOption("Dump Balls And Pick Up More", dumpBallsAndPickUp);
        SmartDashboard.putData("AUTO CHOOSER", autoChooser);
    }

    public void start() { 
        select();
    }

    public AutoMode select(){
        AutoMode autoMode = new DoNothing();
        String fieldPos = fieldPosition.getSelected();
        String autoRoutine = autoChooser.getSelected();

        if(fieldPos.equalsIgnoreCase(right)){
            switch(autoRoutine){
                case doNothing:
                    autoMode = new DoNothing();
                    break;
                case crossBaseline: 
                    autoMode = new CrossBaseline();
                    break;
                case dumpBallsOnce: 
                    autoMode = new RightSideDump();
                    break;
                case dumpBallsAndPickUp: 
                    autoMode = new RightSideDumpAndPickUp();
                    break;
            }
        }else if(fieldPos.equalsIgnoreCase(left)){
            switch(autoRoutine){
                case doNothing:
                    autoMode = new DoNothing();
                    break;
                case crossBaseline: 
                    autoMode = new CrossBaseline();
                    break;
                case dumpBallsOnce: 
                    autoMode = new LeftSideDump();
                    break;
                case dumpBallsAndPickUp: 
                    autoMode = new LeftSideDumpAndPickUp();
                    break;
            }
        }else if(fieldPos.equalsIgnoreCase(center)){
            switch(autoRoutine){
                case doNothing: 
                    autoMode = new DoNothing();
                    break;
                case crossBaseline:
                    autoMode = new CrossBaseline();
                    break;
                case dumpBallsOnce:
                    autoMode = new CenterSideDump();
                    break;
                case dumpBallsAndPickUp: 
                    autoMode = new CenterSideDumpAndPickUp();
                    break;
            }
        }

        return autoMode;
        
    }

    public AutoMode select(AutoMode autoMode){
        return autoMode;
    }
}

