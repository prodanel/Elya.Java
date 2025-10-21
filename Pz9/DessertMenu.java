package Pz9;

public class DessertMenu extends AbstractMenuType {
    public DessertMenu() {
        super("Десерты");
    }

    @Override
    public boolean canAddItem(MenuItem item) {
        return item.getType().equals("DESSERT");
    }
}
