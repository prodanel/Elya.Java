package Pz9;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CafeOrderSystem {
    private List<Order> orders;
    private Map<String, MenuType> availableMenus;

    public CafeOrderSystem() {
        this.orders = new ArrayList<>();
        this.availableMenus = new HashMap<>();
        initializeMenus();
    }

    private void initializeMenus() {
        // Получаем все зарегистрированные типы меню
        for (MenuType menuType : MenuFactory.getAllMenuTypes()) {
            availableMenus.put(menuType.getMenuTypeName(), menuType);
        }
    }

    public void addMenuItemToMenu(String menuName, MenuItem item) {
        MenuType menu = availableMenus.get(menuName);
        if (menu != null) {
            // Проверяем, что меню имеет метод addItem
            if (menu instanceof AbstractMenuType) {
                ((AbstractMenuType) menu).addItem(item);
            } else {
                System.out.println("Меню '" + menuName + "' не поддерживает добавление элементов");
            }
        } else {
            System.out.println("Меню '" + menuName + "' не найдено");
        }
    }

    public Order createNewOrder() {
        Order order = new CafeOrder();
        orders.add(order);
        return order;
    }

    public void displayAllMenus() {
        if (availableMenus.isEmpty()) {
            System.out.println("Нет доступных меню");
            return;
        }

        for (MenuType menu : availableMenus.values()) {
            menu.displayMenu();
        }
    }

    public MenuType getMenu(String menuName) {
        return availableMenus.get(menuName);
    }

    public void displayAllOrders() {
        System.out.println("\n=== Все заказы ===");
        if (orders.isEmpty()) {
            System.out.println("Нет активных заказов");
            return;
        }

        for (Order order : orders) {
            order.displayOrder();
        }
    }
}