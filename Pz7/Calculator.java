package Pz7;

import java.util.*;
import java.util.stream.Collectors;

    class Calculator<T extends Number> {
        private List<T> numbers;

        public Calculator() {
            this.numbers = new ArrayList<>();
        }

        public Calculator(List<T> numbers) {
            this.numbers = new ArrayList<>(numbers);
        }

        public void addNumber(T number) {
            numbers.add(number);
        }

        public void addAllNumbers(List<T> numbersToAdd) {
            numbers.addAll(numbersToAdd);
        }

        public double sum() {
            return numbers.stream()
                    .mapToDouble(Number::doubleValue)
                    .sum();
        }

        public double subtract() {
            if (numbers.isEmpty()) {
                return 0.0;
            }

            double result = numbers.get(0).doubleValue();
            for (int i = 1; i < numbers.size(); i++) {
                result -= numbers.get(i).doubleValue();
            }
            return result;
        }

        public double subtract(T a, T b) {
            return a.doubleValue() - b.doubleValue();
        }

        public double average() {
            if (numbers.isEmpty()) {
                return 0.0;
            }

            return numbers.stream()
                    .mapToDouble(Number::doubleValue)
                    .average()
                    .orElse(0.0);
        }

        public double min() {
            return numbers.stream()
                    .mapToDouble(Number::doubleValue)
                    .min()
                    .orElse(0.0);
        }

        public double max() {
            return numbers.stream()
                    .mapToDouble(Number::doubleValue)
                    .max()
                    .orElse(0.0);
        }

        public int count() {
            return numbers.size();
        }

        public void clear() {
            numbers.clear();
        }

        public List<T> getNumbers() {
            return new ArrayList<>(numbers);
        }

        public void printInfo() {
            System.out.println("Числа в калькуляторе: " + numbers);
            System.out.println("Количество чисел: " + count());
            if (!numbers.isEmpty()) {
                System.out.println("Сумма: " + sum());
                System.out.println("Среднее значение: " + average());
                System.out.println("Минимальное: " + min());
                System.out.println("Максимальное: " + max());
            }
            System.out.println("---");
        }

        public static <U extends Number> void demonstrateCalculator(Calculator<U> calc, String typeName) {
            System.out.println("=== КАЛЬКУЛЯТОР ДЛЯ " + typeName.toUpperCase() + " ===");
            calc.printInfo();
        }
    }

    class ScientificCalculator<T extends Number> extends Calculator<T> {

        public double multiply() {
            return getNumbers().stream()
                    .mapToDouble(Number::doubleValue)
                    .reduce(1, (a, b) -> a * b);
        }

        public double divide() {
            List<T> numbers = getNumbers();
            if (numbers.isEmpty()) {
                return 0.0;
            }

            double result = numbers.get(0).doubleValue();
            for (int i = 1; i < numbers.size(); i++) {
                double divisor = numbers.get(i).doubleValue();
                if (divisor == 0) {
                    throw new ArithmeticException("Деление на ноль!");
                }
                result /= divisor;
            }
            return result;
        }

        public double power(T base, T exponent) {
            return Math.pow(base.doubleValue(), exponent.doubleValue());
        }
    }

