package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXFeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.AnalogInput;

public class Shooter {
    private WPI_TalonFX shooterMotor = new WPI_TalonFX(7);
    private XboxController controller = new XboxController(1);
    //TalonFXConfiguration configs = new TalonFXConfiguration();
  
    private double velocityWant;
    private double velocityRPM;
    
    private AnalogInput input = new AnalogInput(0);
    private AnalogPotentiometer hoodPotentiometer = new AnalogPotentiometer(input);
    private VictorSPX hoodMotor = new VictorSPX(11);
    
  
    //private PIDController shooterController = new PIDController(0, 0, 0);
    //public void configSelectedFeedbackSensor(TalonFXFeedbackDevice integratedsensor, int i, int j) {}
  
    public Shooter() {
      shooterMotor.configSelectedFeedbackSensor(TalonFXFeedbackDevice.IntegratedSensor, 0, 10);
      //shooterMotor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, 10;);
      shooterMotor.setSensorPhase(false);
      shooterMotor.setInverted(true);
      shooterMotor.config_kP(0, 0.7, 10);
      shooterMotor.config_kI(0, 0.0001, 10);
      shooterMotor.config_kD(0, 20, 10);
      shooterMotor.setNeutralMode(NeutralMode.Coast);
      shooterMotor.configPeakOutputForward(0, 10);
      shooterMotor.configClosedloopRamp(1, 10);

      input.setAverageBits(2);
    }

    public void shooter(){
        velocityRPM = -3200;
        velocityWant = velocityRPM * 2048 / 600;
        //SmartDashboard.getNumber("shoot position", 12000);
        SmartDashboard.putNumber("velocity", -shooterMotor.getSelectedSensorVelocity());
        SmartDashboard.putNumber("velocityWant", -velocityWant);
  
         //double shooterMotorVelocityRPM = -shooterMotor.getSelectedSensorVelocity() / 2048 * 600;
      
        if(controller.getRawButton(3)){
          //shooterMotor.set(TalonFXControlMode.PercentOutput, -0.7);
          shooterMotor.set(ControlMode.Velocity, velocityWant);
          //shooterMotor.set(shooterController.calculate(shooterMotor.getSelectedSensorVelocity(), -8000));
          //System.out.print("shooter velocity " + shooterMotor.getSelectedSensorVelocity() + " velocityWant " + velocityWant + "\n");
    
         //shooterMotor.set(0.8);
        } else {
          shooterMotor.set(0);
        }
    }

    public void hoodMotor() {
        if (controller.getRawAxis(2) > 0) {
          hoodMotor.set(ControlMode.PercentOutput, 0.3);
        } 
        else if (controller.getRawAxis(3) > 0) {
          hoodMotor.set(ControlMode.PercentOutput, -0.3);
        } 
        else {
          hoodMotor.set(ControlMode.PercentOutput, 0);
        }

        if (controller.getRawButton(4) && hoodPotentiometer.get() < 0.4) {
            hoodMotor.set(ControlMode.PercentOutput, -0.4);
        }

        SmartDashboard.putNumber("Hood Angle", hoodPotentiometer.get());
    }
}
