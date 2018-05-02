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
        UltrasonicSensor ultraSonic = myRobot.getUltrasonicSensor(Sensor.Port.S3);
        Motor rightMotor = myRobot.getLargeMotor(Motor.Port.C);
        float sumError = 0;
        float lastError = 0;
        double r = 50;
        float ki = 0.6f;
        float kp = 22;
        float kd = 0.4f;
        float smallestError = 99999;

        long runTime = System.currentTimeMillis();
        long accumTime = System.currentTimeMillis();

        leftMotor.forward();
        rightMotor.forward();
        for (int j =0; j<500; j++) {
            long dt = System.currentTimeMillis() - runTime;
            runTime = System.currentTimeMillis();
            float distance = ultraSonic.getDistance()*100;

            float error = (float)(distance - r);
            System.out.println("error" + error);
            sumError += error;

            float p = kp * error;
            float i = ki * sumError;
            float d = kd * (error - lastError)/dt;

            lastError = error;
            int speed = ((int)(p+i+d));

            if (speed < 0) {
                speed = speed*-1;
                leftMotor.setSpeed(speed);
                rightMotor.setSpeed(speed);
                leftMotor.backward();
                rightMotor.backward();
            } else {
                leftMotor.setSpeed(speed);
                rightMotor.setSpeed(speed);
                leftMotor.forward();
                rightMotor.forward();
            }
            if (Math.abs(error) < smallestError) {
                smallestError = Math.abs(error);
                System.out.println("new smallest error: " + Math.abs(smallestError));
            }

            if (System.currentTimeMillis() - accumTime >= 10000) {
                if (r == 50)
                    r = 30;
                else
                    r = 50;
                accumTime = System.currentTimeMillis();
            }
            System.out.println("runtime: "+(System.currentTimeMillis() - runTime)/1000);

            System.out.println("r: "+r);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
    }

}
