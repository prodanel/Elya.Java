package Pz9;

import java.util.HashMap;
import java.util.Map;

public class CafeOrder implements Order {
    private Map<MenuItem, Integer> items;
    private int orderNumber;
    private static int orderCounter = 1;

    public CafeOrder() {
        this.items = new HashMap<>();
        this.orderNumber = orderCounter++;
    }

    @Override
    public void addItem(MenuItem item, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Количество должно быть положительным");
        }
        items.put(item, items.getOrDefault(item, 0) + quantity);
    }

    @Override
    public void removeItem(MenuItem item) {
        items.remove(item);
    }

    @Override
    public double calculateTotal() {
        return items.entrySet().stream()
                .mapToDouble(entry -> entry.getKey().getPrice() * entry.getValue())
                .sum();
    }

    @Override
    public void displayOrder() {
        System.out.println("\n=== Заказ №" + orderNumber + " ===");
        if (items.isEmpty()) {
            System.out.println("Заказ пуст");
            return;
        }

        items.forEach((item, quantity) -> {
            double itemTotal = item.getPrice() * quantity;
            System.out.printf("%s x%d - %.2f руб.\n",
                    item.getName(), quantity, itemTotal);
        });
        System.out.printf("Итого: %.2f руб.\n", calculateTotal());
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public Map<MenuItem, Integer> getItems() {
        return new HashMap<>(items);
    }
}