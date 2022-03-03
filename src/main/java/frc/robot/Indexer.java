package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;

public class Indexer {
    private CANSparkMax indexerMotor = new CANSparkMax(20 , MotorType.kBrushless);
    private XboxController controller = new XboxController(1);
    private ColorSensorV3 colorSensor;
    private ColorMatch colorMatcher;
    
    private final Color kRedTarget = new Color(0.596, 0.308, 0.096);
    private final Color kBlueTarget = new Color(0.221, 0.435, 0.344);

    public Indexer() {
        colorSensor = new ColorSensorV3(I2C.Port.kOnboard);
        colorMatcher.addColorMatch(kRedTarget);
        colorMatcher.addColorMatch(kBlueTarget);
    }

    public void indexWheel() {
        if (controller.getPOV() == 0) {
          indexerMotor.set(-0.75);
        } 
        else if (controller.getPOV() == 180) {
          indexerMotor.set(0.5);
        }
        else {
         indexerMotor.set(0);
        }
    }

    public String publishAllianceColor(){
        String myAlliance = "Invalid";
        if(DriverStation.getAlliance() == Alliance.Red){
           myAlliance = "Red";
        } 
        else if (DriverStation.getAlliance() == Alliance.Blue) {
           myAlliance = "Blue";
        } 
        else if (DriverStation.getAlliance() == Alliance.Invalid) {
           myAlliance = "Invalid";
        }
         
       
        SmartDashboard.putString("My Alliance", myAlliance);
       
       return myAlliance;
    }

    public void isBallOurs() {
        Color detectedColor = colorSensor.getColor();
        ColorMatchResult matchedColor = colorMatcher.matchClosestColor(detectedColor);

        if ((publishAllianceColor() == "Red") && (matchedColor.color == kRedTarget)){
          SmartDashboard.putBoolean("Is Ball Ours", true);
        } 
        else if ((publishAllianceColor() == "Blue") && (matchedColor.color == kBlueTarget)) {
          SmartDashboard.putBoolean("Is Ball Ours", true);
        } 
        else { 
            SmartDashboard.putBoolean("Is Ball Ours", false);
        }
      
    }
}
