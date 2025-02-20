package frc.robot;

import static frc.robot.GlobalConstants.ROBOT;

import frc.robot.OI.*;

public final class Config {

  public static final class Subsystems {
    public static final boolean DRIVETRAIN_ENABLED = true;
    public static final boolean VISION_ENABLED = false;
    public static final boolean GAME_PIECE_VISION_ENABLED = false;
    public static final boolean INTAKE_ENABLED = false;
    public static final boolean FEEDER_ENABLED = false;
    public static final boolean PIVOT_ENABLED = false;
    public static final boolean SHOOTER_ENABLED = false;
    public static final boolean CLIMBER_ENABLED = false;
    public static final boolean LEDS_ENABLED = false;
    public static final boolean ELEVATOR_ENABLED = false;

    public static final boolean PROTOTYPES_ENABLED = false;
  }

  public static final class Controllers {

    public static final boolean DRIVER_ENALBED = true;
    public static final int DRIVER_PORT = 0;

    public static final boolean OPERATOR_ENABLED = false;
    public static final int OPERATOR_PORT = 1;
    public static final boolean JOYSTICK_OPERATOR_ENABLED = false;
    public static final boolean BOARD_OPERATOR_ENABLED = !JOYSTICK_OPERATOR_ENABLED;

    public static DriverMap getDriverController() {
      return DRIVER_ENALBED
          ? switch (ROBOT) {
            case COMPBOT -> new XboxDriverMap(DRIVER_PORT);
            case DEVBOT -> new XboxDriverMap(DRIVER_PORT);
            case SIMBOT -> new SimXboxUniversalMap(DRIVER_PORT);
          }
          : null;
    }

    public static OperatorMap getOperatorController() {
      return OPERATOR_ENABLED
          ? switch (ROBOT) {
            case COMPBOT -> JOYSTICK_OPERATOR_ENABLED
                ? new XboxOperatorMap(OPERATOR_PORT)
                : new BoardOperatorMap(OPERATOR_PORT);
            case DEVBOT -> JOYSTICK_OPERATOR_ENABLED
                ? new XboxOperatorMap(OPERATOR_PORT)
                : new BoardOperatorMap(OPERATOR_PORT);
            case SIMBOT -> new SimXboxUniversalMap(OPERATOR_PORT);
          }
          : null;
    }
  }
}
