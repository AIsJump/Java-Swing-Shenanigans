import javax.swing.*;
import org.jfree.chart.*;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.data.category.DefaultCategoryDataset;

import java.io.*;
import java.util.Scanner;
import java.awt.*;
import static java.lang.System.out;

public class Chart {
    ApplicationFrame frame;
    //hard code height and width
    Dimension WINDOW_DIMENSIONS = new Dimension(500, 500);
    String FILEPATH = "data_points.dat";
    public Chart(String name) {
        frame = new ApplicationFrame(name);
        finishWindow();
    }

    void finishWindow() {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();

        //set spawn in the right of the screen
        frame.setBounds(d.width / 3 * 2 - WINDOW_DIMENSIONS.width / 2, d.height / 2 - WINDOW_DIMENSIONS.height / 2, WINDOW_DIMENSIONS.width, WINDOW_DIMENSIONS.height);
        frame.setResizable(false);
        frame.setLayout(null);
        chart();
        frame.setVisible(true);
    }

    void chart() {
        //attempt to extract data from referenced file
        try {
            //parse all relevant data if formatted properly
            Scanner data = new Scanner(new File(FILEPATH));
            String chartTitle = data.nextLine();
            String[] axis = data.nextLine().split(",");
            DefaultCategoryDataset plotPoints = new DefaultCategoryDataset();
            while (data.hasNext()) {
                plotPoints.addValue(Integer.valueOf(data.next()), data.next(), data.next());
            }
            JFreeChart chart = ChartFactory.createLineChart(
                    chartTitle,                             //title of chart
                    axis[0], axis[1],                       //axis titles; x, y
                    plotPoints,                             //all data points from file
                    PlotOrientation.VERTICAL,               //vertical orientation
                    true, true, false               //legend, tooltips, urls
            );
            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(500, 400));
            frame.setContentPane(chartPanel);
        }
        catch (IOException e) {
            out.println("The file does not exist");
        }
    }
}
