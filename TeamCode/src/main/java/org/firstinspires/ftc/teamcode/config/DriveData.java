package org.firstinspires.ftc.teamcode.config;

import com.qualcomm.robotcore.hardware.PIDFCoefficients;

public class DriveData {
    public static class GoBuildaStraferV5Base implements RobotConfig {
        /*
         * These are motor constants that should be listed online for your motors.
         */
        @Override
        public double getTicksPerRev() {
            return 537.6;
        }

        @Override
        public double getMaxRPM() {
            return 312;
        }

        /*
         * Set RUN_USING_ENCODER to true to enable built-in hub velocity control using drive encoders.
         * Set this flag to false if drive encoders are not present and an alternative localization
         * method is in use (e.g., tracking wheels).
         *
         * If using the built-in motor velocity PID, update MOTOR_VELO_PID with the tuned coefficients
         * from DriveVelocityPIDTuner.
         */
        @Override
        public boolean getRunUsingEncoder() {
            return false;
        }

        @Override
        public PIDFCoefficients getModerVeloPID() {
            return new PIDFCoefficients(0, 0, 0,
                    getMotorVelocityF(getMaxRPM() / 60 * getTicksPerRev()));
        }

        /*
         * These are physical constants that can be determined from your robot (including the track
         * width; it will be tune empirically later although a rough estimate is important). Users are
         * free to chose whichever linear distance unit they would like so long as it is consistently
         * used. The default values were selected with inches in mind. Road runner uses radians for
         * angular distances although most angular parameters are wrapped in Math.toRadians() for
         * convenience. Make sure to exclude any gear ratio included in MOTOR_CONFIG from GEAR_RATIO.
         */
        @Override
        public double getWheelRadius() {
            return 1.88976;
        }

        @Override
        public double getGearRatio() {
            return 1;
        }

        @Override
        public double getTrackWidth() {
            return 16.34;
        }

        /*
         * These are the feedforward parameters used to model the drive motor behavior. If you are using
         * the built-in velocity PID, *these values are fine as is*. However, if you do not have drive
         * motor encoders or have elected not to use them for velocity control, these values should be
         * empirically tuned.
         */
        @Override
        public double getKV() {
            return 1.0 / rpmToVelocity(getMaxRPM());
        }

        @Override
        public double getKA() {
            return 0;
        }

        @Override
        public double getKStatic() {
            return 0;
        }

        /*
         * These values are used to generate the trajectories for you robot. To ensure proper operation,
         * the constraints should never exceed ~80% of the robot's actual capabilities. While Road
         * Runner is designed to enable faster autonomous motion, it is a good idea for testing to start
         * small and gradually increase them later after everything is working. All distance units are
         * inches.
         */
        /*
         * Note from LearnRoadRunner.com:
         * The velocity and acceleration constraints were calculated based on the following equation:
         * ((MAX_RPM / 60) * GEAR_RATIO * WHEEL_RADIUS * 2 * Math.PI) * 0.85
         * Resulting in 52.48180821614297 in/s.
         * This is only 85% of the theoretical maximum velocity of the bot, following the recommendation above.
         * This is capped at 85% because there are a number of variables that will prevent your bot from actually
         * reaching this maximum velocity: voltage dropping over the game, bot weight, general mechanical inefficiencies, etc.
         * However, you can push this higher yourself if you'd like. Perhaps raise it to 90-95% of the theoretically
         * max velocity. The theoretically maximum velocity is 61.74330378369762 in/s.
         * Just make sure that your bot can actually reach this maximum velocity. Path following will be detrimentally
         * affected if it is aiming for a velocity not actually possible.
         *
         * The maximum acceleration is somewhat arbitrary and it is recommended that you tweak this yourself based on
         * actual testing. Just set it at a reasonable value and keep increasing until your path following starts
         * to degrade. As of now, it simply mirrors the velocity, resulting in 52.48180821614297 in/s/s
         *
         * Maximum Angular Velocity is calculated as: maximum velocity / trackWidth * (180 / Math.PI) but capped at 360°/s.
         * You are free to raise this on your own if you would like. It is best determined through experimentation.

         */
        @Override
        public double getMaxVel() {
            return 52.48180821614297;
        }

        @Override
        public double getMaxAccel() {
            return 52.48180821614297;
        }

        @Override
        public double getMaxAngVel() {
            return Math.toRadians(184.02607784577722);
        }

        @Override
        public double getMaxAngAccel() {
            return Math.toRadians(184.02607784577722);
        }

        @Override
        public double getEncoderTicksPerRev() {
            return 8192;
        }

        @Override
        public double getEncoderWheelRadius() {
            return 0.69;
        }

        @Override
        public double getEncoderGearRatio() {
            return 1;
        }

        @Override
        public double getEncoderLateralDistance() {
            return 16.14173f;
        }

        @Override
        public double getEncoderForwardOffset() {
            return 1.375f;
        }

        private GoBuildaStraferV5Base() {

        }
    }
    public static class GoBuildaStraferV5Tuned extends GoBuildaStraferV5Base {
        @Override
        public double getTrackWidth() {
            return 14.34;
        }

        @Override
        public double getXMultiplier() {
            // 89.25
            // 89.19
            // 89
            return 1.009572241;
        }

        @Override
        public double getYMultiplier() {
            // 88.95
            // 89.63
            // 88.72
            return 1.011292769;
        }

        @Override
        public double getKV() {
            return 0.0191960882997653;
        }

        @Override
        public double getKA() {
            return 0.0022;
        }

        @Override
        public double getMaxAngVel() {
            return 8.021820929253712;
        }
    }

    public static class OldBuild implements RobotConfig {
        /*
         * These are motor constants that should be listed online for your motors.
         */
        @Override
        public double getTicksPerRev() {
            return 537.6;
        }

        @Override
        public double getMaxRPM() {
            return 340;
        }

        /*
         * Set RUN_USING_ENCODER to true to enable built-in hub velocity control using drive encoders.
         * Set this flag to false if drive encoders are not present and an alternative localization
         * method is in use (e.g., tracking wheels).
         *
         * If using the built-in motor velocity PID, update MOTOR_VELO_PID with the tuned coefficients
         * from DriveVelocityPIDTuner.
         */
        @Override
        public boolean getRunUsingEncoder() {
            return false;
        }

        @Override
        public PIDFCoefficients getModerVeloPID() {
            return new PIDFCoefficients(0, 0, 0,
                    getMotorVelocityF(getMaxRPM() / 60 * getTicksPerRev()));
        }

        /*
         * These are physical constants that can be determined from your robot (including the track
         * width; it will be tune empirically later although a rough estimate is important). Users are
         * free to chose whichever linear distance unit they would like so long as it is consistently
         * used. The default values were selected with inches in mind. Road runner uses radians for
         * angular distances although most angular parameters are wrapped in Math.toRadians() for
         * convenience. Make sure to exclude any gear ratio included in MOTOR_CONFIG from GEAR_RATIO.
         */
        @Override
        public double getWheelRadius() {
            return 2;
        }

        @Override
        public double getGearRatio() {
            return 1;
        }

        @Override
        public double getTrackWidth() {
            return 13.2;
        }

        /*
         * These are the feedforward parameters used to model the drive motor behavior. If you are using
         * the built-in velocity PID, *these values are fine as is*. However, if you do not have drive
         * motor encoders or have elected not to use them for velocity control, these values should be
         * empirically tuned.
         */
        @Override
        public double getKV() {
            return 0.0204308;
        }

        @Override
        public double getKA() {
            return 0.00003;
        }

        @Override
        public double getKStatic() {
            return 0.0022;
        }

        /*
         * These values are used to generate the trajectories for you robot. To ensure proper operation,
         * the constraints should never exceed ~80% of the robot's actual capabilities. While Road
         * Runner is designed to enable faster autonomous motion, it is a good idea for testing to start
         * small and gradually increase them later after everything is working. All distance units are
         * inches.
         */
        /*
         * Note from LearnRoadRunner.com:
         * The velocity and acceleration constraints were calculated based on the following equation:
         * ((MAX_RPM / 60) * GEAR_RATIO * WHEEL_RADIUS * 2 * Math.PI) * 0.85
         * Resulting in 52.48180821614297 in/s.
         * This is only 85% of the theoretical maximum velocity of the bot, following the recommendation above.
         * This is capped at 85% because there are a number of variables that will prevent your bot from actually
         * reaching this maximum velocity: voltage dropping over the game, bot weight, general mechanical inefficiencies, etc.
         * However, you can push this higher yourself if you'd like. Perhaps raise it to 90-95% of the theoretically
         * max velocity. The theoretically maximum velocity is 61.74330378369762 in/s.
         * Just make sure that your bot can actually reach this maximum velocity. Path following will be detrimentally
         * affected if it is aiming for a velocity not actually possible.
         *
         * The maximum acceleration is somewhat arbitrary and it is recommended that you tweak this yourself based on
         * actual testing. Just set it at a reasonable value and keep increasing until your path following starts
         * to degrade. As of now, it simply mirrors the velocity, resulting in 52.48180821614297 in/s/s
         *
         * Maximum Angular Velocity is calculated as: maximum velocity / trackWidth * (180 / Math.PI) but capped at 360°/s.
         * You are free to raise this on your own if you would like. It is best determined through experimentation.

         */
        @Override
        public double getMaxVel() {
            return 60.52801845916335;
        }

        @Override
        public double getMaxAccel() {
            return 60.52801845916335;
        }

        @Override
        public double getMaxAngVel() {
            return 9.093841190871938;
        }

        @Override
        public double getMaxAngAccel() {
            return Math.toRadians(247.7142857142857);
        }

        @Override
        public double getEncoderTicksPerRev() {
            return 8192;
        }

        @Override
        public double getEncoderWheelRadius() {
            return 0.69;
        }

        @Override
        public double getEncoderGearRatio() {
            return 1;
        }

        @Override
        public double getEncoderLateralDistance() {
            return 13.688697112094044;
        }

        @Override
        public double getEncoderForwardOffset() {
            return 3.75;
        }

        @Override
        public double getXMultiplier() {
            return 1.019884283;
        }

        @Override
        public double getYMultiplier() {
            return 1.012099197;
        }

        private OldBuild() {

        }
    }
    public static RobotConfig currentInstance = new GoBuildaStraferV5Tuned();
}
