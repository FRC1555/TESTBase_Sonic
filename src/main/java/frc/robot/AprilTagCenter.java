package frc.robot;

import frc.robot.subsystems.DriveSubsystem;

public class AprilTagCenter {
    private static final double TARGET_DISTANCE_INCHES = 120.0;
    private static final double SPEED_KP = 0.03; // Proportional control for speed
    private static final double TURN_KP = 0.05; // Proportional control for turning
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
        double currentDistance = Math.abs(botPose[2]) * 39.37; // Changes metres to inches
        double distanceError = currentDistance - TARGET_DISTANCE_INCHES;
        double speedAdjustment = SPEED_KP * distanceError * 0.1; // Reduce speed to 2%
    
        if (Math.abs(distanceError) < SPEED_DEADBAND) {
            speedAdjustment = 0; // Avoid oscillation
        }
    
        double turnOffset = limelight.getXOffset();
        double xOffset = botPose[0] * 39.37; // Convert meters to inches for strafe
        double turnAdjustment = TURN_KP * turnOffset * 0.2; // Reduce turn speed to 20%
        double strafeAdjustment = SPEED_KP * xOffset * 0.1; // Adjust strafe movement
    
        drive.drive(speedAdjustment, strafeAdjustment, turnAdjustment, false);
        System.out.println("distanceError: " + distanceError);
        System.out.println("currentDistance: " + currentDistance);
        System.out.println("xOffset: " + xOffset);
    }
    
}    