import java.lang.Math;
import java.util.regex.Pattern;
import static java.lang.System.out;

public class MinefieldGenerator {
    private boolean[][] minefield;
    private int[][] adjacentNums;
    public int numberMines;

    //constructors
    public MinefieldGenerator() {
        minefield = new boolean[10][10];
        adjacentNums = new int[10][10];
    }

    public MinefieldGenerator(int n) {
        super();
        if (n > 0) {
            minefield = new boolean[n][n];
            adjacentNums = new int[n][n];
        }
    }

    public MinefieldGenerator(int r, int c) {
        super();
        if (r > 0 && c > 0) {
            minefield = new boolean[r][c];
            adjacentNums = new int[r][c];
        }
    }

    //actual methods
    public void generateMines(int code) {
        int mines = numOfMines(code);
        numberMines = mines;
        while (mines > 0) {
            int row = (int)(Math.random() * minefield.length);
            int column = (int)(Math.random() * minefield[0].length);
            if (!minefield[row][column]) {
                minefield[row][column] = true;
                mines--;
                for (int i = row - 1; i <= row + 1; i++)
                    for (int j = column - 1; j <= column + 1; j++)
                        if (i >= 0 && i < minefield.length && j >= 0 && j < minefield[0].length)
                            if (!minefield[i][j]) adjacentNums[i][j]++;
                            else adjacentNums[i][j] = 9;
            }
        }
    }

    public void generateMines(int code, int r, int c) {
        int mines = numOfMines(code);
        while (mines > 0) {
            int row = (int)(Math.random() * minefield.length);
            int column = (int)(Math.random() * minefield[0].length);
            if (!minefield[row][column] && ((r < r - 1 || r > r + 1) && (c < c - 1 || c > c + 1))) {
                minefield[row][column] = true;
                mines--;
                for (int i = row - 1; i <= row + 1; i++)
                    for (int j = column - 1; j <= column + 1; j++)
                        if (i >= 0 && i < minefield.length && j >= 0 && j < minefield[0].length)
                            if (!minefield[i][j]) adjacentNums[i][j]++;
                            else adjacentNums[i][j] = 9;
            }
        }
    }

    public boolean[][] generateMines(String code) {
        if (Pattern.matches("\\d{4}[\\-\\s][a-zA-Z]{4}[\\-\\s][\\w]{4}[\\-\\s]\\d{4}", code)) {
            String[] part = code.split("-");
        }
        return minefield;
    }

    public int numOfMines(int code) {
        return switch (code) {
            case 0 -> 25;
            case 1 -> 50;
            case 2 -> 99;
            case 3 -> 150;
            default -> code;
        };
    }

    //utility methods
    public boolean[][] getMinefield() {
        return minefield;
    }

    public int[][] getAdjacentNums() {
        return adjacentNums;
    }

    public void printVisualMinefield() {
        for (boolean[] one: minefield) {
            for (boolean two: one) out.print(two?1:0);
            out.println();
        }
    }

    public void printNumericMinefield() {
        for (int[] one: adjacentNums) {
            for (int two: one) out.print(two);
            out.println();
        }
    }

//    public static void main(String[] args) {
//        JFrame window = new JFrame("Minesweeper");
//
//        JLabel label = new JLabel("Minesweeper");
//        label.setFont(new Font("SansSerif", Font.PLAIN, 20));
//        label.setBounds(0, 0, 200, 30);
//
//        window.add(label);
//
//        window.setSize(500, 500);
//        window.setBackground(Color.WHITE);
//        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        window.setLayout(null);
//        window.setVisible(true);
//    }
}
