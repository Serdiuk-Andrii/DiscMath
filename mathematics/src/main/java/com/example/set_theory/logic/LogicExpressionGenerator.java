package com.example.set_theory.logic;

import static com.example.set_theory.RPN.LogicEvaluator.*;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogicExpressionGenerator {

    private static final Random random = new Random();
    public static char[] operations = {AND, OR, IMPLICATION, XOR, EQUIVALENCE, AND, OR, IMPLICATION};

    public static char[] variables = "abcdefgh".toCharArray();

    private final int negationProbability;

    public LogicExpressionGenerator(int negationProbability) {
        this.negationProbability = negationProbability;
    }

    public String generateExpression(int minNumberOfOperation, int maxNumberOfOperations) {
        int numberOfOperations = random.nextInt(maxNumberOfOperations - minNumberOfOperation)
                + minNumberOfOperation;
        return generateExpression(numberOfOperations);
    }

    public String generateExpression(int numberOfOperations) {
        if (numberOfOperations == 1) {
            int probability = random.nextInt(100);
            if (probability < negationProbability) {
                return "!" + variables[random.nextInt(variables.length)];
            }
            return String.valueOf(variables[random.nextInt(variables.length)]);
        }
        char operation = getRandomOperation();
        int leftSideOperations = numberOfOperations / 2;
        int rightSideOperations = numberOfOperations - leftSideOperations;
        String leftSide = generateExpression(leftSideOperations);
        String rightSide = generateExpression(rightSideOperations);
        while (leftSide.length() == 1 && leftSide.equals(rightSide)) {
            rightSide = generateExpression(rightSideOperations);
        }
        return wrapExpressionLeft(leftSide) + operation + wrapExpressionRight(rightSide);
    }

    private String wrapExpressionLeft(String expression) {
        if (expression.length() < 3) {
            return expression + ' ';
        }
        return "(" + expression + ") ";
    }

    private String wrapExpressionRight(String expression) {
        if (expression.length() < 3) {
            return ' ' + expression;
        }
        return " (" + expression + ")";
    }

    private char getRandomOperation() {
        return operations[random.nextInt(operations.length)];
    }

    public String removeRedundantBrackets(String expression) {
        String pattern = "(\\([a-z]\\s*∧\\s*[a-z]\\)\\s*∧\\s*[a-z])";

        Pattern regex = Pattern.compile(pattern);

        Matcher matcher = regex.matcher(expression);
        while (matcher.find()) {
            expression = expression.replace(matcher.group(0), matcher.group(1));
        }

        return expression;
    }
    
}
