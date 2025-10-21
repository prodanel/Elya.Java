package Pz9;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MenuFactory {
    private static Map<String, MenuType> menuTypes = new HashMap<>();

    static {
        // Регистрируем стандартные типы меню
        registerMenuType("MAIN", new MainMenu());
        registerMenuType("DESSERT", new DessertMenu());
        registerMenuType("DRINK", new DrinkMenu());
        registerMenuType("VEGAN", new VeganMenu());
    }

    public static void registerMenuType(String key, MenuType menuType) {
        menuTypes.put(key, menuType);
    }

    public static MenuType getMenuType(String key) {
        return menuTypes.get(key);
    }

    public static Collection<MenuType> getAllMenuTypes() {
        return menuTypes.values();
    }
}