package frc.robot;

public class Constants {

    //max peak current of the drive train motors
    public static final int driveMaxPeakCurrent     = 35;
    
    //duration of peak current milliseconcs
    public static final int driveMaxPeakCurrentTime = 500;

    //max continuous currents
    public static final int driveMaxConinuousCurrent = 15;
    public static final int turretMaxCurrent = 5;

    //Deadzone for Right Control Stick
    public static final double rightStickDeadZone = .1875;

    //seconds to ramp from 0 to full throttle
    public static final double driveRampTime = .75;

    //time in milli seconds that the dump valve will remain open
    public static final double dumpTime = .0625;

    //PID constants
    public static final double rotatekP = 1;
    public static final double tiltkP = 1;
    public static final double revolvekP = 1;

    public static final double rotateAccel = 100; public static final double rotateVel = 100;
    public static final double tiltAccel = 100; public static final double tiltVel = 100;
    public static final double revolveAccel = 100; public static final double revolveVel = 100;

    //Revolver Rotation per shot
    public static final double revolveToNext = 100;

}
