package frc.robot;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class Drive {
    private WPI_TalonFX driveMotorLeft1 = new WPI_TalonFX(3);
    private WPI_TalonFX driveMotorRight1 = new WPI_TalonFX(5);

    private WPI_TalonFX driveMotorLeft2 = new WPI_TalonFX(4);
    private WPI_TalonFX driveMotorRight2 = new WPI_TalonFX(6);

    private DifferentialDrive differentialDrive = new DifferentialDrive(driveMotorLeft1, driveMotorRight1);

    private XboxController controller = new XboxController(0);

    //private double driveMotorLeftPosition = driveMotorLeft1.getSelectedSensorPosition(3);
    //private double driveMotorRightPosition = driveMotorRight1.getSelectedSensorPosition(5);
        
    //private double driveMotorLeftPosition2 = driveMotorLeft2.getSelectedSensorPosition(4);
    //private double driveMotorRightPosition2 = driveMotorRight2.getSelectedSensorPosition(6);

    //private double getAverageDistance = (driveMotorLeftPosition + driveMotorRightPosition) /2;

    private double topSpeed = 0.5; //Percent value for drive motor speed from 0 to 1
    private double creepSpeed = 1;
    private double xStickValue; //Variable to store the value from the Xbox Controller
    private double yStickValue; //Variable to store the value from the Xbox Controller
    
    private AHRS gyro;

    public Drive(){
        driveMotorLeft2.follow(driveMotorLeft1);
        driveMotorRight2.follow(driveMotorRight1);
       
        driveMotorLeft1.setNeutralMode(NeutralMode.Brake);
        driveMotorRight1.setNeutralMode(NeutralMode.Brake);
        driveMotorLeft1.configOpenloopRamp(0.75);
        driveMotorRight1.configOpenloopRamp(0.75);

        try {
            /* Communicate w/navX-MXP via the MXP SPI Bus.                                     */
            /* Alternatively:  I2C.Port.kMXP, SerialPort.Port.kMXP or SerialPort.Port.kUSB     */
            /* See http://navx-mxp.kauailabs.com/guidance/selecting-an-interface/ for details. */
            gyro = new AHRS(Port.kMXP); 
        } catch (RuntimeException ex ) {
            DriverStation.reportError("Error instantiating navX-MXP:  " + ex.getMessage(), true);
        }
        gyro.reset();
    }

    /**
     * Main method of driving for Teleop
     */
    public void drive(){
       topSpeed = SmartDashboard.getNumber("Top Speed", 0.5); 
       // Allows for change of speed limit on SmartDashboard for testing/demo 
       
        if(controller.getRawAxis(2) == 1){
            creepSpeed = 0.5;
        } else {
            creepSpeed = 1;
        }

        xStickValue = controller.getRawAxis(4) * topSpeed * creepSpeed;
        yStickValue = -controller.getRawAxis(1) * topSpeed * creepSpeed;
        differentialDrive.arcadeDrive(xStickValue, yStickValue, false);

    }

    public void subclassTurn(double turnValue, double moveValue) {
        differentialDrive.arcadeDrive(moveValue, turnValue);
    }

    double distanceToRev(double in){
        in *= 12;
        in /= 18.84;
        in *= 7.18;
        return in;
    }
      
    double clampDrive(double in,double minval,double maxval) {
        if (in > maxval) {
            return maxval;
        }
        else if (in < minval) {
            return minval;
        }
        else {
            return in;
        }
    }

    double leftEncLast;
    double rightEncLast;
    double gyroLast;
    double revNeed;
    double offset;
    double leftCurrentPos;
    double rightCurrentPos;
    double avgPosition;
    double power;
    final double TURN_K = 0.012;
    double turnCorrection;
    double turnOffset;

    void autoPrep() {
        gyro.reset();
        gyro.zeroYaw();
      
        leftEncLast = driveMotorLeft1.getSelectedSensorPosition();
        rightEncLast = driveMotorRight1.getSelectedSensorPosition();
        gyroLast = gyro.getAngle();
      
        Robot.autoStep++;
      
        //autoTime->Reset();
        //autoTime->Start();
    }

    void autoDrive(double distance){
        leftCurrentPos = driveMotorLeft1.getSelectedSensorPosition() - leftEncLast;
        rightCurrentPos = driveMotorRight1.getSelectedSensorPosition() - rightEncLast;
      
        revNeed = distanceToRev(distance);
      
        avgPosition = (leftCurrentPos - rightCurrentPos) / 2.0;
        if (distance>0) {
          offset = revNeed - avgPosition;
        }
        else if (distance<0) {
          offset = -(revNeed - avgPosition);
        }
      
        power = offset / (revNeed / 18.0);
        power = clampDrive(power,-0.5,0.5);
      
        turnCorrection = (gyro.getAngle() - gyroLast) * TURN_K;
        differentialDrive.arcadeDrive(power, 0);
      
        if (offset < 0.2){
            differentialDrive.arcadeDrive(0,0);
          Robot.autoStep++;
        }
        
      
        /*frc::SmartDashboard::PutNumber("auto power", power);
        frc::SmartDashboard::PutNumber("offset", offset);
        frc::SmartDashboard::PutNumber("rev want", revNeed);
        frc::SmartDashboard::PutNumber("avg position", avgPosition);
        frc::SmartDashboard::PutNumber("left pos", leftCurrentPos);
        frc::SmartDashboard::PutNumber("right pos", rightCurrentPos);*/
    }

    void autoTurn(double angle) {
        turnCorrection = (angle - gyro.getAngle()) * TURN_K;
        turnCorrection = clampDrive(turnCorrection, -0.4, 0.4);
      
        differentialDrive.arcadeDrive(0, turnCorrection);
      
        turnOffset = angle - gyro.getAngle();
      
        if (turnOffset < 0) {
          turnOffset = -turnOffset;
        }
      
        if (turnOffset < 5) {
            differentialDrive.arcadeDrive(0,0);
          Robot.autoStep++;
        }
      
        //frc::SmartDashboard::PutNumber("turn offset", turnOffset);
        //frc::SmartDashboard::PutNumber("gyro angle", gyro->GetAngle());
    }

    void zeroDrive() {
        differentialDrive.arcadeDrive(0,0);
    }
}