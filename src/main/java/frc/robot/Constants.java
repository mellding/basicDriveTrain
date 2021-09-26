package frc.robot;

public class Constants {

    //max peak current of the drive train motors
    public static final int driveMaxPeakCurrent     = 35;
    
    //duration of peak current milliseconcs
    public static final int driveMaxPeakCurrentTime = 500;

    //max continuous currents
    public static final int driveMaxConinuousCurrent = 15;
    public static final int turretMaxCurrent = 2;

    //Deadzone for Right Control Stick
    public static final double rightStickDeadZone = .2;

    //seconds to ramp from 0 to full throttle
    public static final double driveRampTime = 1;

    //time in milli seconds that the dump valve will remain open
    public static final double dumpTime = .05;
    public static final double dumpPauseTime = .125;

    //PID constants
    public static final double revolvekP = 200;
    public static final double revolvekI = 0.5;

    public static final double revolveAccel = 150; public static final double revolveVel = 1000;

    //Revolver Rotation per shot
    public static final double revolveToNext = 332;
    public static final int kTimeoutMs = 30;
    public static final int kPIDLoopIdx = 0;
    public static final int kSlotIdx = 0;

}
