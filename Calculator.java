import java.util.ArrayList;

public class Calculator {
    private ArrayList<Double> numbers;

    public Calculator(ArrayList<Double> numbers) {
        this.numbers = numbers;
    }

    public double sum() {
        return numbers.get(0) + numbers.get(1);
    }

    public double subtraction() {
        return numbers.get(0) - numbers.get(1);
    }

    public double multiplication() {
        return numbers.get(0) * numbers.get(1);
    }

    public double division() {
        return numbers.get(0) / numbers.get(1);
    }

    public double squareRoot() {
        return Math.sqrt(numbers.get(0));
    }

    public double sine() {
        return Math.sin(numbers.get(0));
    }

    public double cosine() {
        return Math.cos(numbers.get(0));
    }
}