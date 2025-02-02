import org.jetbrains.annotations.*;

import javax.swing.*;
import java.util.*;

public class CalculatorWindow {
    private static JFrame window;
    private boolean operationBeingUsed = false;
    private static boolean clear = false;
    CalculatorFunctions cf = new CalculatorFunctions();

    public CalculatorWindow() {
        //create the window for the calculator
        window = new JFrame("Calculator");
        window.setSize(400, 400);
        //window.setResizable(false);
        window.setLayout(null);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel calculator = new JPanel();
        calculator.setSize(400, 400);
        calculator.setLayout(null);

        //create the screen for the calculator
        JTextArea textArea = new JTextArea();
        textArea.setBounds(calculator.getBounds().width / 2 - 100, 20, 200, 30);
        //textArea.setAlignmentY(0.5f);
        textArea.setEditable(false);
        textArea.setVisible(true);

        //create the numeric buttons 0-9
        JButton[] numericButtons = new JButton[10];
        numericButtons[0] = new JButton("0");
        numericButtons[0].setBounds(100, 250, 50, 50);

        //create all other buttons
        JButton decimal = new JButton(".");
        decimal.setBounds(150, 250, 50, 50);
        decimal.addActionListener(e -> textArea.append(decimal.getText()));
        decimal.setVisible(true);
        calculator.add(decimal);

        JButton equals = new JButton("=");
        equals.setBounds(200, 250, 50, 50);
        equals.addActionListener(e -> {
            textArea.append(equals.getText());
            clear = true;
        });
        equals.setVisible(true);
        calculator.add(equals);

        JButton plus = new JButton(" + ");
        plus.setBounds(250, 200, 50, 100);
        addActionsAndAttach(calculator, textArea, plus);

        JButton minus = new JButton(" - ");
        minus.setBounds(250, 150, 50, 50);
        addActionsAndAttach(calculator, textArea, minus);

        JButton x = new JButton(" x ");
        x.setBounds(250, 100, 50, 50);
        addActionsAndAttach(calculator, textArea, x);

        JButton divide = new JButton(" ÷ ");
        divide.setBounds(250, 50, 50, 50);
        addActionsAndAttach(calculator, textArea, divide);

        JButton c = new JButton("C");
        c.setBounds(100, 50, 50, 50);
        c.addActionListener(e -> textArea.setText(""));
        c.setVisible(true);
        calculator.add(c);

        JButton percent = new JButton("%");
        percent.setBounds(150, 50, 50, 50);
        percent.addActionListener(e -> textArea.setText(Objects.requireNonNull(simpleDetermineAndSolve(textArea.getText(), percent.getText())).toString()));
        percent.setVisible(true);
        calculator.add(percent);

        JButton sqrt = new JButton("√");
        sqrt.setBounds(200, 50, 50, 50);
        sqrt.addActionListener(e -> textArea.setText(Objects.requireNonNull(simpleDetermineAndSolve(textArea.getText(), sqrt.getText())).toString()));
        sqrt.setVisible(true);
        calculator.add(sqrt);

        JButton advanced = new JButton("Advanced Functions");
        advanced.setBounds(250, 320, 100, 30);
        advanced.addActionListener(e -> makeAdvancedFrame(calculator));
        advanced.setVisible(true);
        calculator.add(advanced);

        int j = 0;
        for (int i = 1; i < numericButtons.length; i++) {
            if ((i - 1) % 3 == 0) j++;
            numericButtons[i] = new JButton(Integer.toString(i));
            numericButtons[i].setBounds(100 + 50 * ((i - 1) % 3), 250 - 50 * j, 50, 50);
        }

        for (JButton b : numericButtons) {
            b.addActionListener(e -> {
                if (clear) textArea.setText("");
                textArea.append(b.getText());
                clear = false;
            });
            b.setVisible(true);
            calculator.add(b);
        }

        calculator.add(textArea);
        calculator.setVisible(true);

        window.add(calculator);
        window.setVisible(true);
    }

    private void makeAdvancedFrame(JPanel p) {
        CalculatorAdvancedFunctions caf = new CalculatorAdvancedFunctions();

        window.remove(p);
        window.revalidate();
        window.repaint();

        JPanel adv = new JPanel();
        adv.setBounds(0, 0, 400, 300);
        adv.setLayout(null);
        adv.setVisible(true);

        String[] allFunctions = caf.returnAllFunctions();
        JComboBox functionList = new JComboBox(allFunctions);
        functionList.setBounds(0, 0, 120, 30);
        functionList.setSelectedIndex(0);
        functionList.addItemListener(e -> {
            int index = Arrays.asList(allFunctions).indexOf(Objects.requireNonNull(functionList.getSelectedItem()).toString());
            switch (index) {
                case 0 -> {
                    revalidateJPanel(adv);
                    adv.add(functionList);
                }
                case 1 -> {
                    revalidateJPanel(adv);
                    adv.add(functionList);
                    adv.add(caf.quadraticZeroes());
                }
                case 2 -> {
                    revalidateJPanel(adv);
                    adv.add(functionList);
                    adv.add(caf.pythagorean());
                }
                case 3 -> {
                    revalidateJPanel(adv);
                    adv.add(functionList);
                    adv.add(caf.derivativeFinder());
                }
                default -> System.out.println("got em");
            }
        });
        functionList.setVisible(true);
        adv.add(functionList);

        window.add(adv);
        window.revalidate();
        window.repaint();
    }

    private void revalidateJPanel(@NotNull JPanel j) {
        j.removeAll();
        j.revalidate();
        j.repaint();
    }

    private void addActionsAndAttach(@NotNull JPanel j, JTextArea textArea, @NotNull JButton button) {
        button.addActionListener(e -> {
            if (operationBeingUsed) {
                Object answer = determineAndSolve(textArea.getText());
                if (answer == null) textArea.setText("");
                    //else if (answer instanceof Integer) textArea.setText("" + (int) answer);
                else if (answer instanceof Float) textArea.setText("" + (float) answer);
                else if (answer instanceof Long) textArea.setText("" + (long) answer);
                else {
                    textArea.setText("");
                    System.out.println("Something went really wrong " + answer);
                }
                operationBeingUsed = false;
                clear = false;
            }
            else {
                textArea.append(button.getText());
                operationBeingUsed = true;
            }
        });
        button.setVisible(true);
        j.add(button);
    }

    private @Nullable Object determineAndSolve(@NotNull String equation) {
        String[] equationParts = equation.split(" ");
        boolean isFloat = isAFloatEquation(equationParts[0], equationParts[2]);
        switch (equationParts[1]) {
            case "+" -> {
                if (isFloat)
                    return cf.addition(Float.parseFloat(equationParts[0]), Float.parseFloat(equationParts[2]));
                return cf.addition(Long.parseLong(equationParts[0]), Long.parseLong(equationParts[2]));
            }
            case "-" -> {
                if (isFloat)
                    return cf.subtraction(Float.parseFloat(equationParts[0]), Float.parseFloat(equationParts[2]));
                return cf.subtraction(Long.parseLong(equationParts[0]), Long.parseLong(equationParts[2]));
            }
            case "x" -> {
                if (isFloat)
                    return cf.multiplication(Float.parseFloat(equationParts[0]), Float.parseFloat(equationParts[2]));
                return cf.multiplication(Long.parseLong(equationParts[0]), Long.parseLong(equationParts[2]));
            }
            case "÷" -> {
                if (isFloat)
                    return cf.division(Float.parseFloat(equationParts[0]), Float.parseFloat(equationParts[2]));
                return cf.division(Long.parseLong(equationParts[0]), Long.parseLong(equationParts[2]));
            }
            default -> System.out.println("Error in equation");
        }
        return null;
    }

    private @Nullable Object simpleDetermineAndSolve(@NotNull String equation, String operation) {
        int index = equation.lastIndexOf(" ");
        String numberInQuestion;
        if (index > 0) numberInQuestion = equation.substring(index + 1);
        else numberInQuestion = equation;
        float thing = Float.parseFloat(numberInQuestion);

        try {
            System.out.println("cool");
            switch (operation) {
                case "%" -> thing = cf.percent(thing);
                case "√" -> thing = cf.squareRoot(thing);
                default -> System.out.println("oops");
            }
            System.out.println("done " + thing);
        }
        catch (Exception e) {
            System.out.println("Something went wrong");
            return null;
        }
        return thing;
    }

    private boolean isAFloatEquation(@org.jetbrains.annotations.NotNull String a, @NotNull String b) {
        return a.contains(".") || b.contains(".");
    }
}
