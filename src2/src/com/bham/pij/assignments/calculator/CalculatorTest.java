package com.bham.pij.assignments.calculator;

import java.util.Scanner;
import java.util.stream.Collectors;

import static com.bham.pij.assignments.calculator.Calculator.*;

public class CalculatorTest {

    public static void main(String[] args) {
        Calculator calc = new Calculator();
        Scanner s = new Scanner(System.in);
        CalculatorTest t = new CalculatorTest();
        System.out.println("Enter expressions to evaluate, press q to quit.");
        while (true) {
            String input = s.nextLine();
            if (input.equalsIgnoreCase("q")) break;
            t.loop(calc, input);
        }
    }

    public void loop(Calculator calculator, String input) {
        // If input is "m", "mr", "h", "c"
        if (input.matches(FUNCTION)) {
            switch (input) {
                case "mr": {
                    System.out.println("Memory value: " + calculator.getMemoryValue());
                    return;
                }
                case "m": {
                    float last = calculator.getCurrentValue();
                    // Using a ternary operator here for the sake of less Java boilerplate
                    // If the last result is equal to the error value, set the mem to 0, else the last result.
                    calculator.setMemoryValue(last == ERR_VALUE ? 0 : last);
                    System.out.println("Memory value set to: " + calculator.getMemoryValue());
                    return;
                }
                case "h": {
                    // Map all values of history, convert each to a string and then join the stream with a comma.
                    // Alternatively you could do this with a foreach loop, but I have experience with Java streams.
                    System.out.println(calculator.getHistory().stream().map(String::valueOf).collect(Collectors.joining(", ")));
                    return;
                }
                case "c": {
                    calculator.setMemoryValue(0);
                    System.out.println("Memory cleared");
                    return;
                }
                default: {
                    // Unreachable statement.
                    throw new UnsupportedOperationException();
                }
            }
        }
        float result = calculator.evaluate(input);
        if (result == ERR_VALUE) {
            System.out.println("Invalid Expression");
        } else if (result % 1 == 0) {
            System.out.println((int) result);
        } else {
            System.out.println(result);
        }
    }
}
