package frc.sensors;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.SPI;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class CIA_Gyro{
    private AHRS ahrs;
    private double tilt, heading;
    

    public CIA_Gyro(){

        ahrs = new AHRS(SPI.Port.kMXP); 

    }

    public double getTilt(){
        return tilt;
    }

    public double getHeading(){
        return heading;
    }

    public void update(){
        tilt = ahrs.getRoll();
        heading = ahrs.getPitch();
        SmartDashboard.putBoolean("Gyro Is Connected", ahrs.isConnected());
        SmartDashboard.putNumber("Tilt", tilt);
        SmartDashboard.putNumber("Heading", heading);
    }
}