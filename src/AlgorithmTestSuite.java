import org.jetbrains.annotations.NotNull;

public class AlgorithmTestSuite {
    public static void main(String[] args) {

    }

    void SelectionSort(@NotNull Comparable @NotNull [] theStrs) {
        final int theLength = theStrs.length;
        for (int i = 0; i < theLength - 1; i++) {
            int elementIndex = i;

            for (int j = i + 1; j < theLength; j++) {
                if (theStrs[j].compareTo(theStrs[i]) < 0) {
                    elementIndex = j;
                }
            }

            Comparable temp = theStrs[elementIndex];
            theStrs[elementIndex] = theStrs[i];
            theStrs[i] = temp;
        }
    }
}
