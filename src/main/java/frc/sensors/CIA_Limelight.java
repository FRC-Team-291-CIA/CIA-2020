package frc.sensors;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

import edu.wpi.first.wpilibj.smartdashboard.*;

public class CIA_Limelight{
    NetworkTable table;
    //NetworkTableEntry getpipe; //Creating the entries for the table
    boolean limelightView = true; //Creates boolean for switching cameras

    public CIA_Limelight(){
        table = NetworkTableInstance.getDefault().getTable("limelight");

        NetworkTableInstance.getDefault().getTable("limelight").getEntry("camMode").setNumber(1);
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(1);
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("stream").setNumber(0); 
    }

    
    private void switchCameras(){ //Used to switch the boolean
        limelightView = !limelightView;
    }

    private boolean currentCamera(){ //Used to return current camera
        return limelightView;
    }

    public void update(boolean cameraSwitch){
        //Below will switch the camera if needed
        if (cameraSwitch){
            this.switchCameras();
        }

        //Below sets the camera to not vision process
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("camMode").setNumber(1);

        //Below turns off the LEDS
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(1);

        //Below checks to see which camera it is and display the camera
        if (this.currentCamera() == true){ 

            NetworkTableInstance.getDefault().getTable("limelight").getEntry("stream").setNumber(1); 

        } else {

            NetworkTableInstance.getDefault().getTable("limelight").getEntry("stream").setNumber(2); 

        }  

        SmartDashboard.putBoolean("Viewing Limelight: ", this.limelightView); //Displays boolean of camera display
    }
}

/*
Author Notes:
To prevent issues with trying to get the limelight data this code is set up with read and get.
Get is to be used in the robot periodic while read is to be used anywhere else in code. Get is used in
the update command so it will always be updated when robot periodic is ran. 
*/


/*
General Notes:
Config panel at http://10.TE.AM.11:5801
Camera stream at http://10.TE.AM.11:5800

To set settings use the following code

NetworkTableInstance.getDefault().getTable("limelight").getEntry("<variablename>").setNumber(<value>);

Where variablename is the setting and value is the number for the setting. Below is the list

ledMode
0 = Use pipeline mode
1 = force off
2 = force blink
3 = force on

camMode
0 = Vision Processor
1 = Driver Camera

pipeline
0 ... 9 = Sets the pipeline number to use

stream
0 = side by side streams (When a second camera is attched)
1 = PiP Main - The secondary camera stream is placed in the lower-right corner of the primary camera stream
2 = PiP Secondary - The primary camera stream is placed in the lower-right corner of the secondary camera stream

snapshot
0 = Stop taking snapshots
1 = Take two snapshots a second
*/