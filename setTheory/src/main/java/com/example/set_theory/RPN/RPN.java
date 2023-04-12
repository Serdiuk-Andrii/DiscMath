package com.example.set_theory.RPN;

import com.example.set_theory.exceptions.UnknownOperatorException;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import kotlin.Function;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;


public class RPN<T> {

    private final String expression;
    private final String postfixExpression;
    private final Set<Character> sets;

    private static boolean isOperand(char symbol) {
        return Character.isLetter(symbol);
    }

    public RPN(@NotNull final String expression, @NotNull final Comparator<Character> comparator)
            throws UnknownOperatorException {
        this.expression = expression.replace(" ",
                "");
        this.sets = new HashSet<>(expression.length());
        this.postfixExpression = convertToRPN(comparator);
    }

    public String convertToRPN(final Comparator<Character> comparator) {
        StringBuilder result = new StringBuilder(expression.length());
        Stack<Character> stack = new Stack<>();
        for (char symbol: expression.toCharArray()) {
            if (isOperand(symbol)) {
                result.append(symbol);
                sets.add(symbol);
                continue;
            }
            // If the stack is empty or if the symbol is an opening bracket,
            // we should put it on the stack
            if (stack.isEmpty() || symbol == '(') {
                stack.push(symbol);
                continue;
            }
            // If the symbol is the closing bracket, we should empty the stack until the first
            // opening bracket
            if (symbol == ')') {
                while (true) {
                  char top = stack.pop();
                  if (top == '(') {
                      break;
                  }
                  result.append(top);
                }
                continue;
            }
            /*if (!OperatorComparator.AVAILABLE_CHARACTERS.contains(symbol)) {
                throw new UnknownOperatorException(symbol);
            }*/
            while (!stack.isEmpty()) {
                char top = stack.peek();
                if (top == '(' || comparator.compare(symbol, top) > 0) {
                    break;
                }
                // The precedence is the same or lower, empty the stack until the next
                // symbol with lower precedence
                stack.pop();
                result.append(top);
            }
            // The precedence is higher, put the symbol on the stack
            stack.push(symbol);
        }
        while (!stack.isEmpty()) {
            result.append(stack.pop());
        }
        return result.toString();
    }

    public T evaluate(@NotNull Map<Character, Function<T>> evaluator,
                                   @NotNull Map<Character, T> labeledSets) {
        Stack<T> stack = new Stack<>();
        for (final char symbol: postfixExpression.toCharArray()) {
            if (isOperand(symbol)) {
                // Retrieve the value of the set and put it on the stack
                stack.push(labeledSets.get(symbol));
            } else {
                Function<T> function = evaluator.get(symbol);
                // Pop the first set and check if is it an unary operation
                T first = stack.pop();
                if (function instanceof Function1) {
                    Function1<T, T> unaryFunction = (Function1<T, T>) function;
                    stack.push(unaryFunction.invoke(first));
                    continue;
                }
                // The symbol represents a binary operation
                // Pop two elements from the stack and perform the operation
                T second = stack.pop();
                Function2<T, T, T> binaryFunction = (Function2<T, T, T>) function;
                // Notice that the second element appears earlier in the expression.
                // The order is important because we do not know whether the operation is
                // commutative
                second = binaryFunction.invoke(second, first);
                stack.push(second);
            }
        }
        return stack.peek();
    }



    public Set<Character> getSetNames() { return sets; }

    public int getNumberOfVariables() {
        return sets.size();
    }

    public String getExpression() {
        return expression;
    }
}
