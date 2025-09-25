import java.util.ArrayList;
import java.util.List;

class Product {

    String name;
    double price;
    int storagePeriod;
    String grade;
    String releaseDate;
    int shelfLife;
    String article;

    public Product(String name, double price, int storagePeriod, String grade,
                   String releaseDate, int shelfLife, String article) {
        this.name = name;
        this.price = price;
        this.storagePeriod = storagePeriod;
        this.grade = grade;
        this.releaseDate = releaseDate;
        this.shelfLife = shelfLife;
        this.article = article;
    }

    public void displayInfo() {
        System.out.println("Товар: " + name +
                ", Цена: " + price + " руб." +
                ", Сорт: " + grade +
                ", Артикул: " + article +
                ", Выпуск: " + releaseDate +
                ", Годен: " + shelfLife + " дней");
    }
}

public class Pz3 {
    public static void main(String[] args) {

        List<Product> products = new ArrayList<>();

        products.add(new Product("Молоко", 85.50, 30, "Высший", "15.01.2024", 14, "MLK001"));
        products.add(new Product("Хлеб", 45.00, 10, "Первый", "20.01.2024", 5, "BRD002"));
        products.add(new Product("Сыр", 320.00, 90, "Высший", "10.01.2024", 30, "CHS003"));
        products.add(new Product("Шоколад", 120.00, 365, "Высший", "01.12.2023", 180, "CHC004"));
        products.add(new Product("Йогурт", 65.00, 45, "Первый", "25.01.2024", 21, "YGT005"));
        products.add(new Product("Колбаса", 280.00, 60, "Второй", "05.01.2024", 45, "KLS006"));

        System.out.println("=== ВСЕ ТОВАРЫ ===");
        for (int i = 0; i < products.size(); i++) {
            products.get(i).displayInfo();
        }

        System.out.println("\n=== ВЫБОРКА 1: Товары высшего сорта ===");
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            if (product.grade.equals("Высший")) {
                product.displayInfo();
            }
        }

        System.out.println("\n=== ВЫБОРКА 2: Товары дороже 100 рублей ===");
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            if (product.price > 100) {
                product.displayInfo();
            }
        }

        System.out.println("\n=== ВЫБОРКА 3: Товары со сроком годности > 30 дней ===");
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            if (product.shelfLife > 30) {
                product.displayInfo();
            }
        }

        System.out.println("\n=== ВЫБОРКА 4: Товары высшего сорта и дороже 100 руб ===");
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            if (product.grade.equals("Высший") && product.price > 100) {
                product.displayInfo();
            }
        }
    }
}

