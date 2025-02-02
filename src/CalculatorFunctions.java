import java.lang.Math;

public class CalculatorFunctions {
    public int addition(int a, int b) {
        return a + b;
    }

    public float addition(float a, float b) {
        return a + b;
    }

    public int subtraction(int a, int b) {
        return a - b;
    }

    public float subtraction(float a, float b) {
        return a - b;
    }

    public long multiplication(int a, int b) {
        return (long) a * b;
    }

    public float multiplication(float a, float b) {
        return a * b;
    }

    public float division(float a, float b) {
        return a / b;
    }

    public float percent(float a) {
        return a / 100;
    }

    public float squareRoot(float a) {
        return (float) Math.sqrt(a);
    }
}
