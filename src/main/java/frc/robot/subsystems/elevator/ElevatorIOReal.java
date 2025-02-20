package frc.robot.subsystems.elevator;

import frc.robot.generic.elevators.GenericElevatorSystemIOSparkMax;

public class ElevatorIOReal extends GenericElevatorSystemIOSparkMax implements ElevatorIO {
    public ElevatorIOReal() {
        super(
                new int[]{ElevatorConstants.LEFT_ELEVATOR,
                        ElevatorConstants.RIGHT_ELEVATOR},
                40,
                0.0,
                false,
                true,
                0.0,
                ElevatorConstants.kP,
                ElevatorConstants.kI,
                ElevatorConstants.kD);
    }
}
