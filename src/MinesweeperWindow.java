import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.Duration;
import java.time.Instant;

import static java.lang.System.out;

public class MinesweeperWindow extends JFrame{
    public JFrame window; //setup window
    public JFrame minesweeper; //minesweeper window
    MinefieldGenerator mg; //class to create minesweeper game
    JButton easy, medium, hard, custom;
    public int[] cleared = {0};
    private int emptySpaces;
    Instant start, end;

    public MinesweeperWindow() { //construct initial setup window
        window = new JFrame("Setup");
        window.setSize(516, 539);
        window.setResizable(false);
        window.setBackground(Color.WHITE);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(null);
        window.setVisible(true);
    }

    public void drawBeginning() { //draw the initial setup window
        Container container = window.getContentPane();
        easy = new JButton("Easy"); medium = new JButton("Medium"); hard = new JButton("Hard"); custom = new JButton("Custom"); //initialize all buttons

        //setup title
        JLabel title = new JLabel("Minesweeper", JLabel.CENTER);
        title.setBounds(container.getWidth()/2 - 65, 100 ,100, 20);

        //setup easy button
        easy.setBounds(0, 450, 125, 50);
        easy.addActionListener(e -> {
            out.println("easy");
            buttonsToInvisible();
            mg = new MinefieldGenerator(15);
            mg.generateMines(0);
            newWindow();
        });

        //setup medium button
        medium.setBounds(125, 450, 125, 50);
        medium.addActionListener(e -> {
            out.println("medium");
            buttonsToInvisible();
            mg = new MinefieldGenerator(20);
            mg.generateMines(1);
            newWindow();
        });

        //setup hard button
        hard.setBounds(250, 450, 125, 50);
        hard.addActionListener(e -> {
            buttonsToInvisible();
            mg = new MinefieldGenerator(30);
            int randomNum = (int)(Math.random() * 65535);
            out.println(randomNum);
            if (randomNum < 16) {
                mg.generateMines(3);
                out.println("super hard");
            }
            else {
                mg.generateMines(2);
                out.println("hard");
            }
            newWindow();
        });

        //setup custom button
        custom.setBounds(375, 450, 125, 50);
        custom.addActionListener(e -> {
            out.println("custom");
            buttonsToInvisible();
            softClear(window);

            JLabel question = new JLabel("How many rows, columns, and bombs?");
            question.setBounds(100, 250, 500, 20);

            JTextField tf1 = new JTextField("Rows");
            tf1.setBounds(100, 300, 50, 20);

            JTextField tf2 = new JTextField("Columns");
            tf2.setBounds(100, 350, 50, 20);

            JTextField tf3 = new JTextField("Bombs");
            tf3.setBounds(100, 400, 50, 20);

            JLabel generating = new JLabel();
            generating.setBounds(100, 50, 100, 20);

            JButton generate = new JButton("Generate");
            generate.setBounds(100, 450, 100, 50);
            generate.addActionListener(e1 -> {
                try {
                    int r = Integer.parseInt(tf1.getText());
                    int c = Integer.parseInt(tf2.getText());
                    int mines = Integer.parseInt(tf3.getText());

                    if (r > 5 && c > 5 && mines > 3 && mines < r * c) {
                        mg = new MinefieldGenerator(r, c);
                        mg.generateMines(mines);
                        generating.setText("Generating");
                        newWindow();
                    }
                    else generating.setText("Failed to generate");
                } catch (Exception f) {
                    generating.setText("Failed to generate");
                }
            });

            //adding custom options text fields
            container.add(question);
            container.add(tf1);
            container.add(tf2);
            container.add(tf3);
            container.add(generate);
            container.add(generating);
        });

        //adding buttons
        container.add(title);
        container.add(easy);
        container.add(medium);
        container.add(hard);
        container.add(custom);
    }

    //remove buttons
    public void buttonsToInvisible() {
        easy.setVisible(false);
        medium.setVisible(false);
        hard.setVisible(false);
        custom.setVisible(false);
    }

    public void newWindow() {
        window.dispose();
        displayField();
    }

    //clearing methods
    public void softClear(@NotNull JFrame j) {
        j.getContentPane().invalidate();
        j.getContentPane().validate();
        j.getContentPane().repaint();
    }
    public void softClear(@NotNull Container c) {
        c.invalidate();
        c.validate();
        c.repaint();
    }
    public void hardClear(@NotNull JFrame j) {
        j.getContentPane().removeAll();
        j.getContentPane().validate();
        j.getContentPane().repaint();
    }
    public void hardClear(@NotNull Container c) {
        c.removeAll();
        c.validate();
        c.repaint();
    }

    //printing methods
    public void displayField() {
        minesweeper = new JFrame("Minesweeper");
        //minesweeper.setSize(1000, 1000);
        minesweeper.setResizable(true);
        minesweeper.setBackground(Color.WHITE);
        minesweeper.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        minesweeper.setLayout(null);
        minesweeper.setVisible(true);

        createStaticButtonInteractableField();
        start = Instant.now();
        //printBasicAdjacentNums();
        //testGeneratedWindow(); //debug
    }

    public void createStaticButtonInteractableField() {
        Container container = minesweeper.getContentPane();
        int[][] mineNums = mg.getAdjacentNums();
        JButton[][] buttons = new JButton[mineNums.length][mineNums[0].length];
        minesweeper.setSize(mineNums[0].length * 20 + 16, mineNums.length * 20 + 39);
        emptySpaces = (mineNums.length * mineNums[0].length) - mg.numberMines;

        for (int i = 0; i < mineNums.length; i++) {
            for (int j = 0; j < mineNums[0].length; j++) {
                buttons[i][j] = new JButton();
                Rectangle rectangle = new Rectangle(20 * j, 20 * i, 20, 20);
                buttons[i][j].setBounds(rectangle);
                int finalI = i;
                int finalJ = j;
                buttons[i][j].addActionListener(e -> {
                    if (mineNums[finalI][finalJ] == 9) {
                        try {
                            end = Instant.now();
                            hardClear(minesweeper);
                            JLabel thing = new JLabel(new ImageIcon("explosion.png"));
                            thing.setBounds(0, 0, container.getWidth(), container.getHeight());
                            thing.setVisible(true);
                            container.add(thing);
                            Thread.sleep(2000);
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                        stats(false);
                    }
                    else {
                        cleared[0]++;
                        buttons[finalI][finalJ].setVisible(false);
                        JLabel label = new JLabel(Integer.toString(mineNums[finalI][finalJ]), SwingConstants.CENTER);
                        label.setBounds(rectangle);
                        container.add(label);
                        if (cleared[0] == emptySpaces) {
                            end = Instant.now();
                            stats(true);
                        }
                    }
                });
                container.add(buttons[i][j]);
            }
        }
    }

    public void createInteractableField() {
        Container container = minesweeper.getContentPane();
        int[][] mineNums = mg.getAdjacentNums();
        boolean[][] minefield = mg.getMinefield();
        final int[] xPos = new int[1], yPos = new int[1];

        Graphics g = minesweeper.getGraphics();

        container.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                xPos[0] = e.getXOnScreen();
                yPos[0] = e.getYOnScreen();
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    public void stats(Boolean b) {
        Container container = minesweeper.getContentPane();
        hardClear(container);
        int width = container.getWidth();
        int height = container.getHeight();

        JLabel label = new JLabel(cleared[0] + "/" + emptySpaces + " spaces cleared", SwingConstants.CENTER);
        label.setBounds(width / 2 - 150, height / 3, 300, 20);
        container.add(label);

        JLabel label1 = new JLabel("Time Elapsed: " + Duration.between(start, end).toSeconds() + " seconds", SwingConstants.CENTER);
        label1.setBounds(width / 2 - 150, height / 3 + 20, 300, 20);
        container.add(label1);

        JLabel label2 = new JLabel();
        label2.setBounds(width / 2 - 150, height / 3 + 40, 300, 20);
        label2.setHorizontalAlignment(SwingConstants.CENTER);
        if (b) label2.setText("CONGRATULATIONS!");
        else label2.setText("better luck next time");
        container.add(label2);
    }

    //utility
    public void printBasicAdjacentNums() {
        Container container = minesweeper.getContentPane();
        int[][] mineNums = mg.getAdjacentNums();
        JLabel num;

        repaint();
        for (int i = 0; i < mineNums.length; i++) {
            for (int j = 0; j < mineNums[0].length; j++) {
                num = new JLabel(Integer.toString(mineNums[i][j]), SwingConstants.CENTER);
                num.setBounds(10 + 20 * j, 20 * i, 10, 20);
                container.add(num);
            }
        }
    }

    public void printBasicMinefield() {
        Container container = minesweeper.getContentPane();
        boolean[][] minefield = mg.getMinefield();
    }

    public void testGeneratedWindow() { //debug
        JLabel label = new JLabel("Test", JLabel.CENTER);
        label.setBounds(500, 500, 100, 20);
        minesweeper.add(label);
    }
}
