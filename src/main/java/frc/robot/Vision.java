package frc.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Vision {
    private double targetOffsetAngle_Horizontal;
    private double targetOffsetAngle_Vertical;
    private double targetArea;
    private double targetSkew;
    private double tv; //has target

    final private double limelightMountAngleDegrees = 0.0; //mount angle from vertical
    final private double limelightLensHeightInches = 0.0; //distance (in) from center of limelight lenses to floor
    final private double goalHeightInches = 104; //height of upper hub

    private double angleToGoalDegrees;
    private double angleToGoalRadians;

    private double distanceFromLimelightToGoalInches;

    private double turnOutput;
    private final double MAX_STEER = 0.3;
    private final double STEER_K = 0.075;

    public void display() { 
        NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
        NetworkTableEntry tx = table.getEntry("tx");
        NetworkTableEntry ty = table.getEntry("ty");
        NetworkTableEntry ta = table.getEntry("ta");
        NetworkTableEntry ts = table.getEntry("ts");

        targetOffsetAngle_Horizontal = tx.getDouble(0.0);
        targetOffsetAngle_Vertical = ty.getDouble(0.0);
        targetArea = ta.getDouble(0.0);
        targetSkew = ts.getDouble(0.0);

        SmartDashboard.putNumber("tx", targetOffsetAngle_Horizontal);
        SmartDashboard.putNumber("ty", targetOffsetAngle_Vertical);
        SmartDashboard.putNumber("ta", targetArea);
        SmartDashboard.putNumber("ts", targetSkew);

        table.getEntry("pipeline").setNumber(3);
        //table->PutNumber("pipeline", 3);

        //turns on/off limelight led's and switches from targeting to camera mode
        /*if (stick.getRawButtonPressed(4)) {
            imageSwitch = !imageSwitch;
        }

        if (imageSwitch) {
            table.getEntry("camMode").setNumber(0);
            table.getEntry("ledMode").setNumber(3);
        }
        else {
            table.getEntry("camMode").setNumber(1);
            table.getEntry("ledMode").setNumber(1);
        }*/

    }

    public double calculateDistance() {
        NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
        NetworkTableEntry tx = table.getEntry("tx");
        NetworkTableEntry ty = table.getEntry("ty");
        NetworkTableEntry ta = table.getEntry("ta");
        NetworkTableEntry ts = table.getEntry("ts");

        targetOffsetAngle_Horizontal = tx.getDouble(0.0);
        targetOffsetAngle_Vertical = ty.getDouble(0.0);
        targetArea = ta.getDouble(0.0);
        targetSkew = ts.getDouble(0.0);

        angleToGoalDegrees = limelightMountAngleDegrees + targetOffsetAngle_Vertical;
        angleToGoalRadians = angleToGoalDegrees * (Math.PI / 180);

        distanceFromLimelightToGoalInches = (goalHeightInches - limelightLensHeightInches) / Math.tan(angleToGoalRadians);

        return distanceFromLimelightToGoalInches;
    }

    /**
     * forces an imput to be within the provided boundaries
     * 
     * @param in the inputted value
     * @param minval the minimum
     * @param maxval the maximum
     * @return a value between max and min
     */
    private double clamp(double in, double minval, double maxval) {
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

    public double trackTurn() {
        NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
        targetOffsetAngle_Horizontal = table.getEntry("tx").getDouble(0.0);
        tv = table.getEntry("tv").getDouble(0.0);

        if (tv == 1) {
            turnOutput = targetOffsetAngle_Horizontal * STEER_K; //or divid by max value (27 degrees)
            turnOutput = clamp(turnOutput,-MAX_STEER,MAX_STEER);
            return turnOutput;
        }
        else {
            return 0;
        }
    }
}
