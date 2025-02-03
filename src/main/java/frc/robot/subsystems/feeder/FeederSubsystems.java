package frc.robot.subsystems.feeder;

import edu.wpi.first.math.filter.Debouncer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import java.util.function.DoubleSupplier;

import frc.robot.subsystems.feeder.FeederIO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
public class FeederSubsystems extends SubsystemBase {

    public final FeederIO io;
    public final FeederIOInputsAutoLogged inputs = new FeederIOInputsAutoLogged();

    @RequiredArgsConstructor
    public enum FeederGoal {
        IDLING(() -> 0.0),
        ON(() -> 12),
        REVERSE(() -> -12);

        private final DoubleSupplier heightSupplier;

        public double getHeightSupplier() {
            return heightSupplier.getAsDouble();
        }
    }

    private FeederGoal goal = FeederGoal.IDLING;
    private Debouncer currentDebouncer = new Debouncer(0.25, Debouncer.DebounceType.kFalling);

    public FeederSubsystems(FeederIO io) {
        this.io = io;
    }

    @Override
    public void periodic() {
        io.updateInputs(inputs);
    }
}
