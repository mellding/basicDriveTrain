package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Relay.Value;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

import com.ctre.phoenix.motorcontrol.ControlMode;
//import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
//import com.ctre.phoenix.motorcontrol.can.TalonSRX;
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
  WPI_TalonSRX talonRotate = new WPI_TalonSRX(15);
  WPI_TalonSRX talonTilt = new WPI_TalonSRX(16);
  WPI_TalonSRX talonRevolver = new WPI_TalonSRX(17);

  int _smoothing = 0;

  DifferentialDrive m_robotDrive = new DifferentialDrive(talonLeft, talonRight);

  double revolverTarget = 0;
  double revolverTargetOld = 0;

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
    talonRotate.configFactoryDefault();
    talonTilt.configFactoryDefault();
    talonRevolver.configFactoryDefault();
  
    //Configure The two following controllers
    talonRight_follower.follow(talonRight);
    talonLeft_follower.follow(talonLeft);

    //Configure Current limiting options on the Talons
    talonRight.configPeakCurrentLimit(0); talonRight.configContinuousCurrentLimit(Constants.driveMaxConinuousCurrent);
    talonRight_follower.configPeakCurrentLimit(0); talonRight_follower.configContinuousCurrentLimit(Constants.driveMaxConinuousCurrent);
    talonLeft.configPeakCurrentLimit(0); talonLeft.configContinuousCurrentLimit(Constants.driveMaxConinuousCurrent);
    talonLeft_follower.configPeakCurrentLimit(0); talonLeft_follower.configContinuousCurrentLimit(Constants.driveMaxConinuousCurrent);
    talonRotate.configPeakCurrentLimit(0); talonRotate.configContinuousCurrentLimit(Constants.turretMaxCurrent);
    talonTilt.configPeakCurrentLimit(0); talonTilt.configContinuousCurrentLimit(Constants.turretMaxCurrent);

    //Enable current limiting functions on the Talons
    talonRight.enableCurrentLimit(true);
    talonRight_follower.enableCurrentLimit(true);
    talonLeft.enableCurrentLimit(true);
    talonLeft_follower.enableCurrentLimit(true);
    talonRotate.enableCurrentLimit(true);
    talonTilt.enableCurrentLimit(true);

    talonRight.configOpenloopRamp(Constants.driveRampTime);
    talonLeft.configOpenloopRamp(Constants.driveRampTime);

    //configure the Talons with Encoders
    talonRevolver.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
    talonRevolver.setSensorPhase(true);
    talonRevolver.setInverted(false);
    talonRevolver.config_kP(0, Constants.revolvekP);
    talonRevolver.config_kI(0, Constants.revolvekI);
    talonRevolver.configMotionCruiseVelocity(Constants.revolveVel);
    talonRevolver.configMotionAcceleration(Constants.revolveAccel);
    talonRevolver.setSelectedSensorPosition(0);

    talonRevolver.set(ControlMode.MotionMagic, revolverTarget);

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
  public void teleopInit() {

    talonRevolver.setSelectedSensorPosition(0);
    revolverTarget = 0;

  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {

    talonRevolver.set(ControlMode.MotionMagic, revolverTarget);

    //if(SmartDashboard.getBoolean("Enable Drive", false)){
      if( control00.getY(GenericHID.Hand.kRight) < Constants.rightStickDeadZone){
        m_robotDrive.arcadeDrive(-1 * control00.getY(GenericHID.Hand.kRight), control00.getX(GenericHID.Hand.kRight));
      }
      else if(control00.getY(GenericHID.Hand.kRight) > Constants.rightStickDeadZone){
        m_robotDrive.arcadeDrive(-1 * control00.getY(GenericHID.Hand.kRight), -1 * control00.getX(GenericHID.Hand.kRight));
      }
    //}
   
    if(control00.getY(GenericHID.Hand.kLeft) > .25 || 
        control00.getY(GenericHID.Hand.kLeft) < -.25){
      talonTilt.set(control00.getY(GenericHID.Hand.kLeft));
    }else{talonTilt.set(0);
      }
    
    talonRotate.set(.5 * control00.getX(GenericHID.Hand.kLeft));


    if( control00.getAButton() && 
        control00.getTriggerAxis(GenericHID.Hand.kRight) > .75 &&
        control00.getAButtonReleased() &&
        talonRevolver.getSelectedSensorPosition() <= revolverTarget + 5 &&
        talonRevolver.getSelectedSensorPosition() >= revolverTarget - 5
        ){
      dumpRelay.set(Value.kForward);
      Timer.delay(Constants.dumpTime);
      dumpRelay.set(Value.kOff);
      Timer.delay(Constants.dumpPauseTime);
      revolverTarget = revolverTarget + Constants.revolveToNext;
      talonRevolver.set(revolverTarget);
    }else{
      dumpRelay.set(Value.kOff);
    }

    if( control00.getBButton() && 
        control00.getTriggerAxis(GenericHID.Hand.kRight) > .75 &&
        control00.getBButtonReleased() &&
        talonRevolver.getSelectedSensorPosition() <= revolverTarget + 5 &&
        talonRevolver.getSelectedSensorPosition() >= revolverTarget - 5
        ){
          revolverTarget = revolverTarget + Constants.revolveToNext;
          talonRevolver.set(revolverTarget);
        }
    
    SmartDashboard.putNumber("Joystick X value", control00.getX(GenericHID.Hand.kRight));
    SmartDashboard.putNumber("Joystick Y value", control00.getY(GenericHID.Hand.kRight));
    SmartDashboard.putNumber("Right Current", talonRight.getSupplyCurrent());
    SmartDashboard.putNumber("Left Current", talonLeft.getSupplyCurrent());
    SmartDashboard.putBoolean("A button", control00.getAButton());
    SmartDashboard.putNumber("Revolver Target", revolverTarget);
    SmartDashboard.putNumber("Revolver Pos", talonRevolver.getSelectedSensorPosition());
    SmartDashboard.putBoolean("Revolver In Pos", revolverTarget == talonRevolver.getSelectedSensorPosition());
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
