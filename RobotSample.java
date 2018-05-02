import ShefRobot.*;
import java.lang.Object.*;
import java.util.*;

/**
 *
 * @author sdn
 */
public class RobotSample {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        //Create a robot object to use and connect to it
        Robot myRobot = new Robot("dia-lego-e3");
        System.out.println("hfiudshfs");

        //The robot is made of components which are themselves objects.
        //Create references to them as useful shortcuts
        Motor leftMotor = myRobot.getLargeMotor(Motor.Port.B);
        UltrasonicSensor ultraSonicR = myRobot.getUltrasonicSensor(Sensor.Port.S3);
        UltrasonicSensor ultraSonicL = myRobot.getUltrasonicSensor(Sensor.Port.S1);
        Motor rightMotor = myRobot.getLargeMotor(Motor.Port.C);
        float sumError = 0;
        float lastError = 0;

        float ki = 0;// 0.6f;
        float kp = 3;
        float kd = 0; //0.4f;
        float smallestError = 99999;
        int tarSpeed = 150;

        long runTime = System.currentTimeMillis();
        long accumTime = System.currentTimeMillis();

        leftMotor.setSpeed(tarSpeed);
        rightMotor.setSpeed(tarSpeed);
        leftMotor.forward();
        rightMotor.forward();
        for (int j =0; j<300; j++) {
            long dt = System.currentTimeMillis() - runTime;
            runTime = System.currentTimeMillis();
            float distanceR = ultraSonicR.getDistance()*100;
            float distanceL = ultraSonicL.getDistance()*100;
            double in = distanceR - distanceL;
            double r = 0;

            float error = (float)(in - r);
            if (Double.isNaN(error))
                error = lastError;

            sumError += error;

            float p = kp * error;
            float i = ki * sumError;
            float d = kd * (error - lastError)/dt;

            lastError = error;
            int speed = ((int)(p+i+d));
            // If distance is negative (needs to move away from wall)
            if (speed < 0) {
                if (Math.abs(speed) > tarSpeed) {
                    // ADDING NEGATIVE
                    leftMotor.setSpeed(0);
                    rightMotor.setSpeed(tarSpeed*2);
                }
                else {
                    leftMotor.setSpeed(tarSpeed + speed);
                    rightMotor.setSpeed(tarSpeed - speed);
                }
                leftMotor.forward();
                rightMotor.forward();
            } else {
                //leftMotor.setSpeed(speed);
                if (Math.abs(speed) > tarSpeed) {
                    // ADDING NEGATIVE
                    leftMotor.setSpeed(tarSpeed*2);
                    rightMotor.setSpeed(0);
                }
                else {
                    leftMotor.setSpeed(tarSpeed + speed);
                    rightMotor.setSpeed(tarSpeed - speed);
                }
                leftMotor.forward();
                rightMotor.forward();
            }
            if (Math.abs(error) < smallestError) {
                smallestError = Math.abs(error);
            }
            /*
            if (System.currentTimeMillis() - accumTime >= 10000) {
                if (r == 50)
                    r = 30;
                else
                    r = 50;
                accumTime = System.currentTimeMillis();
            }
            */
        }
    }

}
