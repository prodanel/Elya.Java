package Pz3;

public class Product {
    String name;
    double price;
    int shelfLife;
    String grade;
    String releaseDate;
    String expiryDate;
    String article;

    public Product(String name, double price, String article) {
        this.name = name;
        this.price = price;
        this.article = article;
    }

    public Product(String name, double price, int shelfLife, String grade,
                   String releaseDate, String expiryDate, String article) {
        this.name = name;
        this.price = price;
        this.shelfLife = shelfLife;
        this.grade = grade;
        this.releaseDate = releaseDate;
        this.expiryDate = expiryDate;
        this.article = article;
    }

    public void printInfo() {
        System.out.println("=== ИНФОРМАЦИЯ О ТОВАРЕ ===");
        System.out.println("Наименование: " + name);
        System.out.println("Стоимость: " + price + " руб.");
        System.out.println("Срок хранения: " + shelfLife + " дней");
        System.out.println("Сорт: " + grade);
        System.out.println("Дата выпуска: " + releaseDate);
        System.out.println("Срок годности: " + expiryDate);
        System.out.println("Артикул: " + article);
        System.out.println("============================");
    }

    public void applyDiscount(double discountPercent) {
        double discount = price * (discountPercent / 100);
        double oldPrice = price;
        price = price - discount;
        System.out.println("Применена скидка " + discountPercent + "%");
        System.out.println("Старая цена: " + oldPrice + " руб.");
        System.out.println("Новая цена: " + price + " руб.");
    }

    public void checkExpiry(String currentDate) {
        System.out.println("Срок годности до: " + expiryDate);
        System.out.println("Сегодняшняя дата: " + currentDate);
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getArticle() {
        return article;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public void setShelfLife(int shelfLife) {
        this.shelfLife = shelfLife;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }
}

