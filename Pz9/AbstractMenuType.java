package Pz9;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMenuType implements MenuType {
    protected String menuTypeName;
    protected List<MenuItem> items;

    public AbstractMenuType(String menuTypeName) {
        this.menuTypeName = menuTypeName;
        this.items = new ArrayList<>();
    }

    @Override
    public String getMenuTypeName() {
        return menuTypeName;
    }

    // Этот метод должен быть обязательно!
    public void addItem(MenuItem item) {
        if (canAddItem(item)) {
            items.add(item);
        }
    }

    @Override
    public void displayMenu() {
        System.out.println("\n=== " + menuTypeName + " ===");
        for (int i = 0; i < items.size(); i++) {
            System.out.println((i + 1) + ". " + items.get(i));
        }
    }

    public List<MenuItem> getItems() {
        return new ArrayList<>(items);
    }

    // Абстрактный метод, который должны реализовать подклассы
    public abstract boolean canAddItem(MenuItem item);
}
