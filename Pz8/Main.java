package Pz8;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

abstract class Tour {
    protected String name;
    protected String destination;
    protected LocalDate startDate;
    protected LocalDate endDate;
    protected double basePrice;

    public Tour(String name, String destination, LocalDate startDate, LocalDate endDate, double basePrice) {
        this.name = name;
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.basePrice = basePrice;
    }

    public abstract double calculateTotalPrice();

    public String getName() { return name; }
    public String getDestination() { return destination; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public double getBasePrice() { return basePrice; }

    @Override
    public String toString() {
        return String.format("Тур: %s | Направление: %s | Дата: %s - %s | Базовая цена: %.2f ₽",
                name, destination, startDate, endDate, basePrice);
    }
}

class ExcursionTour extends Tour {
    private int numberOfExcursions;
    private static final double EXCURSION_TAX = 0.1; // 10% налог

    public ExcursionTour(String name, String destination, LocalDate startDate,
                         LocalDate endDate, double basePrice, int numberOfExcursions) {
        super(name, destination, startDate, endDate, basePrice);
        this.numberOfExcursions = numberOfExcursions;
    }

    @Override
    public double calculateTotalPrice() {
        return basePrice + (basePrice * EXCURSION_TAX) + (numberOfExcursions * 1000);
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" | Тип: Экскурсионный | Экскурсии: %d | Итог: %.2f ₽",
                numberOfExcursions, calculateTotalPrice());
    }
}

class BeachTour extends Tour {
    private boolean allInclusive;
    private static final double BEACH_TAX = 0.08; // 8% налог

    public BeachTour(String name, String destination, LocalDate startDate,
                     LocalDate endDate, double basePrice, boolean allInclusive) {
        super(name, destination, startDate, endDate, basePrice);
        this.allInclusive = allInclusive;
    }

    @Override
    public double calculateTotalPrice() {
        double total = basePrice + (basePrice * BEACH_TAX);
        if (allInclusive) {
            total += 5000; // Доплата за all-inclusive
        }
        return total;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" | Тип: Пляжный | All-Inclusive: %s | Итог: %.2f ₽",
                allInclusive ? "Да" : "Нет", calculateTotalPrice());
    }
}

class Client {
    private String name;
    private String phone;
    private String email;

    public Client(String name, String phone, String email) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Имя клиента не может быть пустым");
        }
        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("Телефон клиента не может быть пустым");
        }

        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }

    @Override
    public String toString() {
        return String.format("Клиент: %s | Телефон: %s | Email: %s", name, phone, email);
    }
}
class Guide {
    private String name;
    private String specialization;
    private double hourlyRate;

    public Guide(String name, String specialization, double hourlyRate) {
        this.name = name;
        this.specialization = specialization;
        this.hourlyRate = hourlyRate;
    }

    public String getName() { return name; }
    public String getSpecialization() { return specialization; }
    public double getHourlyRate() { return hourlyRate; }

    @Override
    public String toString() {
        return String.format("Гид: %s | Специализация: %s | Ставка: %.2f ₽/час",
                name, specialization, hourlyRate);
    }
}

class Booking {
    private static int nextId = 1;

    private int id;
    private Client client;
    private Tour tour;
    private LocalDateTime bookingDate;
    private boolean isConfirmed;

    public Booking(Client client, Tour tour) {
        this.id = nextId++;
        this.client = client;
        this.tour = tour;
        this.bookingDate = LocalDateTime.now();
        this.isConfirmed = false;
    }

    public void confirmBooking() {
        this.isConfirmed = true;
    }

    public int getId() { return id; }
    public Client getClient() { return client; }
    public Tour getTour() { return tour; }
    public LocalDateTime getBookingDate() { return bookingDate; }
    public boolean isConfirmed() { return isConfirmed; }

    @Override
    public String toString() {
        return String.format("Бронирование #%d | %s | %s | Дата: %s | Статус: %s | Стоимость: %.2f ₽",
                id, client.getName(), tour.getName(), bookingDate,
                isConfirmed ? "Подтверждено" : "Ожидание", tour.calculateTotalPrice());
    }
}

class TravelAgency {
    private List<Tour> tours;
    private List<Client> clients;
    private List<Booking> bookings;
    private List<Guide> guides;

    public TravelAgency() {
        this.tours = new ArrayList<>();
        this.clients = new ArrayList<>();
        this.bookings = new ArrayList<>();
        this.guides = new ArrayList<>();
    }

    public void addTour(Tour tour) {
        tours.add(tour);
    }

    public void addClient(Client client) {
        clients.add(client);
    }

    public void addGuide(Guide guide) {
        guides.add(guide);
    }

    public List<Tour> findToursByDate(LocalDate date) {
        return tours.stream()
                .filter(tour -> tour.getStartDate().equals(date))
                .collect(Collectors.toList());
    }

    public List<Tour> findToursByPriceRange(double minPrice, double maxPrice) {
        return tours.stream()
                .filter(tour -> tour.calculateTotalPrice() >= minPrice &&
                        tour.calculateTotalPrice() <= maxPrice)
                .collect(Collectors.toList());
    }

    public Booking bookTour(Client client, Tour tour) {
        try {
            if (!clients.contains(client)) {
                throw new IllegalArgumentException("Клиент не зарегистрирован в системе");
            }
            if (!tours.contains(tour)) {
                throw new IllegalArgumentException("Тур не найден в системе");
            }

            Booking booking = new Booking(client, tour);
            bookings.add(booking);
            return booking;

        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка бронирования: " + e.getMessage());
            return null;}
    }

    public List<Tour> getAllTours() {
        return new ArrayList<>(tours);
    }

    public List<Booking> getAllBookings() {
        return new ArrayList<>(bookings);
    }

    public List<Guide> getAllGuides() {
        return new ArrayList<>(guides);
    }

    public List<Client> getAllClients() {
        return new ArrayList<>(clients);
    }
}

public class Main {
    public static void main(String[] args) {
        TravelAgency agency = new TravelAgency();

        try {
            System.out.println("=== СОЗДАНИЕ КЛИЕНТОВ ===");
            Client client1 = new Client("Иван Петров", "+79161234567", "ivan@mail.ru");
            Client client2 = new Client("Мария Сидорова", "+79037654321", "maria@mail.ru");

            agency.addClient(client1);
            agency.addClient(client2);

            System.out.println(client1);
            System.out.println(client2);

            System.out.println("\n=== СОЗДАНИЕ ГИДОВ ===");
            Guide guide1 = new Guide("Анна Ковалева", "История", 1500);
            Guide guide2 = new Guide("Дмитрий Соколов", "Природа", 1200);

            agency.addGuide(guide1);
            agency.addGuide(guide2);

            System.out.println(guide1);
            System.out.println(guide2);

            System.out.println("\n=== СОЗДАНИЕ ТУРОВ ===");
            Tour tour1 = new ExcursionTour("Римские каникулы", "Италия",
                    LocalDate.of(2024, 6, 15), LocalDate.of(2024, 6, 22), 50000, 5);

            Tour tour2 = new BeachTour("Отдых в Турции", "Турция",
                    LocalDate.of(2024, 7, 1), LocalDate.of(2024, 7, 14), 70000, true);

            Tour tour3 = new ExcursionTour("Парижские тайны", "Франция",
                    LocalDate.of(2024, 6, 15), LocalDate.of(2024, 6, 25), 65000, 7);

            Tour tour4 = new BeachTour("Греческие острова", "Греция",
                    LocalDate.of(2024, 8, 10), LocalDate.of(2024, 8, 20), 55000, false);

            agency.addTour(tour1);
            agency.addTour(tour2);
            agency.addTour(tour3);
            agency.addTour(tour4);

            System.out.println("\n=== ВСЕ ТУРЫ ===");
            for (Tour tour : agency.getAllTours()) {
                System.out.println(tour);
            }

            System.out.println("\n=== ПОИСК ТУРОВ ПО ДАТЕ (15.06.2024) ===");
            List<Tour> toursByDate = agency.findToursByDate(LocalDate.of(2024, 6, 15));
            if (toursByDate.isEmpty()) {
                System.out.println("Туры на указанную дату не найдены");
            } else {
                for (Tour tour : toursByDate) {
                    System.out.println(tour);
                }
            }

            System.out.println("\n=== ПОИСК ТУРОВ ПО ЦЕНЕ (60000-80000 ₽) ===");
            List<Tour> toursByPrice = agency.findToursByPriceRange(60000, 80000);
            if (toursByPrice.isEmpty()) {
                System.out.println("Туры в указанном ценовом диапазоне не найдены");
            } else {
                for (Tour tour : toursByPrice) {
                    System.out.println(tour);
                }
            }

            System.out.println("\n=== БРОНИРОВАНИЯ ===");

            Booking booking1 = agency.bookTour(client1, tour1);if (booking1 != null) {
                booking1.confirmBooking();
                System.out.println("✓ Бронирование создано: " + booking1);
            }

            Booking booking2 = agency.bookTour(client2, tour2);
            if (booking2 != null) {
                System.out.println("✓ Бронирование создано: " + booking2);
            }

            Booking booking3 = agency.bookTour(client1, tour3);
            if (booking3 != null) {
                booking3.confirmBooking();
                System.out.println("✓ Бронирование создано: " + booking3);
            }

            System.out.println("\n=== ПРОВЕРКА ОБРАБОТКИ ОШИБОК ===");
            Client fakeClient = new Client("Тест", "123", "test@test.ru");
            Booking booking4 = agency.bookTour(fakeClient, tour1);
            if (booking4 == null) {
                System.out.println("✗ Бронирование не выполнено: клиент не зарегистрирован");
            }

            System.out.println("\n=== ВСЕ БРОНИРОВАНИЯ ===");
            List<Booking> allBookings = agency.getAllBookings();
            if (allBookings.isEmpty()) {
                System.out.println("Бронирований нет");
            } else {
                for (Booking booking : allBookings) {
                    System.out.println(booking);
                }
            }

            System.out.println("\n=== СВОДКА ===");
            System.out.println("Всего туров: " + agency.getAllTours().size());
            System.out.println("Всего клиентов: " + agency.getAllClients().size());
            System.out.println("Всего гидов: " + agency.getAllGuides().size());
            System.out.println("Всего бронирований: " + agency.getAllBookings().size());

            double totalRevenue = allBookings.stream()
                    .mapToDouble(booking -> booking.getTour().calculateTotalPrice())
                    .sum();
            System.out.printf("Общая стоимость всех бронирований: %.2f ₽\n", totalRevenue);

        } catch (Exception e) {
            System.out.println("Произошла ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
