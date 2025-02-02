import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;
import static java.lang.System.out;
import javax.swing.*;
import java.awt.*;
import java.util.concurrent.TimeUnit;

public class Physics2Project {
    static String FILEPATH = "physics2_data.dat";
    //degreeY is degree from HORIZONTAL //degreeZ is degree as BEARING //velocityOfEjection is velocity from cannon in 3D (for pitch kinematics) //velocity2D is velocity from cannon in 2D (for yaw kinematics)
    private static double degreeY, degreeZ, degreeMach, temperature, specificRatio, velocityOfEjection, velocity2D;
    private static double time, distanceUp, distance0PI, distanceHalfPI;

    public Physics2Project() throws FileNotFoundException {
        setupPitchYawVelocity();
        out.println(degreeY + " " + degreeZ + " " + velocityOfEjection); //debug
        calculateTime();
        calculateDistanceUp();
        calculateDistance0PI();
        calculateDistanceHalfPI();
        out.println(time + " " + distanceUp + " " + distance0PI + " " + distanceHalfPI);
        out.println(velocityOfEjection + " " + velocity2D);
        AttributesTable2 table = new AttributesTable2(time, degreeY, degreeZ, velocityOfEjection, velocity2D);
        table.printVelocities();
        out.println(Arrays.toString(table.getTimes()) + " = Times");
        out.println(Arrays.toString(table.getXDistancesAndVelocities()) + " = XDistances");
        out.println(Arrays.toString(table.getZDistancesAndVelocities()) + " = ZDistances");
        new PhysicsWindowSide2(table);
    }

    public static void setupPitchYawVelocity() throws FileNotFoundException { //sets up degreeY (pitch), degreeZ (yaw), and the initial velocity of the projectile
        //read in user input
        Scanner kb = new Scanner(new File(FILEPATH));

        //get and check for valid degreeY input
        boolean degreeYValid = false;
        while (!degreeYValid) {
            System.out.print("Enter pitch of cannon (degrees): ");
            try {
                degreeY = Double.parseDouble(kb.nextLine());
                if (degreeY % 360 <= 90 && degreeY % 360 >= 0) degreeYValid = true;
                else System.out.println("A backwards cannon is a useless cannon.");
            }
            catch (Exception e) {System.out.println("Invalid input");}
        }

        //get and check for valid degreeZ input
        boolean degreeZValid = false;
        while (!degreeZValid) {
            System.out.print("Enter yaw of cannon (degrees): ");
            try {
                degreeZ = Double.parseDouble(kb.nextLine());
                if (degreeZ % 360 > 45 || degreeZ % 360 < -45)
                    System.out.println("I had 3 days to do this, I didn't have time :(");
                else degreeZValid = true;
            }
            catch (Exception e) {
                System.out.println("Invalid input");
            }
        }

        //get and check for valid mach angle input
        boolean machValid = false;
        while (!machValid) {
            System.out.print("Enter the mach angle of the projectile from cannon normal (degrees): ");
            try {
                degreeMach = Double.parseDouble(kb.nextLine());
                if (degreeMach % 360 <= 90 && degreeMach % 360 >= 0) machValid = true;
                else System.out.println("That's a wacky projectile.");
            }
            catch (Exception e) {System.out.println("Invalid input");}
        }

        //get and check for valid initial velocity input
//        boolean velocityValid = false;
//        String theLine;
//        while (!velocityValid) {
//            System.out.print("Enter velocity of cannon: ");
//            try {
//                theLine = kb.next();
//                if (theLine.charAt(0) == '-') {
//                    System.out.println("The cannon does not shoot itself.");
//                }
//                else {
//                    velocityOfEjection = Double.parseDouble(theLine);
//                    velocityValid = true;
//                }
//            }
//            catch (Exception e) {System.out.println("Invalid input");}
//        }

        //get and check for valid pressure input
//        boolean pressureValid = false;
//        String theLine2;
//        while (!pressureValid) {
//            System.out.print("Enter the pressure of the air in the cannon: ");
//            try {
//                theLine2 = kb.next();
//                if (theLine2.charAt(0) == '-') {
//                    System.out.println("The cannon does not enter the quantum realm.");
//                }
//                else {
//                    pressure = Double.parseDouble(theLine2);
//                    pressureValid = true;
//                }
//            }
//            catch (Exception e) {System.out.println("Invalid input");}
//        }

        //get and check for valid temperature input in kelvin
        boolean temperatureValid = false;
        while (!temperatureValid) {
            System.out.print("Enter the temperature of the air (kelvin): ");
            try {
                temperature = Double.parseDouble(kb.next());
                if (temperature <= 0) System.out.println("The cannon doesn't work.  Wonder why...");
                else temperatureValid = true;
            }
            catch (Exception e) {System.out.println("Invalid input");}
        }

        //get and check for valid ratio of specific heat input
        boolean ratioValid = false;
        while (!ratioValid) {
            System.out.print("Enter the ratio of specific heat of the gas (1-2): ");
            try {
                specificRatio = Double.parseDouble(kb.next());
                if (specificRatio < 1 || specificRatio > 2) {
                    System.out.println("What gas are you using???");
                }
                else ratioValid = true;
            }
            catch (Exception e) {System.out.println("Invalid input");}
        }

        //solve for magnitude of velocity
        //soundVelocity = Math.sqrt(specificRatio * universalGasConstant (J/KgK) * temperature)
        //c = sqrt(kRT)
        double soundVelocity = Math.sqrt(specificRatio * 287 * temperature);
        //sin(machAngle) = c/v
        //v = c/sin(machAngle)
        velocityOfEjection = soundVelocity / Math.sin(Math.toRadians(degreeMach));

        //set velocity of projectile for circle
        velocity2D = velocityOfEjection * Math.cos(Math.toRadians(degreeY));
        kb.close();
    }

    //calculation methods
    //time = 2 * initialVelocityY/gravity
    //t = 2V_Yo / g
    public static void calculateTime() {
        time = 2 * (velocityOfEjection * Math.sin(Math.toRadians(degreeY)) / 9.81);
    }

    //distanceY = initialVelocityY * time + .5 * acceleration * time^2
    //d_Y = (V_Yo)t + .5a(t)^2
    public static void calculateDistanceUp() {
        distanceUp = ((velocityOfEjection * Math.sin(Math.toRadians(degreeY)) * (time / 2)) + (.5 * (-9.81 * Math.pow((time / 2), 2))));
    }

    //distanceX = initialVelocityX * time
    //d_X = (V_Xo)t
    public static void calculateDistance0PI() {
        distance0PI = velocity2D * Math.cos(Math.toRadians(degreeZ)) * time;
    }

    //distanceZ = initialVelocityZ * time
    //d_Z = (V_Zo)t
    public static void calculateDistanceHalfPI() {
        distanceHalfPI = velocity2D * Math.sin(Math.toRadians(degreeZ)) * time;
    }
}

class AttributesTable2 {
    //times[] stores all the times used for mathematics
    private final double[] times;

    //XDistancesAndVelocities[] stores all the distances for the times stored in double[] times; velocities stay consistent/equal to initialXVelocity
    private final double[] XDistancesAndVelocities;
    public final double initialXVelocity;

    //ZDistancesAndVelocities[] stores all the distances for the times stored in double[] times; velocities stay consistent/equal to initialZVelocity
    private final double[] ZDistancesAndVelocities;
    public final double initialZVelocity;

    //YDistancesAndVelocities[] stores all the distances and velocities for the times stored in double[] times
    //YDistancesAndVelocities[][0] stores distances
    //YDistancesAndVelocities[][1] stores velocities
    private final double[][] YDistancesAndVelocities;
    private final double initialYVelocity;

    //stores the max airtime of projectile
    private final double time;

    public AttributesTable2(double time, double pitch, double yaw, double velocity, double velocity2) {
        //remember YVelocity subject to -9.81 acceleration via gravity
        //XVelocities and ZVelocities stay the same
        int n = ((int)(time / .25)) + 1; //scale time increment here
        this.time = time;
        initialXVelocity = velocity2 * Math.cos(Math.toRadians(yaw));
        initialZVelocity = velocity2 * Math.sin(Math.toRadians(yaw));
        initialYVelocity = velocity * Math.sin(Math.toRadians(pitch));

        times = new double[n];
        XDistancesAndVelocities = new double[n];
        //YDistancesAndVelocities[0] stores all Y distances as a function of time; YDistancesAndVelocities[1] stores all Y velocities as a function of time
        YDistancesAndVelocities = new double[n][2];
        ZDistancesAndVelocities = new double[n];
        fillTable();
    }

    //fills out all arrays with data points
    public void fillTable() {
        int index = 0;
        for (double i = .25; i < time; i += .25) { //scale time increment here
            times[index] = i;
            XDistancesAndVelocities[index] = findXDistance(i);
            YDistancesAndVelocities[index][0] = findYDistance(i);
            YDistancesAndVelocities[index][1] = findYVelocity(i);
            ZDistancesAndVelocities[index] = findZDistance(i);
            index++;
        }
        times[index] = time;
        XDistancesAndVelocities[index] = findXDistance(time);
        YDistancesAndVelocities[index][0] = findYDistance(time);
        YDistancesAndVelocities[index][1] = findYVelocity(time);
        ZDistancesAndVelocities[index] = findZDistance(time);
        roundingTable();
    }

    public void roundingTable() {
        int limit = times.length;
        for (int i = 0; i < limit; i++) {
            times[i] = Math.round(times[i] * 1000d) / 1000d;
            XDistancesAndVelocities[i] = Math.round(XDistancesAndVelocities[i] * 1000d) / 1000d;
            YDistancesAndVelocities[i][0] = Math.round(YDistancesAndVelocities[i][0] * 1000d) / 1000d;
            YDistancesAndVelocities[i][1] = Math.round(YDistancesAndVelocities[i][1] * 1000d) / 1000d;
            ZDistancesAndVelocities[i] = Math.round(ZDistancesAndVelocities[i] * 1000d) / 1000d;
        }
    }

    public double findXDistance(double time) {
        return initialXVelocity * time;
    }

    public double findYDistance(double time) {
        return (initialYVelocity * time) + (.5 * -9.81 * Math.pow(time, 2));
    }

    public double findYVelocity(double time) {
        return initialYVelocity + (-9.81 * time);
    }

    public double findZDistance(double time) {
        return initialZVelocity * time;
    }

    //utility functions
    public double[] getTimes() {
        return times;
    }

    public double[] getXDistancesAndVelocities() {
        return XDistancesAndVelocities;
    }

    public double[] getZDistancesAndVelocities() {
        return ZDistancesAndVelocities;
    }

    public double[][] getYDistancesAndVelocities() {
        return YDistancesAndVelocities;
    }

    public void printVelocities() {
        out.println("XVelocity = " + initialXVelocity + "\nYVelocity = " + initialYVelocity + "\nZVelocity = " + initialZVelocity);
    }
}

class PhysicsWindowSide2 extends JFrame {
    private final JFrame frame1;
    private final JFrame frame2;
    private final JLabel timeLabel, distanceLabel, velocityLabel;
    private int indexOfVisibility = 0;
    private final double[] times;
    private final double[] XDistancesAndVelocities;
    private final double[][] YDistancesAndVelocities;
    private final double[] ZDistancesAndVelocities;

    public PhysicsWindowSide2(@NotNull AttributesTable2 at) {
        times = at.getTimes();
        XDistancesAndVelocities = at.getXDistancesAndVelocities();
        YDistancesAndVelocities = at.getYDistancesAndVelocities();
        ZDistancesAndVelocities = at.getZDistancesAndVelocities();

        frame1 = new JFrame("3D Kinematics - Side View");
        frame1.setSize(1000, 550);
        frame1.setResizable(false);
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame1.setLayout(null);
        drawSide();
        frame1.setVisible(true);

        frame2 = new JFrame("3D Kinematics - Bird's Eye View");
        frame2.setBounds(1000, 0, 500, 500);
        frame2.setResizable(false);
        frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame2.setLayout(null);
        drawBird();
        frame2.setVisible(true);

        Container container = frame1.getContentPane();

        timeLabel = new JLabel();
        timeLabel.setBounds(0, 410, 1000, 20);
        timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        container.add(timeLabel);
        distanceLabel = new JLabel();
        distanceLabel.setBounds(0, 440, 1000, 20);
        distanceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        container.add(distanceLabel);
        velocityLabel = new JLabel();
        velocityLabel.setBounds(0, 470, 1000, 20);
        velocityLabel.setHorizontalAlignment(SwingConstants.CENTER);
        container.add(velocityLabel);

        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            timedButtonVisibility(at);
            try {
                TimeUnit.MILLISECONDS.sleep(250);
            }
            catch (Exception e) {
                Thread.currentThread().interrupt();
                out.println("stop");
            }
        }
    }

    private JButton[] allButtonsSide;

    public void drawSide() {
        final int mPerPixelSide = 100; //scale pixels here; 100 meters = 1 pixel

        Container container = frame1.getContentPane();

        JButton button = new JButton("cannon");
        button.setBounds(0, 350, 100, 50);
        container.add(button);

        //100 meters = 1 pixel
        allButtonsSide = new JButton[times.length];
        for (int i = 0; i < allButtonsSide.length; i++) {
            allButtonsSide[i] = new JButton();
            allButtonsSide[i].setBounds(
                    100 + (int)(XDistancesAndVelocities[i] * (1 / mPerPixelSide)),
                    350 - (int)(YDistancesAndVelocities[i][0] * (1 / mPerPixelSide)),
                    10, 10
            );
            allButtonsSide[i].setVisible(false);
            container.add(allButtonsSide[i]);
        }
    }

    private JButton[] allButtonsBird;

    public void drawBird() {
        final int mPerPixelBird = 20; //scale pixels here; 20 meters = 1 pixel

        Container container = frame2.getContentPane();

        JButton button = new JButton("cannon");
        button.setBounds(225, 450, 50, 50);
        container.add(button);

        //20 meters = 1 pixel
        allButtonsBird = new JButton[times.length];
        for (int i = 0; i < allButtonsBird.length; i++) {
            allButtonsBird[i] = new JButton();
            allButtonsBird[i].setBounds(
                    250 + (int)(ZDistancesAndVelocities[i] * (1 / mPerPixelBird)),
                    450 - (int)(XDistancesAndVelocities[i] * (1 / mPerPixelBird)),
                    10, 10
            );
            allButtonsBird[i].setVisible(false);
            container.add(allButtonsBird[i]);
        }
    }

    public void timedButtonVisibility(AttributesTable2 at) {
        if (indexOfVisibility >= times.length) {
            indexOfVisibility = 0;
            for (JButton allButton : allButtonsSide) allButton.setVisible(false);
            for (JButton allButton2 : allButtonsBird) allButton2.setVisible(false);
        }
        allButtonsSide[indexOfVisibility].setVisible(true);
        allButtonsBird[indexOfVisibility].setVisible(true);

        double XVelocityConstant = Math.round(at.initialXVelocity * 1000d) / 1000d;
        double ZVelocityConstant = Math.round(at.initialZVelocity * 1000d) / 1000d;

        timeLabel.setText("Time (s): " + times[indexOfVisibility]);
        distanceLabel.setText("Distance X (m): " + XDistancesAndVelocities[indexOfVisibility] + "   Distance Y (m): " + YDistancesAndVelocities[indexOfVisibility][0] + "   Distance Z (m): " + ZDistancesAndVelocities[indexOfVisibility]);
        velocityLabel.setText("Velocity X (m/s): " + XVelocityConstant + "   Velocity Y (m/s): " + YDistancesAndVelocities[indexOfVisibility][1] + "   Velocity Z (m/s): " + ZVelocityConstant);
        indexOfVisibility++;
    }
}