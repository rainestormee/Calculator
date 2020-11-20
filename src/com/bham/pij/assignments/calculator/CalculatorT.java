package com.bham.pij.assignments.calculator;
// Ryan Arrowsmith 2132159
public class CalculatorT {
    private float r = 0, memory = 0, lastResult = 0;
    private final java.util.List<Float> history = new java.util.ArrayList<>();
    public float evaluate(String expression) {
        if (expression.matches("[+\\-/*][ ](([+-]?[0-9]*\\.?[0-9]+|m)|(([+-]?[0-9]*\\.?[0-9]+|m)[ ][+\\-/*][ ])+([+-]?[0-9]*\\.?[0-9]+|m))")) expression = memory + " " + expression;
        java.util.Stack<Integer> bracketIndex = new java.util.Stack<>();
        while (expression.contains(")")) for (int i = 0; i < expression.length(); i++) {
            if (expression.charAt(i) == '(') bracketIndex.push(i);
            else if (expression.charAt(i) == ')') {
                if (bracketIndex.size() == 0) return 0x0.000002P-126f;
                expression = expression.replaceAll(java.util.regex.Pattern.quote("(" + expression.substring(bracketIndex.peek() + 1, i) + ")"), "" + evaluation(expression.substring(bracketIndex.pop() + 1, i).replaceAll("m", String.valueOf(memory))));
                break;
            }
        }
        if ((lastResult = evaluation(expression.replaceAll("m", String.valueOf(memory)))) != 0x0.000002P-126f) history.add(lastResult);
        return lastResult;
    }
    private float evaluation(String expression) {
        if (!expression.matches("(([+-]?[0-9]*\\.?[0-9]+)[ ][+\\-/*][ ])+([+-]?[0-9]*\\.?[0-9]+)")) return 0x0.000002P-126f;
        java.util.Stack<Float> operandsStack = new java.util.Stack<>();
        java.util.Stack<String> operatorsStack = new java.util.Stack<>();
        String[] strings = java.util.stream.Stream.of(expression.split("[ ]")).collect(java.util.stream.Collector.of(java.util.ArrayDeque::new, java.util.ArrayDeque::addFirst, (d1, d2) -> {d2.addAll(d1); return d2;})).stream().map(Object::toString).toArray(String[]::new);
        for (String s : strings) if (s.matches("[+\\-/*]")) {
            while (!operatorsStack.isEmpty() && (operatorsStack.peek().matches("[+-]") ? 1 : 2) > (s.matches("[+-]") ? 1 : 2)) if (this.calculateNext(operandsStack, operatorsStack) == 0x0.000002P-126f) return 0x0.000002P-126f; operatorsStack.push(s);
        } else if (s.matches("([+-]?[0-9]*\\.?[0-9]+)")) operandsStack.push(Float.valueOf(s)); else return 0x0.000002P-126f;
        while (!operatorsStack.isEmpty()) if (this.calculateNext(operandsStack, operatorsStack) == 0x0.000002P-126f) return 0x0.000002P-126f;
        return operandsStack.pop();
    }
    private float calculateNext(java.util.Stack<Float> nums, java.util.Stack<String> ops) {
        if (ops.peek().equals("+")) r = (nums.pop() + nums.pop());
        else if (ops.peek().equals("-")) r = (nums.pop() - nums.pop());
        else if (ops.peek().equals("*")) r = (nums.pop() * nums.pop());
        else {
            r = nums.pop();
            r = (nums.peek() == 0) ? 0x0.000002P-126f : (r / nums.pop());
        }
        ops.pop();
        return nums.push(r);
    }
    public float getCurrentValue() {
        return lastResult;
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
