package com.example.set_theory.RPN;

import com.example.set_theory.exceptions.UniversalSetMissingException;
import com.example.set_theory.exceptions.UnknownOperatorException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

import static com.example.set_theory.RPN.OperatorComparator.*;

import org.jetbrains.annotations.NotNull;

public class SetRPN {

    private final String expression;
    private Set<Character> universalSet = null;

    private final String postfixExpression;

    private final List<Character> sets;

    private static boolean isOperand(char symbol) {
        return Character.isUpperCase(symbol);
    }

    public SetRPN(@NotNull final String expression) throws UnknownOperatorException {
        this.expression = expression.replace(" ",
                "");
        this.sets = new ArrayList<>(2 * expression.length() / 3);
        this.postfixExpression = convertToRPN();
    }

    public SetRPN(@NotNull final String expression, @NotNull final Set<Character> universalSet)
            throws UnknownOperatorException {
        this(expression);
        this.universalSet = universalSet;
    }

    public String convertToRPN() throws UnknownOperatorException {
        OperatorComparator comparator = new OperatorComparator();
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
            if (!OperatorComparator.AVAILABLE_CHARACTERS.contains(symbol)) {
                throw new UnknownOperatorException(symbol);
            }
            while (true) {
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

    public Set<Character> evaluate(@NotNull Map<Character, Set<Character>> labeledSets) throws
            UnknownOperatorException, UniversalSetMissingException {
        Stack<Set<Character>> stack = new Stack<>();
        for (final char symbol: postfixExpression.toCharArray()) {
            if (isOperand(symbol)) {
                // Retrieve the value of the set and put it on the stack
                stack.push(labeledSets.get(symbol));
            } else {
                // Pop the first set and check if is it an unary operation
                Set<Character> first = stack.pop();
                if (symbol == COMPLEMENT) {
                    if (universalSet == null) {
                        throw new UniversalSetMissingException();
                    }
                    first = getComplement(first);
                    stack.push(first);
                    continue;
                }
                // The symbol represents a binary operation
                // Pop two sets from the stack and perform the operation
                Set<Character> second = stack.pop();
                // After this switch, the second set contains the result of the operation
                switch (symbol) {
                    case UNION:
                        second.addAll(first);
                        break;
                    case INTERSECTION:
                        second.retainAll(first);
                        break;
                    case DIFFERENCE:
                        second.removeAll(first);
                        break;
                    case SYMMETRIC_DIFFERENCE:
                        // Calculating the intersection
                        Set<Character> copy = new HashSet<>(first);
                        copy.retainAll(second);
                        // Calculating the difference
                        second.addAll(first);
                        second.removeAll(copy);
                        break;
                    default:
                        throw new UnknownOperatorException(symbol);
                }
                stack.push(second);
            }
        }
        return stack.peek();
    }

    private Set<Character> getComplement(@NotNull Set<Character> set) {
        return universalSet.stream().filter(element -> !set.contains(element)).
                collect(Collectors.toSet());
    }

    public List<Character> getSetNames() { return sets; }

    public int getNumberOfVariables() {
        return sets.size();
    }

    public String getExpression() {
        return expression;
    }

    public Set<Character> getUniversalSet() {
        return universalSet;
    }

}
