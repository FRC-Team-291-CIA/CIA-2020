package frc.sensors;

import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class CIA_UltraSonic{
    Ultrasonic topSensor, bottomSensor;
    double topDistance, bottomDistance;

    public CIA_UltraSonic(int bottomPortZero, int bottomPortOne, int topPortZero, int topPortOne){
        topSensor = new Ultrasonic(topPortZero, topPortOne);
        topSensor.setAutomaticMode(true);
        bottomSensor = new Ultrasonic(bottomPortZero, bottomPortOne);
        bottomSensor.setAutomaticMode(true);
    }

    public double getTopSensor(){
        return topDistance;
    }

    public double getBottomSensor(){
        return bottomDistance;
    }

    public void update(){
        topDistance = topSensor.getRangeInches();
        bottomDistance = bottomSensor.getRangeInches();
        SmartDashboard.putNumber("Top Sensor", topDistance);
        SmartDashboard.putNumber("Bottom Sensor", bottomDistance);
    }
}