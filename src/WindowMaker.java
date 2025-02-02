import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import static java.lang.System.out;

public class WindowMaker {
    //filepath for dat file w/all options
    String FILEPATH = "list_of_options.dat";
    
    //window dimensions for the window (width, height)
    Dimension WINDOW_DIMENSIONS = new Dimension(240, 200);

    JFrame frame;
    Object obj;
    int itemIndex;

    //likely will not be used
    public WindowMaker() {
        frame = new JFrame();
        finishWindow();
    }

    public WindowMaker(String name) {
        frame = new JFrame(name);
        finishWindow();
    }

    void finishWindow() {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        
        //set spawn in the left of the screen
        frame.setBounds(d.width / 8 - WINDOW_DIMENSIONS.width / 2, d.height / 2 - WINDOW_DIMENSIONS.height / 2, WINDOW_DIMENSIONS.width, WINDOW_DIMENSIONS.height);
        frame.setResizable(false);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(dropDownMenu());
        itemIndex = -1;
        frame.setVisible(true);
    }

    JPanel dropDownMenu() {
        //create default panel where dropdown menu is stored
        JPanel panel = new JPanel();
        panel.setSize(frame.getSize());
        panel.setLayout(null);

        //create dropdown menu
        JComboBox<String> dropDown = new JComboBox<>();
        dropDown.setBounds(10, 10, 200, 20);
        try {
/*            BufferedReader br = new BufferedReader(new FileReader(FILEPATH));
            for (int i = 0; i < Integer.parseInt("" + br.read()); i++)
                dropDown.addItem(br.readLine());*/
            //default value
            dropDown.addItem("");

            //retrieve all names
            Scanner data = new Scanner(new File(FILEPATH));
            while(data.hasNextLine())
                dropDown.addItem(data.nextLine());
            data.close();
        }
        catch (IOException e) {
            out.println("Something happened");
        }
        dropDown.addActionListener(e -> {
            int index = dropDown.getSelectedIndex();
            out.println(index);
            out.println(dropDown.getItemAt(index));
            if (itemIndex != index) {
                itemIndex = index;
                switch (index) {
                    //minesweeper
                    case 1:
                        out.println("Minesweeper");
                        break;
                    case 2:
                        out.println("Kinematics");
                        break;
                    case 3:
                        out.println("Calculator");
                        obj = new CalculatorWindow();
                        break;
                    case 4:
                        out.println("Kinematics + Mach Speed");
                        try {
                            obj = new Physics2Project();
                        } catch (FileNotFoundException ex) {
                            throw new RuntimeException(ex);
                        }
                        break;
                    case 5:
                        out.println("Chart");
                        obj = new Chart("Chart");
                        break;
                    case 6:
                        out.println("MMCalculator");
                        obj = new MMCalculator();
                        break;
                    case 7:
                        out.println("Chemistry Suite");
                        obj = new ChemistrySuite();
                        break;
                    case 8:
                        out.println("ENGR 216 Nonsense");
                        obj = new ENGR216Nonsense();
                        break;
                    case 0:
                    default:
                        out.println("Blank");
                        break;
                }
            }
        });
        dropDown.setEnabled(true);

        //adding dropdown menu to the panel
        panel.add(dropDown);
        panel.setEnabled(true);
        panel.setVisible(true);

        return panel;
    }
}
