// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private int m_autoSelected;
  private final SendableChooser<Integer> m_chooser = new SendableChooser<>();

  Drive driveManager = new Drive();
  Intake intakeManager = new Intake();
  Shooter shooterManager = new Shooter();
  Indexer indexerManager = new Indexer();
  Climber climberManager = new Climber();
  Vision visionManager = new Vision();

  XboxController controller = new XboxController(0);

  double visionTurn;
  double visionMove;
  
  static int autoStep;

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("2 Ball Shoot", 0);
    m_chooser.addOption("1 Ball Shoot", 1);
    m_chooser.addOption("Move", 2);
    SmartDashboard.putData("Auto choices", m_chooser);


    SmartDashboard.putNumber("Top Speed", 0.5);

    autoStep = 0;
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {}

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
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
      case 0:
        // Put custom auto code here
        break;
      case 1:

        break;
      case 2:

        break;
    }
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {}

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {

    //if x is pressed the robot will auto turn to the vision target with human control of forwards/backwards
    if (controller.getRawButton(3)) {
      visionTurn = visionManager.trackTurn();
      driveManager.subclassTurn(visionTurn, controller.getRawAxis(4) * 0.5);
    }
    else {
      driveManager.drive();
    }

    shooterManager.shooter();
    shooterManager.hoodMotor();

    indexerManager.indexWheel();
    indexerManager.isBallOurs();
    indexerManager.publishAllianceColor();

    intakeManager.intake();

    climberManager.climberControl();

    visionManager.display();
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
