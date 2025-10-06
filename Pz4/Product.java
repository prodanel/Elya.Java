package Pz4;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

    interface ProductInterface {
        String getFullName();
        double getPrice();
        int getStoragePeriod();
        String getGrade();
        String getProductionDate();
        int getExpirationDays();
        String getArticleNumber();

        void printInfo();

        boolean isExpired(String currentDate);
    }

    class Product implements ProductInterface {
        private String name;
        private double price;
        private int storagePeriod;
        private String grade;
        private String productionDate;
        private int expirationDays;
        private String articleNumber;

        public Product(String name, double price, int storagePeriod, String grade,
                       String productionDate, int expirationDays, String articleNumber) {
            this.name = name;
            this.price = price;
            this.storagePeriod = storagePeriod;
            this.grade = grade;
            this.productionDate = productionDate;
            this.expirationDays = expirationDays;
            this.articleNumber = articleNumber;
        }

        @Override
        public String getFullName() {
            return name + " (" + grade + " сорт)";
        }

        @Override
        public double getPrice() {
            return price;
        }

        @Override
        public int getStoragePeriod() {
            return storagePeriod;
        }

        @Override
        public String getGrade() {
            return grade;
        }

        @Override
        public String getProductionDate() {
            return productionDate;
        }

        @Override
        public int getExpirationDays() {
            return expirationDays;
        }

        @Override
        public String getArticleNumber() {
            return articleNumber;
        }

        @Override
        public void printInfo() {
            System.out.println("=== Информация о товаре ===");
            System.out.println("Наименование: " + getFullName());
            System.out.println("Артикул: " + articleNumber);
            System.out.println("Стоимость: " + price + " руб.");
            System.out.println("Сорт: " + grade);
            System.out.println("Дата выпуска: " + productionDate);
            System.out.println("Срок годности: " + expirationDays + " дней");
            System.out.println("Срок хранения: " + storagePeriod + " дней");
            System.out.println("Просрочен: " + (isExpired(getCurrentDate()) ? "Да" : "Нет"));
            System.out.println("============================");
        }

        @Override
        public boolean isExpired(String currentDate) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                LocalDate production = LocalDate.parse(productionDate, formatter);
                LocalDate current = LocalDate.parse(currentDate, formatter);

                long daysBetween = ChronoUnit.DAYS.between(production, current);
                return daysBetween > expirationDays;
            } catch (Exception e) {
                System.out.println("Ошибка при проверке срока годности");
                return false;
            }
        }

        private String getCurrentDate() {
            return LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        }
    }

