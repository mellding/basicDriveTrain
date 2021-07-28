package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Relay.Value;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Relay;

public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  //Create Xbox controller
  XboxController control00 = new XboxController(0);

  //Create the relay for the dump valve
  Relay dumpRelay = new Relay(0);

  //Create Talon SRX motor Controllers
  WPI_TalonSRX talonRight = new WPI_TalonSRX(11);
  WPI_TalonSRX talonRight_follower = new WPI_TalonSRX(12);
  WPI_TalonSRX talonLeft = new WPI_TalonSRX(13);
  WPI_TalonSRX talonLeft_follower = new WPI_TalonSRX(14);

  DifferentialDrive m_robotDrive = new DifferentialDrive(talonLeft, talonRight);

  /**
   * This function is run when the robot is first started up and should be used for any initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    //Reset all the talons to defaul settings
    talonRight.configFactoryDefault();
    talonRight_follower.configFactoryDefault();
    talonLeft.configFactoryDefault();
    talonLeft_follower.configFactoryDefault();
  
    //Configure The two following controllers
    talonRight_follower.follow(talonRight);
    talonLeft_follower.follow(talonLeft);

    //Configure Current limiting options on the Drive Talons
    talonRight.configPeakCurrentLimit(0); talonRight.configContinuousCurrentLimit(Constants.driveMaxConinuousCurrent);
    talonRight_follower.configPeakCurrentLimit(0); talonRight_follower.configContinuousCurrentLimit(Constants.driveMaxConinuousCurrent);
    talonLeft.configPeakCurrentLimit(0); talonLeft.configContinuousCurrentLimit(Constants.driveMaxConinuousCurrent);
    talonLeft_follower.configPeakCurrentLimit(0); talonLeft_follower.configContinuousCurrentLimit(Constants.driveMaxConinuousCurrent);

    //Enable current limiting functions on the Drive Talons
    talonRight.enableCurrentLimit(true);
    talonRight_follower.enableCurrentLimit(true);
    talonLeft.enableCurrentLimit(true);
    talonLeft_follower.enableCurrentLimit(true);

    talonRight.configOpenloopRamp(Constants.driveRampTime);
    talonLeft.configOpenloopRamp(Constants.driveRampTime);

    dumpRelay.set(Value.kOff);
  }

  @Override
  public void robotPeriodic() {}

  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {}

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    if( control00.getY(GenericHID.Hand.kRight) < Constants.rightStickDeadZone){
      m_robotDrive.arcadeDrive(-1 * control00.getY(GenericHID.Hand.kRight), control00.getX(GenericHID.Hand.kRight));
    }
    else if(control00.getY(GenericHID.Hand.kRight) > Constants.rightStickDeadZone){
      m_robotDrive.arcadeDrive(-1 * control00.getY(GenericHID.Hand.kRight), -1 * control00.getX(GenericHID.Hand.kRight));
    }
   
    if( control00.getAButton() && 
        control00.getTriggerAxis(GenericHID.Hand.kRight) > .75 &&
        control00.getAButtonReleased()){
      dumpRelay.set(Value.kForward);
      Timer.delay(Constants.dumpTime);
      dumpRelay.set(Value.kOff);
    }else{
      dumpRelay.set(Value.kOff);
    }

    
    SmartDashboard.putNumber("Joystick X value", control00.getX(GenericHID.Hand.kRight));
    SmartDashboard.putNumber("Joystick Y value", control00.getY(GenericHID.Hand.kRight));
    SmartDashboard.putNumber("Right Current", talonRight.getSupplyCurrent());
    SmartDashboard.putNumber("Left Current", talonLeft.getSupplyCurrent());
    SmartDashboard.putBoolean("A button", control00.getAButton());
  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}
}
