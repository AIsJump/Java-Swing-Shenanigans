import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Scanner;

import static java.lang.System.out;

public class MMCalculator {
    JFrame frame;
    Dimension WINDOW_DIMENSIONS = new Dimension(400, 200);
    HashMap<String, ElementProperties> elementMap;
    double molarMass = 0;

    public MMCalculator() {
        frame = new JFrame("MMCalculator");
        finishWindow();
    }

    void finishWindow() {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();

        //set spawn in the left of the screen
        frame.setBounds(d.width / 8 - WINDOW_DIMENSIONS.width / 2, d.height / 3 * 2 - WINDOW_DIMENSIONS.height / 2, WINDOW_DIMENSIONS.width, WINDOW_DIMENSIONS.height);
        frame.setResizable(false);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        elementMap = makeElementMap();
        makeScreen();

        frame.setVisible(true);
    }

    void makeScreen() {
        JLabel enterHere = new JLabel("Enter chemical formula");
        enterHere.setBounds(10, 50, 150, 30);
        enterHere.setVisible(true);
        frame.add(enterHere);

        JLabel answer = new JLabel();
        answer.setBounds(120, 120, 250, 30);
        answer.setVisible(true);
        frame.add(answer);

        JTextField formula = new JTextField();
        formula.setBounds(160, 50, 200, 30);
        formula.setEditable(true);
        formula.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    updateMolarMass(formula, answer);
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        formula.setVisible(true);
        frame.add(formula);

        JButton solve = new JButton("Solve");
        solve.setBounds(10, 100, 100, 50);
        solve.addActionListener(e -> updateMolarMass(formula, answer));
        solve.setVisible(true);
        frame.add(solve);
    }

    void updateMolarMass(@NotNull JTextField formula, JLabel answer) {
        molarMass = 0;
        char[] theFormula = formula.getText().toCharArray();
        int index = 0;
        while (checkIndex(index, theFormula.length)) {
            if (Character.isUpperCase(theFormula[index])) {
                String element = "" + theFormula[index++];

                if (checkIndex(index, theFormula.length)) {
                    if (Character.isLowerCase(theFormula[index])) {
                        element += theFormula[index++];

                        if (checkIndex(index, theFormula.length)) {
                            if (Character.isDigit(theFormula[index])) {
                                calculateMM(element, Character.getNumericValue(theFormula[index++]));
                            }
                            else {
                                calculateMM(element, 1);
                            }
                        }
                        else calculateMM(element, 1);
                    }

                    else if (Character.isDigit(theFormula[index])) {
                        calculateMM(element, Character.getNumericValue(theFormula[index++]));
                    }
                    else {
                        calculateMM(element, 1);
                    }
                }
                else calculateMM(element, 1);
            }
            else if (Character.isDigit(theFormula[index]))
                index++;
            else break;
        }
        out.println(molarMass);
        DecimalFormat df = new DecimalFormat("#.###");
        df.setRoundingMode(RoundingMode.HALF_UP);
        answer.setText("Molar Mass: " + df.format(molarMass) + " g/mol");
    }

    boolean checkIndex(int index, int len) {
        return index < len;
    }

    void calculateMM(String element, int sub) {
        molarMass += elementMap.get(element).molarMass * sub;
    }

    HashMap<String, ElementProperties> makeElementMap() {
        HashMap<String, ElementProperties> elementMap = new HashMap<>(118);
        try (Scanner data = new Scanner(new File("p_table.dat"))) {
            int atomicNumber = 1;
            while (data.hasNextLine()) {
                String[] theLine = data.nextLine().split(" ");
                ElementProperties element = new ElementProperties();
                element.atomicNumber = atomicNumber;
                element.name = theLine[1];
                element.molarMass = Double.parseDouble(theLine[2]);
                elementMap.put(theLine[0], element);
                atomicNumber++;
            }
        }
        catch (FileNotFoundException e) {
            out.println("Something went wrong");
        }
        return elementMap;
    }
}

class ElementProperties {
    public int atomicNumber;
    public String name;
    public double molarMass;
}