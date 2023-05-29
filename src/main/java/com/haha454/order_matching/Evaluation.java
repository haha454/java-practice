package com.haha454.order_matching;

import java.util.Map;
import java.util.function.Function;

class Evaluation {
    private final String expression;
    private final Map<String, Integer> context;
    private int idx;

    public Evaluation(String expression, Map<String, Integer> context) {
        this.expression = expression;
        this.context = context;
        idx = 0;
    }

    private boolean getResult() throws Exception {
        var var = getVar();
        if (var.isEmpty()) {
            assert expression.charAt(idx) == '(';
            idx++;
            return evaluate();
        }
        var value = context.get(var);
        var comparator = getComparator();
        var anotherVar = getVar();
        var num = anotherVar.isEmpty() ? getNum() : context.get(anotherVar);
        return switch (comparator) {
            case ">" -> value > num;
            case ">=" -> value >= num;
            case "<" -> value < num;
            case "<=" -> value <= num;
            case "==" -> value == num;
            default -> throw new Exception(String.format("unknown comparator %s", comparator));
        };
    }

    private String getVar() {
        return getToken(Character::isAlphabetic);
    }

    private String getComparator() {
        return getToken((ch) -> ch == '>' || ch == '<' || ch == '=');
    }

    private int getNum() {
        return Integer.parseInt(getToken(Character::isDigit));
    }

    private String getToken(Function<Character, Boolean> validTokenPredicate) {
        skipSpaces();
        StringBuilder sb = new StringBuilder();
        while (idx < expression.length() && validTokenPredicate.apply(expression.charAt(idx))) {
            sb.append(expression.charAt(idx));
            idx++;
        }

        return sb.toString();
    }

    private void skipSpaces() {
        while (idx < expression.length() && expression.charAt(idx) == ' ') idx++;
    }

    private String getOperator() {
        return getToken((ch) -> ch == '&' || ch == '|');
    }

    public boolean evaluate() throws Exception {
        var currentResult = getResult();
        while (idx < expression.length()) {
            var operator = getOperator();
            if (operator.isEmpty()) {
                assert expression.charAt(idx) == ')';
                idx++;
                return currentResult;
            }
            var nextResult = getResult();
            if (operator.equals("&&")) {
                currentResult &= nextResult;
            } else if (operator.equals("||")) {
                currentResult |= nextResult;
            } else {
                throw new Exception(String.format("unknown operator %s", operator));
            }
        }

        assert idx == expression.length();
        return currentResult;
    }

}
