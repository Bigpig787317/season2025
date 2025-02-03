package frc.robot.subsystems.feeder;

import org.littletonrobotics.junction.AutoLog;

public interface FeederIO {
    @AutoLog
    public class feederIOInputs {
        public double speed;
    }
    default void setSpeed(double speed) {}

    default void updateInputs(feederIOInputs IO) {}

    default void changeVoltage(double voltage){}

}
