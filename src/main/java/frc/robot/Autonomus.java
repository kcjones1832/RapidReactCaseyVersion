package frc.robot;

import edu.wpi.first.wpilibj.Timer;

public class Autonomus {

    Drive drive;
    Shooter shooter;
    int autoStep;
    Timer autoTime;

    public Autonomus(Drive drive) {
        this.drive = drive;

        autoTime = new Timer();
        autoTime.reset();
        
    }


    
}
