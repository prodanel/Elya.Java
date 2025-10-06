package Pz4;
public class ProductProgram {
    public static void main(String[] args) {
        Product[] products = new Product[5];

        products[0] = new Product("Молоко", 85.50, 10, "Высший", "15.10.2024", 14, "MLK001");
        products[1] = new Product("Хлеб", 45.00, 5, "Первый", "18.10.2024", 7, "BRD002");
        products[2] = new Product("Сыр", 320.00, 30, "Высший", "01.10.2024", 30, "CHS003");
        products[3] = new Product("Йогурт", 65.00, 14, "Второй", "16.10.2024", 21, "YGT004");
        products[4] = new Product("Колбаса", 280.00, 20, "Высший", "10.10.2024", 25, "SAG005");

        System.out.println("=== ВСЕ ТОВАРЫ ===");
        for (int i = 0; i < products.length; i++) {
            products[i].printInfo();
        }

        System.out.println("\n=== ТОВАРЫ ВЫСШЕГО СОРТА ===");
        for (Product product : products) {
            if (product.getGrade().equalsIgnoreCase("Высший")) {
                System.out.println(product.getFullName() + " - " + product.getPrice() + " руб.");
            }
        }

        System.out.println("\n=== ТОВАРЫ ДОРОЖЕ 100 РУБЛЕЙ ===");
        for (Product product : products) {
            if (product.getPrice() > 100) {
                System.out.println(product.getFullName() + " - " + product.getPrice() + " руб.");
            }
        }

        System.out.println("\n=== ТОВАРЫ С КОРОТКИМ СРОКОМ ХРАНЕНИЯ (<15 дней) ===");
        for (Product product : products) {
            if (product.getStoragePeriod() < 15) {
                System.out.println(product.getFullName() + " - срок хранения: " + product.getStoragePeriod() + " дней");
            }
        }

        System.out.println("\n=== ПРОСРОЧЕННЫЕ ТОВАРЫ (на 25.10.2024) ===");
        for (Product product : products) {
            if (product.isExpired("25.10.2024")) {
                System.out.println(product.getFullName() + " - ПРОСРОЧЕН!");
            }
        }
    }
}


