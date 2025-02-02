import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;

//import static java.lang.System.mapLibraryName;
import static java.lang.System.out;

public class ChemistrySuite {
    HashMap<String, ElementProperty> elementMap;
    HashMap<String, Integer> allElements;
    final String[] allFunctions = new String[]{
            "",
            "Molar Mass",
            "Molarity",
            "Limiting Reagent",
            "Percent Yield",
            "Pressure",
            "Ideal Gas",
            "Density"
    };
    Dimension WINDOW_DIMENSIONS = new Dimension(800, 500);
    DecimalFormat df;
    public ChemistrySuite() {
        elementMap = makeElementMap();
        df = new DecimalFormat("#.###");
        df.setRoundingMode(RoundingMode.HALF_UP);

        JFrame frame = new JFrame("Chemistry Suite");
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();

        //set spawn in the left of the screen
        frame.setBounds(d.width / 2 - WINDOW_DIMENSIONS.width / 2, d.height / 2 - WINDOW_DIMENSIONS.height / 2, WINDOW_DIMENSIONS.width, WINDOW_DIMENSIONS.height);
        frame.setResizable(false);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        addDropdownPanel(frame);

        frame.setVisible(true);
    }

    //create master and dropdown panel
    void addDropdownPanel(@NotNull JFrame frame) {
        JPanel dropdownPanel = new JPanel();
        dropdownPanel.setBounds(0, 0, WINDOW_DIMENSIONS.width / 8, WINDOW_DIMENSIONS.height);
        dropdownPanel.setLayout(null);

        JPanel cards = new JPanel();
        cards.setBounds(WINDOW_DIMENSIONS.width / 8, 0, WINDOW_DIMENSIONS.width - WINDOW_DIMENSIONS.width / 8, WINDOW_DIMENSIONS.height);
        cards.setLayout(new CardLayout());

        //add all panels
        addBlank(cards);
        addMolarMass(cards);
        addMolarity(cards);

        cards.setVisible(true);

        JComboBox<String> dropdown = new JComboBox<>();
        dropdown.setBounds(10, 10, 80, 20);
        dropdown.setEditable(false);
        for (String function : allFunctions) {
            dropdown.addItem(function);
        }
        dropdown.addActionListener(e -> {
            CardLayout cl = (CardLayout) cards.getLayout();
            cl.show(cards, dropdown.getItemAt(dropdown.getSelectedIndex()));
        });
        dropdown.setVisible(true);
        dropdownPanel.add(dropdown);
        dropdownPanel.setVisible(true);

        frame.add(dropdown);
        frame.add(cards);
    }

    //---JPanel methods---
    void addBlank(@NotNull JPanel cards) {
        JPanel panel = new JPanel();
        panel.setBounds(0, 0, cards.getWidth(), cards.getHeight());
        panel.setLayout(null);

        cards.add(panel, allFunctions[0]);
    }

    void addMolarMass(@NotNull JPanel cards) {
        final double[] mm = new double[1];

        JPanel panel = new JPanel();
        panel.setBounds(0, 0, cards.getWidth(), cards.getHeight());
        panel.setLayout(null);

        //Molar Mass
        JLabel enterHere = new JLabel("Enter chemical formula:");
        enterHere.setBounds(10, 50, 150, 30);
        enterHere.setVisible(true);
        panel.add(enterHere);

        JLabel answer = new JLabel("Molar Mass");
        answer.setBounds(120, 110, 250, 30);
        answer.setVisible(true);
        panel.add(answer);

        JTextField formula = new JTextField();
        formula.setBounds(160, 50, 200, 30);
        formula.setEditable(true);
        formula.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    mm[0] = updateMolarMass(formula, answer);
                    //mm[0] = calculateMolarMassAI(formula.getText());
                    //mm[0] = calculateMolarMassAI2(formula.getText());
                    answer.setText("Molar Mass: " + df.format(mm[0]) + " g/mol");
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        formula.setVisible(true);
        panel.add(formula);

        JButton solve = new JButton("Solve");
        solve.setBounds(10, 100, 100, 50);
        solve.addActionListener(e -> {
            mm[0] = updateMolarMass(formula, answer);
            //mm[0] = calculateMolarMassAI(formula.getText());
            //mm[0] = calculateMolarMassAI2(formula.getText());
            answer.setText("Molar Mass: " + df.format(mm[0]) + " g/mol");
        });
        solve.setVisible(true);
        panel.add(solve);

        //Molar Mass Percent
        JLabel enterElementHere = new JLabel("Enter element here:");
        enterElementHere.setBounds(10, 250, 150, 30);
        enterElementHere.setVisible(true);
        panel.add(enterElementHere);

        JLabel percent = new JLabel("Molar Mass Percent");
        percent.setBounds(120, 310, 250, 30);
        percent.setVisible(true);
        panel.add(percent);

        JTextField element = new JTextField();
        element.setBounds(160, 250, 200, 30);
        element.setEditable(true);
        element.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    percent.setText(calculateMMPercent(element.getText(), mm[0]));
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        element.setVisible(true);
        panel.add(element);

        JButton solve2 = new JButton("Solve");
        solve2.setBounds(10, 300, 100, 50);
        solve2.addActionListener(e -> percent.setText(calculateMMPercent(element.getText(), mm[0])));
        solve2.setVisible(true);
        panel.add(solve2);

        //Element Finder
        JLabel finder = new JLabel("Element Finder");
        finder.setBounds(500, 50, 100, 20);
        finder.setVisible(true);
        panel.add(finder);

        JLabel elementFound = new JLabel();
        elementFound.setBounds(500, 110, 100, 20);
        elementFound.setVisible(true);
        panel.add(elementFound);

        JTextField elementFind = new JTextField();
        elementFind.setBounds(500, 80, 100, 30);
        elementFind.setEditable(true);
        elementFind.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    elementFound.setText(findElement(elementFind.getText()));
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        panel.add(elementFind);

        cards.add(panel, allFunctions[1]);
    }

    void addMolarity(@NotNull JPanel cards) {
        JPanel panel = new JPanel();
        panel.setBounds(new Rectangle(cards.getWidth(), cards.getHeight()));
        panel.setLayout(null);

        JLabel molarityText = new JLabel("Molarity");
        molarityText.setBounds(10, 10, 100, 20);
        molarityText.setVisible(true);
        panel.add(molarityText);

        JLabel moleText = new JLabel("Moles");
        moleText.setBounds(10, 50, 100, 20);
        moleText.setVisible(true);
        panel.add(moleText);

        JLabel volumeText = new JLabel("Volume");
        volumeText.setBounds(10, 90, 100, 20);
        volumeText.setVisible(true);
        panel.add(volumeText);

        JTextField molarity = new JTextField();
        molarity.setBounds(150, 10, 100, 20);
        molarity.setEditable(true);

        JTextField moles = new JTextField();
        moles.setBounds(150, 50, 100, 20);
        moles.setEditable(true);

        JTextField volume = new JTextField();
        volume.setBounds(150, 90, 100, 20);
        volume.setEditable(true);

        KeyListener kl = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    System.out.println(calculateMolarity(molarity.getText(), moles.getText(), volume.getText()));
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        };

        molarity.addKeyListener(kl);
        molarity.setVisible(true);
        panel.add(molarity);

        moles.addKeyListener(kl);
        moles.setVisible(true);
        panel.add(moles);

        volume.addKeyListener(kl);
        volume.setVisible(true);
        panel.add(volume);

        ActionListener al = e -> {

        };

        cards.add(panel, allFunctions[2]);
    }

    //---additional functions---
    HashMap<String, ElementProperty> makeElementMap() {
        HashMap<String, ElementProperty> elementMap = new HashMap<>(118);
        try (Scanner data = new Scanner(new File("p_table.dat"))) {
            int atomicNumber = 1;
            while (data.hasNextLine()) {
                String[] theLine = data.nextLine().split(" ");
                ElementProperty element = new ElementProperty();
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

    //rewrite using hashmap lol what is this wannabe buffer
    //well i don't feel like rewriting this, so here's an added hashmap
    //OMGosh this is bad code
    //it works if there are no parentheses or double-digit subscripts
    double updateMolarMass(@NotNull JTextField formula, JLabel answer) {
        double molarMass = 0;
        HashMap<String, Integer> theElements = new HashMap<>();
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
                                Double thing = calculateMM(element, Character.getNumericValue(theFormula[index]));
                                if (thing != null) {
                                    molarMass += thing;
                                    incrementMap(theElements, element, Character.getNumericValue(theFormula[index++]));
                                }
                            }
                            else {
                                Double thing = calculateMM(element);
                                if (thing != null) {
                                    molarMass += calculateMM(element);
                                    incrementMap(theElements, element);
                                }
                            }
                        }
                        else {
                            Double thing = calculateMM(element);
                            if (thing != null) {
                                molarMass += calculateMM(element);
                                incrementMap(theElements, element);
                            }
                        }
                    }

                    else if (Character.isDigit(theFormula[index])) {
                        Double thing = calculateMM(element, Character.getNumericValue(theFormula[index]));
                        if (thing != null) {
                            molarMass += thing;
                            incrementMap(theElements, element, Character.getNumericValue(theFormula[index++]));
                        }
                    }
                    else {
                        Double thing = calculateMM(element);
                        if (thing != null) {
                            molarMass += calculateMM(element);
                            incrementMap(theElements, element);
                        }
                    }
                }
                else {
                    Double thing = calculateMM(element);
                    if (thing != null) {
                        molarMass += calculateMM(element);
                        incrementMap(theElements, element);
                    }
                }
            }
            else if (Character.isDigit(theFormula[index]))
                index++;
            else break;
        }
        //out.println(molarMass);

        answer.setText("Molar Mass: " + df.format(molarMass) + " g/mol");
        allElements = theElements;
        return molarMass;
    }

    double calculateMolarMassAI(@NotNull String equation) {
        double molarMass = 0;
        char[] equationArray = equation.toCharArray();
        int index = 0;

        while (index < equationArray.length) {
            char currentChar = equationArray[index];

            if (Character.isUpperCase(currentChar)) {
                StringBuilder elementSymbol = new StringBuilder();
                elementSymbol.append(currentChar);

                index++;
                while (index < equationArray.length && Character.isLowerCase(equationArray[index])) {
                    elementSymbol.append(equationArray[index]);
                    index++;
                }

                int subscript = 0;
                while (index < equationArray.length && Character.isDigit(equationArray[index])) {
                    subscript = subscript * 10 + Character.getNumericValue(equationArray[index]);
                    index++;
                }
                if (subscript == 0)
                    subscript = 1;

                String potentialElement = elementSymbol.toString();
                if (elementMap.containsKey(potentialElement)) {
                    molarMass += elementMap.get(potentialElement).molarMass * subscript;
                }
            }
            else if (currentChar == '(') {
                index++;
                int closingIndex = findClosingParenthesis(equationArray, index);
                String subEquation = new String(equationArray, index, closingIndex - 1);
                index = closingIndex + 1;

                int subscript = 1;
                while (index < equationArray.length && Character.isDigit(equationArray[index]))
                    subscript = subscript * 10 + Character.getNumericValue(equationArray[index++]);

                molarMass += calculateMolarMassAI(subEquation) * subscript;
            }
            else index++;
            out.println(molarMass);
        }

        return molarMass;
    }

    double calculateMolarMassAI2(@NotNull String equation) {
        return internalMolarMassAI(equation.toCharArray(), 0).molarMass;
    }

    Result internalMolarMassAI(char @NotNull [] equationArray, int start) {
        double molarMass = 0;
        int index = start;

        while (index < equationArray.length) {
            char currentChar = equationArray[index];

            if (Character.isUpperCase(currentChar)) {
                StringBuilder element = new StringBuilder();
                element.append(currentChar);

                index++;
                while (index < equationArray.length && Character.isLowerCase(equationArray[index]))
                    element.append(equationArray[index++]);

                int subscript = 0;
                while (index < equationArray.length && Character.isDigit(equationArray[index]))
                    subscript = subscript * 10 + Character.getNumericValue(equationArray[index++]);

                if (subscript == 0)
                    subscript = 1;

                String theElement = element.toString();
                if (elementMap.containsKey(theElement))
                    molarMass += elementMap.get(theElement).molarMass * subscript;
                out.println(molarMass);
            }
            else if (currentChar == '(') {
                index++;
                int closingIndex = findClosingParenthesis(equationArray, index);
                String subEquation = new String(equationArray, index, closingIndex - 1);
                out.println(subEquation + " " + closingIndex);
                index = closingIndex + 1;

                int subscript = 0;
                while (index < equationArray.length && Character.isDigit(equationArray[index]))
                    subscript = subscript * 10 + Character.getNumericValue(equationArray[index++]);

                if (subscript == 0)
                    subscript = 1;

                Result subResult = internalMolarMassAI(equationArray, index);
                out.println(subResult.molarMass + " " + subResult.nextIndex);
                molarMass += subResult.molarMass * subscript;

                index = subResult.nextIndex;
            }
            else if (currentChar == ')')
                return new Result(molarMass, index + 1);
            else index++;
        }
        return new Result(molarMass, index);
    }

    int findClosingParenthesis(char @NotNull [] elementArray, int start) {
        int parenthesisCount = 1;
        int index = start;
        while (index < elementArray.length && parenthesisCount > 0) {
            if (elementArray[index] == '(')
                parenthesisCount++;
            else if (elementArray[index] == ')')
                parenthesisCount--;
            index++;
        }
        return index - 1;
    }

    boolean checkIndex(int index, int len) {
        return index < len;
    }

    void incrementMap(HashMap<String, Integer> theElements, String element, int sub) {
        if (elementMap.containsKey(element)) {
            if (theElements.containsKey(element))
                theElements.put(element, theElements.get(element) + sub);
            else theElements.put(element, sub);
        }
    }

    void incrementMap(HashMap<String, Integer> theElements, String element) {
        incrementMap(theElements, element, 1);
    }

    Double calculateMM(String element, int sub) {
        if (!elementMap.containsKey(element))
            return null;
        return elementMap.get(element).molarMass * sub;
    }

    Double calculateMM(String element) {
        return calculateMM(element, 1);
    }

    String calculateMMPercent(String element, double molarMass) {
        if (!elementMap.containsKey(element) || allElements == null || !allElements.containsKey(element)) {
            return "Invalid Input";
        }
        DecimalFormat df = new DecimalFormat("#.###");
        df.setRoundingMode(RoundingMode.HALF_UP);

//        System.out.println(allElements.keySet());
//        System.out.println(allElements.entrySet());

        return "Molar Mass % of " + element + " is " + df.format(elementMap.get(element).molarMass * allElements.get(element) / molarMass * 100) + "%";
    }

    String findElement(String element) {
        if (elementMap.containsKey(element))
            return elementMap.get(element).name;
        for (String e : elementMap.keySet()) {
            if (elementMap.get(e).name.equalsIgnoreCase(element))
                return e;
        }
        return "Invalid Input";
    }

    String calculateMolarity(String molarity, String moles, String volume) {
        int numOfKnowns = 0;

        if (isNumeric(molarity))
            numOfKnowns++;
        if (isNumeric(moles))
            numOfKnowns++;
        if (isNumeric(volume))
            numOfKnowns++;

        if (numOfKnowns < 2)
            return "Invalid Input";

        if (!isNumeric(molarity))
            return "Molarity: " + (Float.parseFloat(moles) / Float.parseFloat(volume));
        else if (!isNumeric(moles))
            return "# of mol" + (Float.parseFloat(molarity) * Float.parseFloat(volume));
        else return "Volume: " + (Float.parseFloat(moles) / Float.parseFloat(molarity));
    }

    String balancingEquation(@NotNull String equation) {
        char[] theEquation = equation.toCharArray();
        int index = 0;

        while (index < theEquation.length) {
            char currentChar = theEquation[index];
            
        }

        //placeholder
        return null;
    }

    boolean isNumeric(String potentialNum) {
        if (potentialNum == null || potentialNum.isEmpty())
            return false;
        return potentialNum.chars().allMatch(Character::isDigit) || potentialNum.matches("-?\\d+(\\.\\d+)?");
    }

    class Result {
        double molarMass;
        int nextIndex;

        Result(double molarMass, int nextIndex) {
            this.molarMass = molarMass;
            this.nextIndex = nextIndex;
        }
    }
}

//extra classes
class ElementProperty {
    public int atomicNumber;
    public String name;
    public double molarMass;
}
