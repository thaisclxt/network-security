import java.util.ArrayList;

public class Calculator {
    private ArrayList<Double> numbers;

    public Calculator() {
        numbers = new ArrayList<>();
    }

    public void addNumber(double number) {
        numbers.add(number);
    }

    public double add() {
        double result = 0;
        for (double number : numbers) {
            result += number;
        }
        return result;
    }

    public double subtract() {
        if (numbers.isEmpty()) {
            return 0;
        }
        double result = numbers.get(0);
        for (int i = 1; i < numbers.size(); i++) {
            result -= numbers.get(i);
        }
        return result;
    }

    public double multiply() {
        if (numbers.isEmpty()) {
            return 0;
        }
        double result = 1;
        for (double number : numbers) {
            result *= number;
        }
        return result;
    }

    public double divide() {
        if (numbers.isEmpty()) {
            return 0;
        }
        double result = numbers.get(0);
        for (int i = 1; i < numbers.size(); i++) {
            if (numbers.get(i) == 0) {
                throw new ArithmeticException("Division by zero.");
            }
            result /= numbers.get(i);
        }
        return result;
    }

    public double sqrt() {
        if (numbers.isEmpty()) {
            return 0;
        }
        return Math.sqrt(numbers.get(0));
    }

    public double sin() {
        if (numbers.isEmpty()) {
            return 0;
        }
        return Math.sin(Math.toRadians(numbers.get(0)));
    }

    public double cos() {
        if (numbers.isEmpty()) {
            return 0;
        }
        return Math.cos(Math.toRadians(numbers.get(0)));
    }
}