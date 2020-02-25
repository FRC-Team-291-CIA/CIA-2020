package frc.sensors;

import com.revrobotics.ColorSensorV3;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorMatch;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;

public class CIA_ColorSensor {
    private ColorSensorV3 sensor;
    private I2C.Port i2CPort = I2C.Port.kOnboard;
    private Color blueTarget, greenTarget, redTarget, yellowTarget, detectedColor;
    private ColorMatch colorMatcher;
    private ColorMatchResult match;
    private String colorString = "Not_Used";

    public CIA_ColorSensor(){
        sensor = new ColorSensorV3(i2CPort);
        colorMatcher = new ColorMatch();

        blueTarget = ColorMatch.makeColor(0.143, 0.427, 0.429);
        greenTarget = ColorMatch.makeColor(0.197, 0.561, 0.240);
        redTarget = ColorMatch.makeColor(0.561, 0.232, 0.114);
        yellowTarget = ColorMatch.makeColor(0.361, 0.524, 0.113);

        colorMatcher.addColorMatch(blueTarget);
        colorMatcher.addColorMatch(greenTarget);
        colorMatcher.addColorMatch(redTarget);
        colorMatcher.addColorMatch(yellowTarget);   
    }

    public String getColor(){
        this.detectColor();

        return colorString;
    }

    private void detectColor(){
        detectedColor = sensor.getColor();

        match = colorMatcher.matchClosestColor(detectedColor);
    
        if (match.color == blueTarget) {
          colorString = "Blue";
        } else if (match.color == redTarget) {
          colorString = "Red";
        } else if (match.color == greenTarget) {
          colorString = "Green";
        } else if (match.color == yellowTarget) {
          colorString = "Yellow";
        } else {
          colorString = "Unknown";
        }
    }

    public void update(){
      this.detectColor();
      SmartDashboard.putNumber("Red", detectedColor.red);
      SmartDashboard.putNumber("Green", detectedColor.green);
      SmartDashboard.putNumber("Blue", detectedColor.blue);
      SmartDashboard.putNumber("Confidence", match.confidence);
      SmartDashboard.putString("Detected Color", colorString);
    }   
}