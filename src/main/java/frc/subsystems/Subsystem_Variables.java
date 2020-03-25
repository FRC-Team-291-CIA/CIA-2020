/* 
File Name: Subsystem_Variables.java
Use: This is used as a "package" variables
Reuse: This file is intended to be reused
Reuse Tips: This us originally intended to be used to communicate between
            subsystems. This allows the bypass of using Robot.java
Files Directly Used / Is In: CIA_DriveBase.java and CIA_Climber.java
Sensors Used: N/A
*/

package frc.subsystems;

public class Subsystem_Variables {
    //The variable below allows the climber to toggle the low gear on the drive base
    public static boolean isOnlyLowGear = false;
}