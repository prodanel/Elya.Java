public class Main1 {
    public double calculate(int a, int b, char op) {
        switch(op) {
            case '+': return a + b;
            case '-': return a - b;
            case '*': return a * b;
            case '/':
                if(b != 0) return (double)a / b;
                else throw new ArithmeticException("Деление на ноль!");
            default: throw new IllegalArgumentException("Недопустимый оператор");
        }
    }
    public double calculate(double a, double b) {
        return a + b;
    }
    public int calculate(int... values) {
        int sum = 0;
        for(int value : values) {
            sum += value;
        }
        return sum;
    }
}


