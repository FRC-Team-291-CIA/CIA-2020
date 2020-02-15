package frc.sensors;

import com.revrobotics.ColorSensorV3;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.util.Color;

public class CIA_ColorSensor {
    private ColorSensorV3 sensor;
    private I2C.Port i2CPort = I2C.Port.kOnboard;
    private Color color;
    private double sensitivity;

    public CIA_ColorSensor(double newSensitivity){
        sensor = new ColorSensorV3(i2CPort);

        sensitivity = newSensitivity;
    }

    private void setColor(){
        color = sensor.getColor();
    }

    /*
    The method below takes in the color from the sensors and converts it to an array of RGB where one is true
    and zero is false which is used to read which color it is.
    */
    public void getCalculatedColor(){
        this.setColor();

        System.out.println("Red: " + color.red*255);
        System.out.println("Green: " + color.green*255);
        System.out.println("Blue: " + color.blue*255);
    }

    private boolean isYellow(double R, double G, double B){
        return false;
    }

    private boolean isRed(double R, double G, double B){
        return false;
    }

    private boolean isGreen(double R, double G, double B){
        return false;
    }

    private boolean isCyan(double R, double G, double B){
        return false;
    }
}

/*
Below are CMYK Colors From The Game Manual to RGB Calculated:
Blue-----(100, 0, 0, 0) ===== (0, 255, 255)
Green----(100, 0, 100, 0) === (0, 255, 0)
Red------(0, 100, 100, 0) === (255, 0, 0)
Yellow---(0, 0, 100, 0) ===== (255, 255, 0)
*/