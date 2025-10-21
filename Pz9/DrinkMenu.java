package Pz9;

public class DrinkMenu extends AbstractMenuType {
    public DrinkMenu() {
        super("Напитки");
    }

    @Override
    public boolean canAddItem(MenuItem item) {
        return item.getType().equals("DRINK");
    }
}
