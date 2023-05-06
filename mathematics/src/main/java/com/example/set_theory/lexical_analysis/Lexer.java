package com.example.set_theory.lexical_analysis;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {

    public static Pattern expression = Pattern.compile("((!)?[a-z])([∧∨→⊕](!)?[a-z])*");
    public static Pattern subexpression = Pattern.compile("\\(((!)?[a-z])([∧∨→⊕](!)?[a-z])*\\)");

    public static boolean isCorrectLogicalExpression(String input) {
        Matcher matcher;
        int previousLength;
        do {
            previousLength = input.length();
            input = subexpression.matcher(input).replaceAll("a");
        } while (input.length() != previousLength);
        matcher = expression.matcher(input);
        return matcher.matches();
    }

}
