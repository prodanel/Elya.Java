package Pz9;

public class VeganMenu extends AbstractMenuType {
    public VeganMenu() {
        super("Веганское меню");
    }

    @Override
    public boolean canAddItem(MenuItem item) {
        String name = item.getName().toLowerCase();
        return !name.contains("мясо") &&
                !name.contains("курица") &&
                !name.contains("рыба") &&
                !name.contains("сыр") &&
                !name.contains("молоко") &&
                !name.contains("яйцо");
    }
}
