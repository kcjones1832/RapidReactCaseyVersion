package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.XboxController;

public class Intake {
    private CANSparkMax intakeMotor = new CANSparkMax(8, MotorType.kBrushless);
    private XboxController controller = new XboxController(1);
    private CANSparkMax intakeLiftMotor = new CANSparkMax(10, MotorType.kBrushless);

    
    public void intake() {
        if(controller.getRawButton(5)){
            intakeMotor.set(0.85);
        }
        else if(controller.getRawButton(6)) {
            intakeMotor.set(-0.85);
    
        }else{
            intakeMotor.set(0);
        }

        if(controller.getRawButton(1) && intakeLiftMotor.getOutputCurrent() < 15){
            intakeLiftMotor.set(-0.3);
        }
        else if(controller.getRawButton(2) && intakeLiftMotor.getOutputCurrent() < 15) {
            intakeLiftMotor.set(0.3);
    
        }else{
            intakeLiftMotor.set(0);
        }
    }
}
