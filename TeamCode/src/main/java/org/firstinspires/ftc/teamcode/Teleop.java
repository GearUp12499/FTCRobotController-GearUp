package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.SharedHardware.encoderLeft;
import static org.firstinspires.ftc.teamcode.SharedHardware.encoderRear;
import static org.firstinspires.ftc.teamcode.SharedHardware.encoderRight;
import static org.firstinspires.ftc.teamcode.SharedHardware.frontLeft;
import static org.firstinspires.ftc.teamcode.SharedHardware.frontRight;
import static org.firstinspires.ftc.teamcode.SharedHardware.liftHorizontal;
import static org.firstinspires.ftc.teamcode.SharedHardware.prepareHardware;
import static org.firstinspires.ftc.teamcode.SharedHardware.rearLeft;
import static org.firstinspires.ftc.teamcode.SharedHardware.rearRight;
import static org.firstinspires.ftc.teamcode.SharedHardware.runtime;
import static org.firstinspires.ftc.teamcode.SharedHardware.turret;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.lib.LinearCleanupOpMode;
import org.firstinspires.ftc.teamcode.lib.RobotCompletePose;
import org.firstinspires.ftc.teamcode.snap.MatchTimer;
import org.firstinspires.ftc.teamcode.snap.SnapRunner;

@TeleOp(name = "TeleOp")
public class Teleop extends LinearCleanupOpMode {
    public Lift l;

    public final int TURRET_THRESHOLD = 800;
    public final int TURRET_DELTA = 1500; // STILL HAVE TO TEST
    public int turret_center;
    public boolean forward = true;
    public IOControl io;

    @Override
    public void cleanup() {
        if (frontLeft != null) {
            frontLeft.setPower(0);
        }
        if (frontRight != null) {
            frontRight.setPower(0);
        }
        if (rearLeft != null) {
            rearLeft.setPower(0);
        }
        if (rearRight != null) {
            rearRight.setPower(0);
        }
        if (l != null) {
            l.liftVertical1.setPower(0);
            l.liftVertical2.setPower(0);
            l.liftHorizontal.setPower(0);
        }
        if (turret != null) {
            turret.setPower(0);
        }
    }

    @Override
    public void main() throws InterruptedException {
        prepareHardware(hardwareMap);
        l = new Lift(hardwareMap);
        io = new IOControl(hardwareMap);
        turret_center = turret.getCurrentPosition();
        SnapRunner snapRunner = new SnapRunner();
        snapRunner.addSnap(new MatchTimer(telemetry));

        waitForStart();
        snapRunner.init();
        while (opModeIsActive()) {
            drive();
            lift();
            turret();
            autoScore();
            //l.update2();
            l.liftVertical1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            l.liftVertical2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


            telemetry.addLine("Distance sensor:");
            telemetry.addData("Distance (mm)", io.distSensorM.getDistance(DistanceUnit.MM));
            telemetry.addLine("Odometry:");
            telemetry.addData("left", encoderLeft.getCurrentPosition());
            telemetry.addData("right", encoderRight.getCurrentPosition());
            telemetry.addData("f/b", encoderRear.getCurrentPosition());
            telemetry.addData("lift counts:", l.liftVertical1.getCurrentPosition());
            telemetry.addData("lift target:", l.targetVerticalCount);
            snapRunner.loop();
            telemetry.update();
        }
        snapRunner.finish();
    }

    //////////////////////////////////////////////////////////////////

    public void drive() {
        double speed = (0.25 + gamepad1.left_trigger * 0.75);
        double vX = 0; // forward/back
        double vY = 0; // left/right
        boolean useDPad = true;
        if (gamepad1.dpad_up) {
            vX += 1;
        } else if (gamepad1.dpad_down) {
            vX -= 1;
        } else if (gamepad1.dpad_left) {
            vY -= 1;
        } else if (gamepad1.dpad_right) {
            vY += 1;
        } else {
            useDPad = false;
        }
        if (useDPad) {
            double m1 = vX + vY;
            double m2 = vX - vY;
            double m3 = vX - vY;
            double m4 = vX + vY;
            driveMotor(speed * m1, speed * m2, speed * m3, speed * m4);
        } else {
            double left = -gamepad1.left_stick_y,
                    right = -gamepad1.right_stick_y;
            updateDirection();
            driveMotor(left, left, right, right);
        }
    }

    public void updateDirection() {
        if (gamepad1.right_trigger > 0.5) {
            if (gamepad1.x)
                forward = true;
            else if (gamepad1.y)
                forward = false;
        }
    }

    public void driveMotor(double lf, double lb, double rf, double rb) {
        if (!l.isExtended())
            lf = lb = rf = rb = 0;
        if (!forward) {
            lf = -lf;
            lb = -lb;
            rf = -rf;
            rb = -rb;
        }

        frontLeft.setPower(lf);
        frontRight.setPower(rf);
        rearLeft.setPower(lb);
        rearRight.setPower(rb);
    }

    /////////////////////////////////////////////////////

    private boolean lastLeftBumper1 = false;
    private boolean last2DpadUp = false;
    private boolean last2DpadDown = false;
    private boolean last2Back = false;


    public void lift() {
        // TODO make dpad not go BRRRRRRRRRRRRRRRRRRRRRR
        /*
        if (RisingFallingEdges.isRisingEdge(gamepad2.back)) {
            l.liftVertical1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            l.liftVertical2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            l.liftVertical1.setPower(Lift.POWER_DOWN / 2.0);
            l.liftVertical2.setPower(Lift.POWER_DOWN / 2.0); // slowly move down
        }
        if (RisingFallingEdges.isFallingEdge(gamepad2.back)) {
            l.liftVertical1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            l.liftVertical1.setPower(Lift.POWER_UP);
            l.liftVertical1.setTargetPosition(0);
            l.liftVertical1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            l.liftVertical2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            l.liftVertical2.setPower(0);
            l.liftVertical2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }*/
        int hTargPos = 0;
        if(gamepad1.start){
            hTargPos = liftHorizontal.getCurrentPosition();
        }
        if (gamepad2.back) {
            l.liftVertical1.setPower(Lift.POWER_DOWN * 0.5);
            l.liftVertical2.setPower(Lift.POWER_DOWN * 0.5);

            //return;  // Skip...
        }

        if (last2Back && !gamepad2.back) {
            l.liftVertical1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            l.liftVertical2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            l.initLiftMotors(); // Reset encoders & stuff
        }
        last2Back = gamepad2.back;

        if (gamepad2.back) return;

        if (gamepad2.x){
                l.setVerticalTarget(2); //2
                l.update();
        }

        else if (gamepad2.b) {
            l.setHorizontalTargetManual(5);
            l.retract();
            l.update();
            int direction = sign(-turret.getCurrentPosition());
            turret.setPower(0.5 * direction);
            //l.setVerticalTarget(0);
            l.setVerticalTargetManual(175);
            runtime.reset();
            while (opModeIsActive() && !(Math.abs(turret.getCurrentPosition()) <= 20)) {
                if (Math.abs(turret.getCurrentPosition()) <= 10) {
                    turret.setPower(0);
                }
                if(runtime.seconds()>0.5)
                    l.update();
            }
            turret.setPower(0);

        } else if (gamepad2.a)
            l.setVerticalTarget(1); //2

            //l.setVerticalTarget(1);
        else if (gamepad2.y){
            //while(liftVertical1.getCurrentPosition()<4500){
                l.setVerticalTarget(3); //2
                l.update();
        }
        else if (gamepad2.dpad_up) {
            l.comp = false;
            l.liftVertical1.setPower(Lift.POWER_UP * 0.8);
            l.liftVertical2.setPower(Lift.POWER_UP * 0.8);
            l.setVerticalTargetManual(l.getFakedCurrentVerticalCounts());
        } else if (gamepad2.dpad_down) {
            l.comp = false;
            l.liftVertical1.setPower(Lift.POWER_DOWN * 0.8);
            l.liftVertical2.setPower(Lift.POWER_DOWN * 0.8);
            l.setVerticalTargetManual(l.getFakedCurrentVerticalCounts());
        }

        if (!gamepad2.dpad_down && !gamepad2.dpad_up) {
            l.comp = true;
        }

        if ((last2DpadUp && !gamepad2.dpad_up) || (last2DpadDown && !gamepad2.dpad_down)) {
            l.setVerticalTargetManual(l.liftVertical1.getCurrentPosition());
        }

        last2DpadUp = gamepad2.dpad_up;
        last2DpadDown = gamepad2.dpad_down;
        if (gamepad2.right_bumper) {
            runtime.reset();
            while(liftHorizontal.getCurrentPosition()>20 && runtime.seconds()<2) {
                if(liftHorizontal.getCurrentPosition()<80){
                    liftHorizontal.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                    liftHorizontal.setPower(-0.005*liftHorizontal.getCurrentPosition());
                }
                else{
                    liftHorizontal.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                    liftHorizontal.setPower(-0.8);
                }
            }
            liftHorizontal.setPower(0);
        } else if (gamepad2.left_bumper) {
            while(liftHorizontal.getCurrentPosition()<hTargPos) {
                liftHorizontal.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                liftHorizontal.setTargetPosition(hTargPos);
                liftHorizontal.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                liftHorizontal.setPower(0.2);
            }
           /* double x = liftHorizontal.getCurrentPosition();
            double vLiftEti = 759/5.25;//encoder count to inch
            int targ = 0;
            liftHorizontal.setPower(0.2);
            if(l.liftVertical1.getCurrentPosition()<500){
                double h = (4.93 - 3.4 - 0.000658*(x)-0.00000324*(x)*(x));
                targ = (int)((1.9-h)*vLiftEti);
                l.setVerticalTargetManual(targ);

            }
            liftHorizontal.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            liftHorizontal.setPower(0.8);
            l.update();*/
        } else if (gamepad2.right_trigger > 0.2) {
            liftHorizontal.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            liftHorizontal.setPower(-0.4);
            l.update();
        } else if (gamepad2.left_trigger > 0.2) {
            double x = liftHorizontal.getCurrentPosition();
            double vLiftEti = 759/5.25;//encoder count to inch
            int targ = 0;
            liftHorizontal.setPower(0.2);
            if(l.liftVertical1.getCurrentPosition()<500){
                // sag correction
                double h = (4.93 - 3.4 - 0.000658*(x)-0.00000324*(x)*(x));
                targ = (int)((2.9-h)*vLiftEti);
                l.setVerticalTargetManual(targ);

            }
            liftHorizontal.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            if(gamepad2.left_trigger<0.9)
                if(l.liftVertical1.getCurrentPosition()>1000){
                    liftHorizontal.setPower(0.2);
                }
                else
                    liftHorizontal.setPower(0.35);
            else if(gamepad2.left_trigger>0.91)
                liftHorizontal.setPower(0.6);
            l.update();
        }else{
            liftHorizontal.setPower(0);
        }


        // CLAW
        if (gamepad1.left_bumper && !lastLeftBumper1) {
            l.closeClaw();
            sleep(600);
            l.moveVertical(300);
        } else if (gamepad1.right_bumper) l.openClaw();


        lastLeftBumper1 = gamepad1.left_bumper;
        l.update();

        telemetry.addData("horizontal slider", l.liftHorizontal.getCurrentPosition());
        telemetry.addData("horizontal slider to", l.liftHorizontal.getTargetPosition());
        telemetry.addData("sensor read", io.distSensorM.getDistance(DistanceUnit.MM));
    }

    private int sign(int i) {
        return Integer.compare(i, 0);
    }

    ////////////////////////////////////////////////////////////////////
    public RobotCompletePose collectionPose = null;
    boolean last1Start = false;
    public RobotCompletePose deliveryPose = null;
    boolean last1Back = false;

    boolean last1A = false;
    boolean last1Y = false;

    RobotCompletePose autoScoreTarget = null;

    public void autoScore(){ //automating scoring
        /*
        basically the way this is going to work is the following
        1. drivers score one manually
            a. when ready to grab cup from stack, driver 1 hits gamepad1.start
            b. when right above the pole ready to deliver, driver 1 hits gamepad1.back
        2. driver 1 can hit y, and it will reset the robots position that is ready to pick up cup
        3. driver 1 will manually close claw
        4. driver 1 will then hit a, which will go from grabbing cone position, to right above pole position
        This cycle repeats on an on, this will probably be revised/optimized as we practice it
        --JJ
         */
        // Now with 100% more snapshots!
        if (gamepad1.start && !last1Start) {
            collectionPose = RobotCompletePose.captureImmediatePosition(l);
            notifyOk(gamepad1);
        }
        last1Start = gamepad1.start;
        if (gamepad1.back && !last1Back) {
            deliveryPose = RobotCompletePose.captureImmediatePosition(l);
            notifyOk(gamepad1);
        }
        last1Back = gamepad1.back;

        if (gamepad1.a && !last1A) {
            if (deliveryPose == null) {
                notifyError(gamepad1);
            } else if (autoScoreTarget == null) {
                autoScoreTarget = deliveryPose;
                notifyOk(gamepad1);
            } else {
                autoScoreTarget = null;
                turret.setPower(0);
                notifyError(gamepad1);  // "error" (cleared)
            }
        }
        last1A = gamepad1.a;
        if (gamepad1.y && !last1Y) {
            if (collectionPose == null) {
                notifyError(gamepad1);
            } else if (autoScoreTarget == null) {
                autoScoreTarget = collectionPose;
                notifyOk(gamepad1);
            } else {
                autoScoreTarget = null;
                turret.setPower(0);
                notifyError(gamepad1);  // "error" (cleared)
            }
        }
        last1Y = gamepad1.y;

        if (autoScoreTarget != null) {
            telemetry.addLine("1: press A or Y to cancel AutoScore");
            autoScoreTarget.runAsync(l, telemetry);
        }
    }

    public void turretAuto(){

    }

    //------------------------------------------------------
    public void turret() throws InterruptedException {
        int b = 0;
        if (l.liftVertical1.getCurrentPosition() < 100)//TURRET_THRESHOLD)
            return;

        double speed = gamepad2.left_stick_x * 0.5; //Math.pow(gamepad2.left_stick_x, 2);
        int now = turret.getCurrentPosition() - turret_center;
        //if ((speed < 0 && now > -TURRET_DELTA) || (speed > 0 && now < TURRET_DELTA))
        turret.setPower(speed);

        if (gamepad1.b) {
            l.setVerticalTargetManual(Math.max(l.liftVertical1.getCurrentPosition(), Lift.inEnc(14)));
            runtime.reset();
            //200
            while (io.distSensorM.getDistance(DistanceUnit.MM) > 260 && Math.abs(turret.getCurrentPosition()) < 1200 && runtime.seconds()<2) {
                turret.setPower(0.40); //0.35
                telemetry.addData("distance:", io.distSensorM.getDistance(DistanceUnit.CM));
                telemetry.update();
                l.update();
            }
            turret.setPower(0);
        } else if (gamepad1.x) {
            l.setVerticalTargetManual(Math.max(l.liftVertical1.getCurrentPosition(), Lift.inEnc(14)));
            runtime.reset();
            while (io.distSensorM.getDistance(DistanceUnit.MM) > 260 && Math.abs(turret.getCurrentPosition()) < 1200 && runtime.seconds()<2) {
                turret.setPower(-0.40); //0.35
                telemetry.addData("distance:", io.distSensorM.getDistance(DistanceUnit.CM));
                telemetry.update();
                l.update();
            }
            turret.setPower(0);
        }
        //else
        //turret.setPower(0);
        // if(turret.getCurrentPosition() == 0)
        //   turret.setPower(0);
        /*if(gamepad2.b){
            //runtime.reset();
            b =1;
            if(turret.getCurrentPosition() > 0){
                turret.setTargetPosition(turret_center);
                turret.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                turret.setPower(-0.5);
            }
            else if(turret.getCurrentPosition() < 0){
                turret.setTargetPosition(0);
                turret.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                turret.setPower(0.5);
            }
        }*/
        telemetry.addData("turret center:", turret_center);
        telemetry.addData("turret position:", now);
        telemetry.addData("turret speed:", speed);

    }

    private void notifyError(Gamepad gamepad) {
        Gamepad.RumbleEffect.Builder builder = new Gamepad.RumbleEffect.Builder();
        builder
                .addStep(0, 0.5f, 200)
                .addStep(0, 0, 100)
                .addStep(0, 0.5f, 200);
        gamepad.runRumbleEffect(builder.build());
    }

    private void notifyOk(Gamepad gamepad) {
        Gamepad.RumbleEffect.Builder builder = new Gamepad.RumbleEffect.Builder();
        builder
                .addStep(0, 0.5f, 200);
        gamepad.runRumbleEffect(builder.build());
    }
}
