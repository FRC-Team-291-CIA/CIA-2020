package frc.sensors;

import edu.wpi.first.wpilibj.Spark;

//Deprecated 3/9/2020 because we are not using the light strip
@Deprecated
public class CIA_Blinkin{
    Spark lights;
    double colorRed = 0.61;
    double colorYellow = 0.69;
    double colorOrange = 0.65;
    double colorGreen = 0.77;
    double colorWhite = 0.93;
    double colorBlue = 0.87;
    double colorHotPink = 0.57;

    public CIA_Blinkin(int pWMPort){ 
        lights = new Spark(pWMPort);
    }

    public void update(){
        lights.set(colorHotPink);
    }
}

/*
Documentation Link: http://www.revrobotics.com/content/docs/REV-11-1105-UM.pdf
*/