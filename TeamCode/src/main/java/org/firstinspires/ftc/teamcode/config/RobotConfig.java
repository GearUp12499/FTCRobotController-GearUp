package org.firstinspires.ftc.teamcode.config;

import com.qualcomm.robotcore.hardware.PIDFCoefficients;

public interface RobotConfig {
    double getTicksPerRev();
    double getMaxRPM();
    boolean getRunUsingEncoder();
    PIDFCoefficients getModerVeloPID();
    double getWheelRadius();
    double getGearRatio();
    double getTrackWidth();
    double getKV();
    double getKA();
    double getKStatic();
    double getMaxVel();
    double getMaxAccel();
    double getMaxAngVel();
    double getMaxAngAccel();

    double getEncoderTicksPerRev();
    double getEncoderWheelRadius();
    double getEncoderGearRatio();
    double getEncoderLateralDistance();
    double getEncoderForwardOffset();

    default double getXMultiplier() { return 1; }
    default double getYMultiplier() { return 1; }
    // TODO add motor direction configuration
    // TODO add hardware configuration (motors + odometry)

    default double rpmToVelocity(double rpm) {
        return rpm * getGearRatio() * 2 * Math.PI * getWheelRadius() / 60.0;
    }

    default double getMotorVelocityF(double ticksPerSecond) {
        // see https://docs.google.com/document/d/1tyWrXDfMidwYyP_5H4mZyVgaEswhOC35gvdmP-V-5hA/edit#heading=h.61g9ixenznbx
        return 32767 / ticksPerSecond;
    }
}