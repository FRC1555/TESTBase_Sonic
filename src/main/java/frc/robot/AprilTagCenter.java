package frc.robot;

import frc.robot.subsystems.DriveSubsystem;

public class AprilTagCenter {
    
    private LimelightInterface limelight;
    private DriveSubsystem swerveDrive; // Assuming you have a swerve drive subsystem
    
    private static final double TARGET_DISTANCE_INCHES = 36.0;
    private static final double APRILTAG_PHYSICAL_WIDTH = 6.5; // inches (replace with actual Apriltag size)
    
    public AprilTagCenter(LimelightInterface limelight, DriveSubsystem swerveDrive) {
        this.limelight = limelight;
        this.swerveDrive = swerveDrive;
    }

    public void keepDistanceFromAprilTag() {
        if (limelight.hasValidTargets()) {
            double targetArea = limelight.getTargetArea(); // Get the area of the target seen by Limelight
            double currentDistance = calculateDistanceFromTarget(targetArea); // Estimate distance from area
            
            double speedAdjustment = calculateSpeedAdjustment(currentDistance);
            double turnAdjustment = limelight.getXOffset() / 27; // Assuming a simple proportional controller for turn

            // Apply adjustments to the swerve drive system
            swerveDrive.drive(speedAdjustment, 0, turnAdjustment); // Only use X speed (forward/back) and rotation
        } else {
            // No valid target, stop robot or maintain position
            swerveDrive.stop();
        }
    }

    private double calculateDistanceFromTarget(double targetArea) {
        // Estimate the distance based on the target area using a proportional relationship
        // A simple way to estimate: distance = k / sqrt(area), where k is a constant (tune this value)
        double k = 50.0; // Tune this constant based on experimental results
        return k / Math.sqrt(targetArea);
    }

    private double calculateSpeedAdjustment(double currentDistance) {
        // We want the robot to stay at 36 inches, so calculate the error
        double error = currentDistance - TARGET_DISTANCE_INCHES;
        
        // A simple proportional controller to adjust the speed
        double kP = 0.1; // Tune this gain value to control how fast the robot adjusts
        return -kP * error; // Negative because we want to correct the distance
    }
}
