package com.bham.pij.assignments.calculator;
// Ryan Arrowsmith 2132159

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Pattern;

public class Calculator {

    public static final String NUMBER = "([+-]?[0-9]*\\.?[0-9]+|m)";
    public static final String OPERATOR = "[+\\-/*]";
    public static final String SPACE = "[ ]";
    public static final String FUNCTION = "(m|mr|c|h)";
    public static final String OPERATION = "(" + NUMBER + SPACE + OPERATOR + SPACE + ")+" + NUMBER;
    public static final String SHORTENED = OPERATOR + SPACE + "(" + NUMBER + "|" + OPERATION + ")";
    public static final float ERR_VALUE = Float.MIN_VALUE;

    // i gave up here because i didn't know how to match regex for brackets as well

    private float memory = 0;
    private float lastResult = 0;
    private final List<Float> history = new ArrayList<>();

    private boolean isMorePrecedent(String o1, String o2) {
        return (o1.matches("[+-]") ? 1 : 2) > (o2.matches("[+-]") ? 1 : 2);
    }

    public float evaluate(String expression) {

        if (expression.matches(SHORTENED)) {
            expression = memory + " " + expression;
        }
        expression = expression.replaceAll("m", String.valueOf(memory));

        Stack<Integer> bracketIndex = new Stack<>();

        while (expression.contains(")")) {
            for (int i = 0; i < expression.length(); i++) {
                char c = expression.charAt(i);
                if (c == '(') {
                    bracketIndex.push(i);
                } else if (c == ')') {
                    if (bracketIndex.size() == 0) return ERR_VALUE;
                    try {
                        String exp = expression.substring(bracketIndex.pop() + 1, i);
                        expression = expression.replaceAll(Pattern.quote("(" + exp + ")"), "" + evaluation(exp));
                    } catch (StringIndexOutOfBoundsException e) {
                        return ERR_VALUE;
                    }
                    break;
                }
            }
        }
        if (bracketIndex.size() != 0) return ERR_VALUE;

        lastResult = evaluation(expression);

        if (lastResult != ERR_VALUE) history.add(lastResult);
        return lastResult;
    }

    private float evaluation(String expression) {
        if (!expression.matches(OPERATION)) return ERR_VALUE;
        Stack<Float> operandsStack = new Stack<>();
        Stack<String> operatorsStack = new Stack<>();
        String[] strings = expression.split(SPACE);
        String[] reverse = strings.clone();
        for (int i = 0; i < strings.length; i++) strings[strings.length - i - 1] = reverse[i];
        for (String s : strings) {
            if (s.matches(OPERATOR)) {
                while (!operatorsStack.isEmpty() && isMorePrecedent(operatorsStack.peek(), s)) {
                    float f = calculateNext(operandsStack, operatorsStack);
                    if (f == ERR_VALUE) return ERR_VALUE;
                }
                operatorsStack.push(s);
            } else if (s.matches(NUMBER)) {
                operandsStack.push(Float.valueOf(s));
            } else return ERR_VALUE;
        }
        while (!operatorsStack.isEmpty()) {
            float f = calculateNext(operandsStack, operatorsStack);
            if (f == ERR_VALUE) return ERR_VALUE;
        }
        return operandsStack.pop();
    }

    private float calculateNext(Stack<Float> nums, Stack<String> ops) {
        String op = ops.pop();
        float s2 = nums.pop();
        float s1 = nums.pop();
        float r = ERR_VALUE;
        switch (op) {
            case "+": {
                r = (s1 + s2);
                break;
            }
            case "-": {
                r = (s2 - s1);
                break;
            }
            case "/": {
                r = (s1 == 0) ? ERR_VALUE : (s2 / s1);
                break;
            }
            case "*": {
                r = (s1 * s2);
                break;
            }
        }
        nums.push(r);
        return r;
    }

    public float getCurrentValue() {
        return lastResult;
    }

    public List<Float> getHistory() {
        return history;
    }

    public float getHistoryValue(int index) {
        return history.get(index);
    }

    public float getMemoryValue() {
        return memory;
    }

    public void setMemoryValue(float memval) {
        memory = memval;
    }

    public void clearMemory() {
        setMemoryValue(0);
    }
}