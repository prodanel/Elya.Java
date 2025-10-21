package Pz9;

public class CafeDemo {
    public static void main(String[] args) {
        System.out.println("=== ЗАПУСК СИСТЕМЫ ЗАКАЗОВ КАФЕ ===\n");

        CafeOrderSystem cafeSystem = new CafeOrderSystem();

        MenuItem pasta = new MainCourse("Паста Карбонара", 450, "Спагетти с беконом и соусом");
        MenuItem steak = new MainCourse("Стейк Рибай", 1200, "Мраморная говядина с овощами");
        MenuItem caesar = new Salad("Цезарь", 350, "Салат с курицей и соусом цезарь");
        MenuItem tiramisu = new Dessert("Тирамису", 280, "Итальянский десерт");
        MenuItem cheesecake = new Dessert("Чизкейк", 320, "Нью-Йоркский чизкейк");
        MenuItem coffee = new Drink("Капучино", 180, "Кофе с молочной пенкой");
        MenuItem juice = new Drink("Апельсиновый сок", 150, "Свежевыжатый сок");

        cafeSystem.addMenuItemToMenu("Основное меню", pasta);
        cafeSystem.addMenuItemToMenu("Основное меню", steak);
        cafeSystem.addMenuItemToMenu("Основное меню", caesar);
        cafeSystem.addMenuItemToMenu("Десерты", tiramisu);
        cafeSystem.addMenuItemToMenu("Десерты", cheesecake);
        cafeSystem.addMenuItemToMenu("Напитки", coffee);
        cafeSystem.addMenuItemToMenu("Напитки", juice);

        MenuItem veganSalad = new Salad("Зеленый салат", 250, "Свежие овощи с оливковым маслом");
        MenuItem fruitSalad = new Dessert("Фруктовый салат", 200, "Свежие сезонные фрукты");

        cafeSystem.addMenuItemToMenu("Веганское меню", veganSalad);
        cafeSystem.addMenuItemToMenu("Веганское меню", fruitSalad);

        System.out.println("=== ДОСТУПНЫЕ МЕНЮ ===");
        cafeSystem.displayAllMenus();

        System.out.println("\n=== ПРОЦЕСС ЗАКАЗА ===");

        System.out.println("\n--- Оформление заказа 1 ---");
        Order order1 = cafeSystem.createNewOrder();
        order1.addItem(pasta, 1);
        order1.addItem(tiramisu, 1);
        order1.addItem(coffee, 2);
        order1.displayOrder();

        System.out.println("\n--- Оформление заказа 2 ---");
        Order order2 = cafeSystem.createNewOrder();
        order2.addItem(veganSalad, 2);
        order2.addItem(fruitSalad, 1);
        order2.addItem(juice, 1);
        order2.displayOrder();

        cafeSystem.displayAllOrders();

        demonstrateExtensibility(cafeSystem);
    }

    private static void demonstrateExtensibility(CafeOrderSystem cafeSystem) {
        System.out.println("\n=== ДЕМОНСТРАЦИЯ РАСШИРЯЕМОСТИ ===");

        MenuType kidsMenu = new AbstractMenuType("Детское меню") {
            @Override
            public boolean canAddItem(MenuItem item) {
                return (item.getType().equals("DESSERT") || item.getType().equals("DRINK"))
                        && item.getPrice() < 300;
            }
        };

        cafeSystem.registerNewMenuType("KIDS", kidsMenu);

        MenuItem kidsDessert = new Dessert("Мороженое", 150, "Ванильное мороженое");
        MenuItem kidsJuice = new Drink("Яблочный сок", 120, "Натуральный сок");
        MenuItem kidsCake = new Dessert("Кекс", 350, "Шоколадный кекс");
        cafeSystem.addMenuItemToMenu("Детское меню", kidsDessert);
        cafeSystem.addMenuItemToMenu("Детское меню", kidsJuice);
        cafeSystem.addMenuItemToMenu("Детское меню", kidsCake);

        System.out.println("\n=== ОБНОВЛЕННЫЕ МЕНЮ ===");
        cafeSystem.displayAllMenus();

        System.out.println("\n--- Детский заказ ---");
        Order kidsOrder = cafeSystem.createNewOrder();
        kidsOrder.addItem(kidsDessert, 2);
        kidsOrder.addItem(kidsJuice, 1);
        kidsOrder.displayOrder();

        System.out.println("\n=== ВСЕ ЗАКАЗЫ СИСТЕМЫ ===");
        cafeSystem.displayAllOrders();
    }
}
