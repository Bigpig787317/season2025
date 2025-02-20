package frc.robot.subsystems.pivot;

import static frc.robot.GlobalConstants.ROBOT;

import edu.wpi.first.math.trajectory.TrapezoidProfile;

public final class PivotConstants {

  private PivotConstants() {
    System.out.println("What are you trying to do?");
  }

  public static final class Software {

    public static final Gains gains =
        switch (ROBOT) {
          case COMPBOT -> new Gains(3, 0, 0.05, 0, 0.5, 1.3);
          case DEVBOT -> new Gains(0, 0, 0, 0, 0, 0);
          case SIMBOT -> new Gains(0, 0, 0, 0, 0, 0);
        };
    public static final double kIZone = 0.08;
    public static final double POSITION_TOLERANCE = 0.05;
    public static final double VELOCITY_TOLERANCE = 0.1;
    public static final int LEFT_PIVOT_ID = 31;
    public static final int RIGHT_PIVOT_ID = 32;
    public static final int ENCODER_PORT = 2;
    public static TrapezoidProfile.Constraints profileConstraints =
        new TrapezoidProfile.Constraints(200, 1);

    public record Gains(double kP, double kI, double kD, double kS, double kV, double kG) {}
  }

  public static final class Hardware {
    public static final double MAX_ANGLE_RAD = 1;
    public static final double ABSOLUTE_ENCODER_OFFSET = 0.38;
    public static final double RESTING_ANGLE = 0.45;
  }
}
