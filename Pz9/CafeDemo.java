package Pz9;

import java.util.*;

public class CafeDemo {
    public static void main(String[] args) {
        demonstrateMenuSystem();
    }

    private static void demonstrateMenuSystem() {
        System.out.println("=== ДЕМОНСТРАЦИЯ СИСТЕМЫ КАФЕ ===\n");

        MenuSystem menuSystem = new MenuSystem();

        MenuType jarrowsMenu = new JarrowsMenuType();
        menuSystem.registerNewMenuType("Меню Jarrows", jarrowsMenu);

        MenuItem item1 = new MenuItem("Шоколадный торт", 150, "Нежный шоколадный торт с вишней", "ДЕСЕРТ");
        MenuItem item2 = new MenuItem("Фруктовый микс", 120, "Свежие сезонные фрукты", "МИКС");
        MenuItem item3 = new MenuItem("Тирамису", 180, "Классический итальянский десерт", "ДЕСЕРТ");
        MenuItem item4 = new MenuItem("Дорогой трюфель", 600, "Элитный шоколадный трюфель", "ДЕСЕРТ");
        MenuItem item5 = new MenuItem("Стейк", 350, "Говяжий стейк с овощами", "ОСНОВНОЕ");

        System.out.println("Добавление элементов в меню:");
        menuSystem.addMenuItemToMenu("Меню Jarrows", item1);
        menuSystem.addMenuItemToMenu("Меню Jarrows", item2);
        menuSystem.addMenuItemToMenu("Меню Jarrows", item3);
        menuSystem.addMenuItemToMenu("Меню Jarrows", item4);
        menuSystem.addMenuItemToMenu("Меню Jarrows", item5);

        System.out.println("\n=== ИТОГОВОЕ МЕНЮ ===");
        menuSystem.displayMenu("Меню Jarrows");

        System.out.println("\n=== ДЕТСКОЕ МЕНЮ ===");
        MenuType kidsMenu = new KidsMenuType();
        menuSystem.registerNewMenuType("Детское меню", kidsMenu);

        MenuItem kidsItem1 = new MenuItem("Шоколадное молоко", 80, "Сладкий напиток для детей", "НАПИТОК");
        MenuItem kidsItem2 = new MenuItem("Детский бургер", 120, "Мини-бургер с картошкой", "ОСНОВНОЕ");
        MenuItem kidsItem3 = new MenuItem("Фруктовое мороженое", 60, "Натуральное фруктовое мороженое", "ДЕСЕРТ");

        menuSystem.addMenuItemToMenu("Детское меню", kidsItem1);
        menuSystem.addMenuItemToMenu("Детское меню", kidsItem2);
        menuSystem.addMenuItemToMenu("Детское меню", kidsItem3);
        menuSystem.displayMenu("Детское меню");

        System.out.println("\n=== ОСНОВНОЕ МЕНЮ ===");
        MenuType mainMenu = new MainMenuType();
        menuSystem.registerNewMenuType("Основное меню", mainMenu);

        MenuItem mainItem1 = new MenuItem("Куриный суп", 90, "Ароматный куриный суп", "СУП");
        MenuItem mainItem2 = new MenuItem("Салат Цезарь", 130, "Классический салат с курицей", "САЛАТ");
        MenuItem mainItem3 = new MenuItem("Лосось на гриле", 280, "Лосось с лимоном и травами", "ОСНОВНОЕ");
        MenuItem mainItem4 = new MenuItem("Веганская паста", 160, "Паста с овощами и соусом песто", "ОСНОВНОЕ");

        menuSystem.addMenuItemToMenu("Основное меню", mainItem1);
        menuSystem.addMenuItemToMenu("Основное меню", mainItem2);
        menuSystem.addMenuItemToMenu("Основное меню", mainItem3);
        menuSystem.addMenuItemToMenu("Основное меню", mainItem4);
        menuSystem.displayMenu("Основное меню");
    }
}

class MenuItem {
    private String name;
    private double price;
    private String description;
    private String type;

    public MenuItem(String name, double price, String description, String type) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.type = type;
    }

    public String getName() { return name; }public double getPrice() { return price; }
    public String getDescription() { return description; }
    public String getType() { return type; }

    @Override
    public String toString() {
        return name + " (" + price + " руб.)";
    }
}

abstract class MenuType {
    private String menuTypeName;

    public MenuType(String menuTypeName) {
        this.menuTypeName = menuTypeName;
    }

    public abstract boolean canAddItem(MenuItem item);
    public String getMenuTypeName() { return menuTypeName; }
}

class JarrowsMenuType extends MenuType {
    public JarrowsMenuType() {
        super("Меню Jarrows");
    }

    @Override
    public boolean canAddItem(MenuItem item) {
        boolean allowed = ("ДЕСЕРТ".equals(item.getType()) || "МИКС".equals(item.getType()))
                && item.getPrice() < 500;
        if (!allowed) {
            System.out.println("   ⚠️ Элемент '" + item.getName() + "' не соответствует правилам Меню Jarrows");
            System.out.println("     (только ДЕСЕРТ/МИКС и цена < 500 руб)");
        }
        return allowed;
    }
}

class KidsMenuType extends MenuType {
    public KidsMenuType() {
        super("Детское меню");
    }

    @Override
    public boolean canAddItem(MenuItem item) {
        boolean allowed = item.getPrice() < 100;
        if (!allowed) {
            System.out.println("   ⚠️ Элемент '" + item.getName() + "' слишком дорогой для Детского меню");
            System.out.println("     (максимальная цена 100 руб)");
        }
        return allowed;
    }
}

class MainMenuType extends MenuType {
    public MainMenuType() {
        super("Основное меню");
    }

    @Override
    public boolean canAddItem(MenuItem item) {
        boolean allowed = !"ДЕСЕРТ".equals(item.getType()) && item.getPrice() < 300;
        if (!allowed) {
            System.out.println("   ⚠️ Элемент '" + item.getName() + "' не подходит для Основного меню");
            System.out.println("     (без десертов и цена < 300 руб)");
        }
        return allowed;
    }
}

class MenuSystem {
    private Map<String, MenuType> menuTypes = new HashMap<>();
    private Map<String, List<MenuItem>> menus = new HashMap<>();

    public void registerNewMenuType(String name, MenuType menuType) {
        menuTypes.put(name, menuType);
        menus.put(name, new ArrayList<>());
        System.out.println("✅ Зарегистрировано меню: " + name);
    }

    public void addMenuItemToMenu(String menuName, MenuItem item) {
        if (!menuTypes.containsKey(menuName)) {
            System.out.println("❌ Меню '" + menuName + "' не найдено");
            return;
        }

        MenuType menuType = menuTypes.get(menuName);
        if (menuType.canAddItem(item)) {
            menus.get(menuName).add(item);
            System.out.println("✅ Добавлено: " + item.getName() + " в меню: " + menuName);
        }
    }

    public void displayMenu(String menuName) {
        System.out.println("\n📋 " + menuName);
        List<MenuItem> items = menus.get(menuName);
        if (items != null && !items.isEmpty()) {
            for (int i = 0; i < items.size(); i++) {
                MenuItem item = items.get(i);
                System.out.println("   " + (i + 1) + ". 🍽️ " + item.getName() +
                        " (" + item.getType() + "): " + item.getPrice() + " руб." +
                        " - " + item.getDescription());
            }
            double total = items.stream().mapToDouble(MenuItem::getPrice).sum();
            System.out.println("   💰 Общая стоимость: " + total + " руб.");
        } else {
            System.out.println("   🚫 Пустое меню");
        }
    }

    public List<MenuItem> getMenuItems(String menuName) {
        return menus.getOrDefault(menuName, new ArrayList<>());
    }
}