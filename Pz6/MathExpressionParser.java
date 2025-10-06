package Pz6;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MathExpressionParser {

    private static final Pattern TOKEN_PATTERN = Pattern.compile(
            "\\d+\\.?\\d*|\\d*\\.\\d+" +
                    "|e\\^[+-]?\\d+" +
                    "|[a-zA-Z_][a-zA-Z0-9_]*" +
                    "|[+\\-*/^()]"
    );

    private static final Pattern NUMBER_PATTERN = Pattern.compile(
            "^\\d+\\.?\\d*|\\d*\\.\\d+$"
    );

    private static final Pattern FUNCTION_PATTERN = Pattern.compile(
            "^(sin|cos|tan|log|sqrt)$"
    );

    private static final Pattern VARIABLE_PATTERN = Pattern.compile(
            "^[a-zA-Z_][a-zA-Z0-9_]*$"
    );

    public static List<Token> tokenize(String expression) {
        List<Token> tokens = new ArrayList<>();
        Matcher matcher = TOKEN_PATTERN.matcher(expression.replace(" ", ""));

        while (matcher.find()) {
            String tokenStr = matcher.group();
            tokens.add(createToken(tokenStr, tokens));
        }

        return tokens;
    }

    private static Token createToken(String tokenStr, List<Token> previousTokens) {
        if (NUMBER_PATTERN.matcher(tokenStr).matches()) {
            return new Token(Token.Type.NUMBER, tokenStr);
        }

        if (FUNCTION_PATTERN.matcher(tokenStr).matches()) {
            return new Token(Token.Type.FUNCTION, tokenStr, 4);
        }

        if (VARIABLE_PATTERN.matcher(tokenStr).matches()) {
            return new Token(Token.Type.VARIABLE, tokenStr);
        }

        switch (tokenStr) {
            case "(":
                return new Token(Token.Type.LEFT_PAREN, tokenStr);
            case ")":
                return new Token(Token.Type.RIGHT_PAREN, tokenStr);
            case "-":

                return isUnaryMinus(previousTokens) ?
                        new Token(Token.Type.OPERATOR, "unary-", 4) :
                        new Token(Token.Type.OPERATOR, tokenStr, 1);
            case "+": case "*": case "/": case "^":
                return new Token(Token.Type.OPERATOR, tokenStr);
        }

        return new Token(Token.Type.VARIABLE, tokenStr);
    }

    private static boolean isUnaryMinus(List<Token> previousTokens) {
        if (previousTokens.isEmpty()) {
            return true;
        }

        Token lastToken = previousTokens.get(previousTokens.size() - 1);
        return lastToken.getType() == Token.Type.OPERATOR ||
                lastToken.getType() == Token.Type.LEFT_PAREN;
    }

    public static List<Token> shuntingYard(List<Token> tokens) {
        List<Token> output = new ArrayList<>();
        Stack<Token> operatorStack = new Stack<>();

        for (Token token : tokens) {
            switch (token.getType()) {
                case NUMBER:
                case VARIABLE:
                    output.add(token);
                    break;

                case FUNCTION:
                    operatorStack.push(token);
                    break;

                case OPERATOR:
                    while (!operatorStack.isEmpty() &&
                            operatorStack.peek().getType() != Token.Type.LEFT_PAREN &&
                            (operatorStack.peek().getPrecedence() > token.getPrecedence() ||
                                    (operatorStack.peek().getPrecedence() == token.getPrecedence() &&
                                            !token.getValue().equals("^")))) {
                        output.add(operatorStack.pop());
                    }
                    operatorStack.push(token);
                    break;

                case LEFT_PAREN:
                    operatorStack.push(token);
                    break;

                case RIGHT_PAREN:
                    while (!operatorStack.isEmpty() &&
                            operatorStack.peek().getType() != Token.Type.LEFT_PAREN) {
                        output.add(operatorStack.pop());
                    }
                    if (!operatorStack.isEmpty() &&
                            operatorStack.peek().getType() == Token.Type.LEFT_PAREN) {
                        operatorStack.pop(); // Убираем левую скобку
                    }
                    if (!operatorStack.isEmpty() &&
                            operatorStack.peek().getType() == Token.Type.FUNCTION) {
                        output.add(operatorStack.pop());
                    }
                    break;
            }
        }

        while (!operatorStack.isEmpty()) {
            output.add(operatorStack.pop());
        }

        return output;
    }

    public static double evaluateRPN(List<Token> rpnTokens, Map<String, Double> variables) {
        Stack<Double> stack = new Stack<>();

        for (Token token : rpnTokens) {
            switch (token.getType()) {
                case NUMBER:
                    stack.push(Double.parseDouble(token.getValue()));
                    break;

                case VARIABLE:
                    if (variables.containsKey(token.getValue())) {
                        stack.push(variables.get(token.getValue()));
                    } else {
                        throw new RuntimeException("Неизвестная переменная: " + token.getValue());
                    }
                    break;

                case OPERATOR:
                    if (stack.size() < 1) throw new RuntimeException("Недостаточно операндов");

                    if (token.getValue().equals("unary-")) {
                        double operand = stack.pop();
                        stack.push(-operand);
                    } else {
                        if (stack.size() < 2) throw new RuntimeException("Недостаточно операндов");double right = stack.pop();
                        double left = stack.pop();
                        stack.push(applyOperator(token.getValue(), left, right));
                    }
                    break;

                case FUNCTION:
                    if (stack.isEmpty()) throw new RuntimeException("Недостаточно операндов");
                    double operand = stack.pop();
                    stack.push(applyFunction(token.getValue(), operand));
                    break;
            }
        }

        if (stack.size() != 1) {
            throw new RuntimeException("Некорректное выражение");
        }

        return stack.pop();
    }

    private static double applyOperator(String operator, double left, double right) {
        switch (operator) {
            case "+": return left + right;
            case "-": return left - right;
            case "*": return left * right;
            case "/":
                if (right == 0) throw new RuntimeException("Деление на ноль");
                return left / right;
            case "^": return Math.pow(left, right);
            default: throw new RuntimeException("Неизвестный оператор: " + operator);
        }
    }

    private static double applyFunction(String function, double operand) {
        switch (function) {
            case "sin": return Math.sin(operand);
            case "cos": return Math.cos(operand);
            case "tan": return Math.tan(operand);
            case "log": return Math.log(operand);
            case "sqrt": return Math.sqrt(operand);
            default: throw new RuntimeException("Неизвестная функция: " + function);
        }
    }

    public static void demonstrateListOperations(List<Token> tokens) {
        System.out.println("\n=== ОПЕРАЦИИ СО СПИСКАМИ ===");

        List<Token> operators = tokens.stream()
                .filter(t -> t.getType() == Token.Type.OPERATOR)
                .collect(Collectors.toList());
        System.out.println("Только операторы: " + operators);

        List<String> tokenTypes = tokens.stream()
                .map(t -> t.getType().name())
                .collect(Collectors.toList());
        System.out.println("Типы токенов: " + tokenTypes);

        Map<Token.Type, List<Token>> grouped = tokens.stream()
                .collect(Collectors.groupingBy(Token::getType));
        System.out.println("Группировка по типам: " + grouped.keySet());

        Map<Token.Type, Long> tokenCounts = tokens.stream()
                .collect(Collectors.groupingBy(Token::getType, Collectors.counting()));
        System.out.println("Количество токенов по типам: " + tokenCounts);

        List<Token> sortedByLength = tokens.stream()
                .sorted((t1, t2) -> Integer.compare(t1.getValue().length(), t2.getValue().length()))
                .collect(Collectors.toList());
        System.out.println("Отсортировано по длине значения: " + sortedByLength);

        List<String> uniqueValues = tokens.stream()
                .map(Token::getValue)
                .distinct()
                .collect(Collectors.toList());
        System.out.println("Уникальные значения токенов: " + uniqueValues);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== ПАРСЕР МАТЕМАТИЧЕСКИХ ВЫРАЖЕНИЙ ===");System.out.println("Поддерживаемые функции: sin, cos, tan, log, sqrt");
        System.out.print("Введите выражение: ");
        String expression = scanner.nextLine();

        try {

            System.out.println("\n=== ТОКЕНИЗАЦИЯ ===");
            List<Token> tokens = tokenize(expression);
            System.out.println("Токены: " + tokens);

            demonstrateListOperations(tokens);

            System.out.println("\n=== ПРЕОБРАЗОВАНИЕ В ОПЗ ===");
            List<Token> rpn = shuntingYard(tokens);
            String rpnString = rpn.stream()
                    .map(Token::toString)
                    .collect(Collectors.joining(" "));
            System.out.println("ОПЗ (RPN): " + rpnString);

            Map<String, Double> variables = new HashMap<>();
            List<String> variableNames = tokens.stream()
                    .filter(t -> t.getType() == Token.Type.VARIABLE)
                    .map(Token::getValue)
                    .distinct()
                    .filter(v -> !FUNCTION_PATTERN.matcher(v).matches())
                    .sorted()
                    .collect(Collectors.toList());

            if (!variableNames.isEmpty()) {
                System.out.println("\n=== ЗНАЧЕНИЯ ПЕРЕМЕННЫХ ===");
                for (String varName : variableNames) {
                    System.out.print(varName + " = ");
                    variables.put(varName, scanner.nextDouble());
                }
            }

            System.out.println("\n=== ВЫЧИСЛЕНИЕ ===");
            double result = evaluateRPN(rpn, variables);
            System.out.println("Результат: " + result);

        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
            e.printStackTrace();
        }

        demonstrateWithExamples();

        scanner.close();
    }

    public static void demonstrateWithExamples() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("ДЕМОНСТРАЦИЯ РАБОТЫ НА ПРИМЕРАХ");
        System.out.println("=".repeat(60));

        String[] expressions = {
                "-3 + 4 * (x - 2)",
                "2 + 3 * 4",
                "sin(0) + cos(0)",
                "x * y + z / 2",
                "3.14 * r ^ 2",
                "5 + -2 * 3"
        };

        Map<String, Double> testVariables = new HashMap<>();
        testVariables.put("x", 5.0);
        testVariables.put("y", 3.0);
        testVariables.put("z", 10.0);
        testVariables.put("r", 2.0);

        for (String expression : expressions) {
            System.out.println("\n" + "-".repeat(50));
            System.out.println("Выражение: " + expression);

            try {
                List<Token> tokens = tokenize(expression);
                System.out.println("Токены: " + tokens);

                List<Token> rpn = shuntingYard(tokens);
                String rpnString = rpn.stream()
                        .map(Token::toString)
                        .collect(Collectors.joining(" "));
                System.out.println("ОПЗ: " + rpnString);

                double result = evaluateRPN(rpn, testVariables);
                System.out.println("Результат: " + result);

            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }
}


