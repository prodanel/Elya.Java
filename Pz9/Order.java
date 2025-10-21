package Pz9;

public interface Order {
    void addItem(MenuItem item, int quantity);
    void removeItem(MenuItem item);
    double calculateTotal();
    void displayOrder();
}
