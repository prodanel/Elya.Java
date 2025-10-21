package Pz9;

public class MainMenu extends AbstractMenuType {
    public MainMenu() {
        super("Основное меню");
    }

    @Override
    public boolean canAddItem(MenuItem item) {
        return item.getType().equals("MAIN_COURSE") ||
                item.getType().equals("SALAD");
    }
}
