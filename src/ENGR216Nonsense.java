import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;

public class ENGR216Nonsense {
    final String[] allFunctions = new String[]{
            "",
            "Basics"
    };
    Dimension WINDOW_DIMENSIONS = new Dimension(800, 500);
    DecimalFormat df;
    public ENGR216Nonsense(){
        df = new DecimalFormat("#.####");
        df.setRoundingMode(RoundingMode.HALF_UP);

        JFrame frame = new JFrame("ENGR 216 Nonsense");
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();

        //set spawn in the left of the screen
        frame.setBounds(d.width / 2 - WINDOW_DIMENSIONS.width / 2, d.height / 2 - WINDOW_DIMENSIONS.height / 2, WINDOW_DIMENSIONS.width, WINDOW_DIMENSIONS.height);
        frame.setResizable(false);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        addDropdownPanel(frame);

        frame.setVisible(true);
    }

    void addDropdownPanel(@NotNull JFrame frame) {
        JPanel dropdownPanel = new JPanel();
        dropdownPanel.setBounds(0, 0, WINDOW_DIMENSIONS.width - WINDOW_DIMENSIONS.width / 8, WINDOW_DIMENSIONS.height);
        dropdownPanel.setLayout(null);

        JPanel cards = new JPanel();
        cards.setBounds(WINDOW_DIMENSIONS.width / 8, 0, WINDOW_DIMENSIONS.width - WINDOW_DIMENSIONS.width / 8, WINDOW_DIMENSIONS.height);
        cards.setLayout(new CardLayout());

        // add all panels
        addBlank(cards);
        addBasics(cards);

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

    // JPanel methods
    void addBlank(@NotNull JPanel cards) {
        JPanel panel = new JPanel();
        panel.setBounds(0, 0, cards.getWidth(), cards.getHeight());
        panel.setLayout(null);

        cards.add(panel, allFunctions[0]);
    }

    void addBasics(@NotNull JPanel cards) {
        ArrayList<Double> dataPoints = new ArrayList<>();

        JPanel panel = new JPanel();
        panel.setBounds(0, 0, cards.getWidth(), cards.getHeight());
        panel.setLayout(null);

        JLabel enterHere = new JLabel("Enter data point:");
        enterHere.setBounds(10, 50, 120, 30);
        enterHere.setVisible(true);
        panel.add(enterHere);

        JLabel invalidWarning = new JLabel();
        invalidWarning.setBounds(10, 80, 80, 20);
        invalidWarning.setVisible(true);
        panel.add(invalidWarning);

        JLabel nElements = new JLabel();
        nElements.setBounds(10, 100, 50, 30);
        nElements.setVisible(true);
        panel.add(nElements);

        JLabel mean = new JLabel();
        mean.setBounds(10, 130, 150, 30);
        mean.setVisible(true);
        panel.add(mean);

        JLabel median = new JLabel();
        median.setBounds(10, 160, 150, 30);
        median.setVisible(true);
        panel.add(median);

//        JLabel mode = new JLabel();

        JTextField dataEntry = new JTextField();
        dataEntry.setBounds(130, 50, 200, 30);
        dataEntry.setEditable(true);
        dataEntry.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    try {
                        dataPoints.add(Double.parseDouble(dataEntry.getText()));
                        // n size
                        nElements.setText("n = " + dataPoints.size());
                        // avg
                        final double[] sum = {0};
                        dataPoints.forEach((num) -> sum[0] += num);
                        mean.setText("mean = " + df.format(sum[0] / dataPoints.size()));
                        // median
                        dataPoints.sort(Comparator.naturalOrder());
                        int half = dataPoints.size() / 2;
                        median.setText("median = " + (dataPoints.size() % 2 == 1 ?
                                "" + dataPoints.get(half) :
                                "" + ((dataPoints.get(half - 1) + dataPoints.get(half)) / 2)));

                        invalidWarning.setText("");
                    } catch (Exception f) {
                        invalidWarning.setText("invalid input");
                    }
                    dataEntry.setText("");
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        panel.add(dataEntry);

        cards.add(panel, allFunctions[1]);
    }
}
