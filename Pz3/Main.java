package Pz3;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== МАГАЗИН ТОВАРОВ ===\n");

        Product milk = new Product("Молоко Простоквашино", 95.0, "MLK001");
        milk.setGrade("Высший");
        milk.setShelfLife(10);
        milk.setReleaseDate("20.01.2024");
        milk.setExpiryDate("30.01.2024");

        Product bread = new Product("Хлеб Бородинский", 45.0, 5, "Первый",
                "22.01.2024", "27.01.2024", "BRD001");

        // Способ 3: Создание простого товара
        Product water = new Product("Вода минеральная", 35.0, "WTR001");

        System.out.println("1. Первый товар:");
        milk.printInfo();
        milk.applyDiscount(15);

        System.out.println("\n2. Второй товар:");
        bread.printInfo();
        bread.checkExpiry("25.01.2024");

        System.out.println("\n3. Третий товар:");
        water.printInfo();

        System.out.println("\n=== ИНФОРМАЦИЯ ЧЕРЕЗ ГЕТТЕРЫ ===");
        System.out.println("Название молока: " + milk.getName());
        System.out.println("Цена хлеба: " + bread.getPrice() + " руб.");
        System.out.println("Артикул воды: " + water.getArticle());
    }
}

