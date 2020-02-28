package frc.sensors;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.SPI;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class CIA_Gyro{
    AHRS ahrs;

    public CIA_Gyro(){

        ahrs = new AHRS(SPI.Port.kMXP); 

    }

    public void update(){
        SmartDashboard.putBoolean("Gyro Is Connected", ahrs.isConnected());
        SmartDashboard.putNumber("Heading:", ahrs.getCompassHeading());
    }
}