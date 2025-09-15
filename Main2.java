public class Main2 {
    public static void main(String[] args) {
        Main1 calculator = new Main1();

        System.out.println("Тест 1 (10 + 5): " + calculator.calculate(10, 5, '+'));

        System.out.println("Тест 2 (3.5 + 2.5): " + calculator.calculate(3.5, 2.5));

        System.out.println("Тест 3 (сумма 1+2+3+4+5): " + calculator.calculate(1, 2, 3, 4, 5));
    }
}


