package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.GenericHID;

public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  //Create Xbox controller
  XboxController control00 = new XboxController(0);

  //Create Talon SRX motor Controllers
  WPI_TalonSRX talonRight = new WPI_TalonSRX(0);
  WPI_TalonSRX talonRight_follower = new WPI_TalonSRX(1);
  WPI_TalonSRX talonLeft = new WPI_TalonSRX(2);
  WPI_TalonSRX talonLeft_follower = new WPI_TalonSRX(3);

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
    talonRight.configPeakCurrentLimit(Constants.driveMaxPeakCurrent, Constants.driveMaxPeakCurrentTime);
    talonRight.configContinuousCurrentLimit(Constants.driveMaxConinuousCurrent);
    talonRight_follower.configPeakCurrentLimit(Constants.driveMaxPeakCurrent, Constants.driveMaxPeakCurrentTime);
    talonRight_follower.configContinuousCurrentLimit(Constants.driveMaxConinuousCurrent);
    talonLeft.configPeakCurrentLimit(Constants.driveMaxPeakCurrent, Constants.driveMaxPeakCurrentTime);
    talonLeft.configContinuousCurrentLimit(Constants.driveMaxConinuousCurrent);
    talonLeft_follower.configPeakCurrentLimit(Constants.driveMaxPeakCurrent, Constants.driveMaxPeakCurrentTime);
    talonLeft_follower.configContinuousCurrentLimit(Constants.driveMaxConinuousCurrent);

    //Enable current limiting functions on the Drive Talons
    talonRight.enableCurrentLimit(true);
    talonRight_follower.enableCurrentLimit(true);
    talonLeft.enableCurrentLimit(true);
    talonLeft_follower.enableCurrentLimit(true);
  }

  @Override
  public void robotPeriodic() {}

  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
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
    m_robotDrive.arcadeDrive(control00.getY(GenericHID.Hand.kLeft), control00.getX(GenericHID.Hand.kRight));
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
