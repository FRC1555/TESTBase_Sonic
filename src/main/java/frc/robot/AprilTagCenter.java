package frc.robot;

import frc.robot.subsystems.DriveSubsystem;

public class AprilTagCenter {
    private static final double TARGET_DISTANCE_INCHES = 48.0;
    private static final double SPEED_KP = 0.05; // Proportional control for speed
    private static final double TURN_KP = 0.01; // Proportional control for turning
    private static final double SPEED_DEADBAND = 0.05;
    
    private final DriveSubsystem drive;
    private final LimelightInterface limelight;

    public AprilTagCenter(DriveSubsystem drive, LimelightInterface limelight) {
        this.drive = drive;
        this.limelight = limelight;
    }

    public void keepDistanceFromAprilTag() {
        if (!limelight.hasValidTargets()) {
            drive.stop();
            return;
        }
    
        double[] botPose = limelight.getBotPose();
        double currentDistance = botPose[2] * 39.37; // Convert meters to inches
        double distanceError = TARGET_DISTANCE_INCHES - currentDistance;
        double speedAdjustment = SPEED_KP * distanceError * 0.02; // Reduce speed to 2%
    
        if (Math.abs(distanceError) < SPEED_DEADBAND) {
            speedAdjustment = 0; // Avoid oscillation
        }
    
        double xOffset = limelight.getXOffset();
        double strafeAdjustment = SPEED_KP * xOffset * 0.02; // Adjust strafe movement
    
        double yOffset = botPose[0] * 39.37; // Convert meters to inches for strafe
        double turnAdjustment = TURN_KP * yOffset * 0.2; // Reduce turn speed to 20%
    
        drive.drive(speedAdjustment, strafeAdjustment, turnAdjustment, false);
    }
    
}    