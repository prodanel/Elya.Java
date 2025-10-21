package Pz9;

public abstract class AbstractMenuItem implements MenuItem {
    protected String name;
    protected double price;
    protected String description;
    protected String type;

    public AbstractMenuItem(String name, double price, String description, String type) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.type = type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return String.format("%s - %.2f руб. (%s)", name, price, description);
    }
}
