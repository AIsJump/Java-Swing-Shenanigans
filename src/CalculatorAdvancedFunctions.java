import javax.swing.*;
import org.jetbrains.annotations.*;

public class CalculatorAdvancedFunctions {
    private final String[] allAvailableFunctions;

    public CalculatorAdvancedFunctions() {
        allAvailableFunctions = new String[] {"", "Quadratic Zeroes", "Pythagorean", "Simple Derivatives"};
    }

    public JPanel quadraticZeroes() {
        JPanel quadZero = new JPanel();
        quadZero.setBounds(0, 100, 400, 300);
        quadZero.setLayout(null);

        JLabel zeroes = new JLabel("Zeroes:");
        zeroes.setBounds(50, 150, 200, 30);
        zeroes.setVisible(true);
        quadZero.add(zeroes);

        JTextField[] textFields = new JTextField[3];
        for (int i = 0; i < textFields.length; i++) {
            textFields[i] = new JTextField();
            textFields[i].setBounds(40 + 80 * (i), 50, 20, 20);
            textFields[i].setEditable(true);
            textFields[i].setVisible(true);
            quadZero.add(textFields[i]);
        }

        JButton solve = new JButton("Solve");
        solve.setBounds(260, 45, 70, 30);
        solve.addActionListener(e -> {
            try {
                float[] answers = quadraticZeroesSolution(Float.parseFloat(textFields[0].getText()), Float.parseFloat(textFields[1].getText()),
                        Float.parseFloat(textFields[2].getText()));
                zeroes.setText("Zeroes: " + answers[0] + ", " + answers[1]);
                //System.out.println(Arrays.toString(answers));
            }
            catch (Exception f) {
                zeroes.setText("invalid input");
            }
        });
        solve.setVisible(true);
        quadZero.add(solve);

        quadZero.setVisible(true);

        return quadZero;
    }

    public JPanel pythagorean() {
        JPanel trig = new JPanel();
        trig.setBounds(0, 100, 400, 300);
        trig.setLayout(null);

        JTextField[] textFields = new JTextField[3];
        for (int i = 0; i < textFields.length; i++) {
            textFields[i] = new JTextField();
            textFields[i].setBounds(40 + 80 * (i), 50, 20, 20);
            textFields[i].setEditable(true);
            textFields[i].setVisible(true);
            trig.add(textFields[i]);
        }

        JLabel answer = new JLabel();
        answer.setBounds(50, 150, 200, 30);
        answer.setVisible(true);
        trig.add(answer);

        JButton solve = new JButton("Solve");
        solve.setBounds(260, 45, 70, 30);
        solve.addActionListener(e -> {
            try {
                if (textFields[0].getText().equals("")) {
                    answer.setText("a = " + Math.sqrt(Math.pow(Float.parseFloat(textFields[2].getText()), 2) - Math.pow(Float.parseFloat(textFields[1].getText()), 2)));
                }
                else if (textFields[1].getText().equals("")) {
                    answer.setText("b = " + Math.sqrt(Math.pow(Float.parseFloat(textFields[2].getText()), 2) - Math.pow(Float.parseFloat(textFields[0].getText()), 2)));
                }
                else if (textFields[2].getText().equals("")) {
                    answer.setText("c = " + Math.sqrt(Math.pow(Float.parseFloat(textFields[0].getText()), 2) + Math.pow(Float.parseFloat(textFields[1].getText()), 2)));
                }
                else answer.setText("invalid input");
            }
            catch (Exception f) {
                answer.setText("invalid input");
            }
        });
        solve.setVisible(true);
        trig.add(solve);

        trig.setVisible(true);

        return trig;
    }

    public JPanel derivativeFinder() {
        JPanel deriv = new JPanel();
        deriv.setBounds(0, 100, 400, 300);
        deriv.setLayout(null);

        JLabel dSymbol = new JLabel("d/d");
        dSymbol.setBounds(40, 50, 20, 30);
        dSymbol.setVisible(true);
        deriv.add(dSymbol);

        JTextField variable = new JTextField();
        variable.setBounds(60, 50, 20, 30);
        variable.setEditable(true);
        variable.setVisible(true);
        deriv.add(variable);

        JTextField textField = new JTextField();
        textField.setBounds(110, 50, 200, 30);
        textField.setEditable(true);
        textField.setVisible(true);
        deriv.add(textField);

        JLabel value = new JLabel("variable = ");
        value.setBounds(40, 80, 70, 30);
        value.setVisible(true);
        deriv.add(value);

        JTextField numericalValue = new JTextField();
        numericalValue.setBounds(100, 80, 70, 30);
        numericalValue.setEditable(true);
        numericalValue.setVisible(true);
        deriv.add(numericalValue);

        JLabel answer = new JLabel();
        answer.setBounds(50, 150, 200, 30);
        answer.setVisible(true);
        deriv.add(answer);

        JButton solve = new JButton("Solve");
        solve.setBounds(260, 80, 70, 30);
        solve.addActionListener(e -> {
            try {
                char theVariable = variable.getText().charAt(0);
                if (!((theVariable >= 'A' && theVariable <= 'Z') || (theVariable >= 'a' && theVariable <= 'z')))
                    answer.setText("invalid input");
                else {
                    answer.setText("Derivative: " + derivativeVal(textField.getText(), Integer.parseInt(numericalValue.getText()), theVariable));
                }
            }
            catch (Exception f) {
                answer.setText("invalid input");
            }
        });
        solve.setVisible(true);
        deriv.add(solve);

        deriv.setVisible(true);

        return deriv;
    }

    @Contract("_, _, _ -> new")
    private float @NotNull [] quadraticZeroesSolution(float a, float b, float c) {
        double sqrt = Math.sqrt(Math.pow(b, 2) - (4 * a * c));
        return new float[] {(-b + (float) sqrt) / (2 * a), (-b - (float) sqrt) / (2 * a)};
    }

    private long derivativeTerm(@NotNull String polynomial, long val, char var)
    {
        StringBuilder theString = new StringBuilder();
        int i;
        for (i = 0; polynomial.charAt(i) != var ; i++) {
            if(polynomial.charAt(i)==' ') continue;
            theString.append(polynomial.charAt(i));
        }

        long coeff = Long.parseLong(theString.toString());
        StringBuilder powStr = new StringBuilder();
        for (i = i + 2; i != polynomial.length() && polynomial.charAt(i) != ' '; i++) powStr.append(polynomial.charAt(i));

        long power = Long.parseLong(powStr.toString());
        return coeff * power * (long)Math.pow(val, power - 1);
    }

    private long derivativeVal(@NotNull String poly, int val, char var)
    {
        long ans = 0;
        int i = 0;
        String[] stSplit = poly.split("\\+");
        while(i < stSplit.length) ans = (ans + derivativeTerm(stSplit[i++], val, var));

        return ans;
    }

    public String[] returnAllFunctions() {
        return allAvailableFunctions;
    }
}
