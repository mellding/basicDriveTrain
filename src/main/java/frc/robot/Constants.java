package frc.robot;

public class Constants {

    //max peak current of the drive train motors
    public static final int driveMaxPeakCurrent     = 35;
    
    //duration of peak current milliseconcs
    public static final int driveMaxPeakCurrentTime = 500;

    //max continuous current
    public static final int driveMaxConinuousCurrent = 15;

    //Deadzone for Right Control Stick
    public static final double rightStickDeadZone = .125;

    //seconds to ramp from 0 to full throttle
    public static final double driveRampTime = .75;

    //time in milli seconds that the dump valve will remain open
    public static final double dumpTime = .0625;
}
