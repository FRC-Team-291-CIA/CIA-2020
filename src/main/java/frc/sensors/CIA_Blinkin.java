package frc.sensors;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

//TODO - Set Light Ports
public class CIA_Blinkin{
    Spark lights;

    public CIA_Blinkin(int pWMPort){ 
        lights = new Spark(pWMPort);
    }

    public void update(){
 
    }
}

/*
Documentation Link: http://www.revrobotics.com/content/docs/REV-11-1105-UM.pdf
*/