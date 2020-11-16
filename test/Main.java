import com.bham.pij.assignments.calculator.CalculatorT;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        CalculatorT calculator = new CalculatorT();
        String s;
        while ((s = new Scanner(System.in).nextLine()) != null) {
            System.out.println(calculator.evaluate(s));
        }
    }
}
