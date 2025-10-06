package Pz7;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UniversalCalculator {
    public static void main(String[] args) {
        System.out.println("=== УНИВЕРСАЛЬНЫЙ КАЛЬКУЛЯТОР ===\n");

        System.out.println("1. ТЕСТИРОВАНИЕ С INTEGER");
        Calculator<Integer> intCalculator = new Calculator<>();

        intCalculator.addNumber(10);
        intCalculator.addNumber(20);
        intCalculator.addNumber(30);
        intCalculator.addNumber(40);

        intCalculator.printInfo();
        System.out.println("Результат вычитания: " + intCalculator.subtract());
        System.out.println("Вычитание 50 - 25: " + intCalculator.subtract(50, 25));

        System.out.println("\n2. ТЕСТИРОВАНИЕ С DOUBLE");
        List<Double> doubleList = Arrays.asList(1.5, 2.7, 3.8, 4.2, 5.1);
        Calculator<Double> doubleCalculator = new Calculator<>(doubleList);

        doubleCalculator.printInfo();
        System.out.println("Результат вычитания: " + doubleCalculator.subtract());
        System.out.println("Вычитание 10.5 - 3.2: " + doubleCalculator.subtract(10.5, 3.2));

        System.out.println("\n3. ОПЕРАЦИИ СО СПИСКАМИ");

        List<Integer> allIntNumbers = intCalculator.getNumbers();
        List<Integer> evenNumbers = allIntNumbers.stream()
                .filter(n -> n % 2 == 0)
                .collect(Collectors.toList());
        System.out.println("Все числа: " + allIntNumbers);
        System.out.println("Только четные: " + evenNumbers);

        List<Double> squares = doubleCalculator.getNumbers().stream()
                .map(n -> n * n)
                .collect(Collectors.toList());
        System.out.println("Исходные числа: " + doubleCalculator.getNumbers());
        System.out.println("Квадраты чисел: " + squares);

        List<Double> sortedNumbers = doubleCalculator.getNumbers().stream()
                .sorted()
                .collect(Collectors.toList());
        System.out.println("Отсортированные числа: " + sortedNumbers);

        Map<String, List<Double>> groupedByRange = doubleCalculator.getNumbers().stream()
                .collect(Collectors.groupingBy(n -> {
                    if (n < 2.0) return "Малые (<2.0)";
                    else if (n < 4.0) return "Средние (2.0-4.0)";
                    else return "Большие (>=4.0)";
                }));
        System.out.println("Группировка по диапазонам: " + groupedByRange);

        System.out.println("\n4. НАУЧНЫЙ КАЛЬКУЛЯТОР");
        ScientificCalculator<Double> sciCalculator = new ScientificCalculator<>();
        sciCalculator.addAllNumbers(Arrays.asList(2.0, 3.0, 4.0));

        System.out.println("Числа: " + sciCalculator.getNumbers());
        System.out.println("Умножение: " + sciCalculator.multiply());
        System.out.println("Деление: " + sciCalculator.divide());
        System.out.println("2 в степени 3: " + sciCalculator.power(2.0, 3.0));

        System.out.println("\n5. СЛОЖНЫЕ ВЫЧИСЛЕНИЯ");
        Calculator<Integer> largeIntCalc = new Calculator<>(
                Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        );

        Calculator<Double> largeDoubleCalc = new Calculator<>(
                Arrays.asList(1.1, 2.2, 3.3, 4.4, 5.5, 6.6, 7.7, 8.8, 9.9, 10.0)
        );

        System.out.println("Integer калькулятор (1-10):");
        largeIntCalc.printInfo();

        System.out.println("Double калькулятор (1.1-10.0):");
        largeDoubleCalc.printInfo();

        System.out.println("\n6. СРАВНЕНИЕ РЕЗУЛЬТАТОВ");
        System.out.printf("Среднее Integer: %.2f\n", largeIntCalc.average());
        System.out.printf("Среднее Double:  %.2f\n", largeDoubleCalc.average());
        System.out.printf("Сумма Integer: %,.2f\n", largeIntCalc.sum());
        System.out.printf("Сумма Double:  %,.2f\n", largeDoubleCalc.sum());

        System.out.println("\n7. ОБРАБОТКА ОШИБОК");
        Calculator<Integer> emptyCalculator = new Calculator<>();
        System.out.println("Пустой калькулятор:");
        emptyCalculator.printInfo();
        System.out.println("Сумма пустого калькулятора: " + emptyCalculator.sum());
        System.out.println("Среднее пустого калькулятора: " + emptyCalculator.average());

        System.out.println("\n8. ДЕМОНСТРАЦИЯ С РАЗНЫМИ ТИПАМИ");

        Calculator<Float> floatCalculator = new Calculator<>(
                Arrays.asList(1.1f, 2.2f, 3.3f, 4.4f, 5.5f)
        );
        Calculator.demonstrateCalculator(floatCalculator, "Float");

        Calculator<Long> longCalculator = new Calculator<>(
                Arrays.asList(100L, 200L, 300L, 400L, 500L)
        );
        Calculator.demonstrateCalculator(longCalculator, "Long");

        System.out.println("=== ТЕСТИРОВАНИЕ ЗАВЕРШЕНО ===");
    }
}


