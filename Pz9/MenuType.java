package Pz9;

public interface MenuType {
    String getMenuTypeName();
    boolean canAddItem(MenuItem item);
    void displayMenu();
}
