package Pz10;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoggerSingletonDemo {

    static class Logger {
        private static volatile Logger instance;
        private PrintWriter writer;
        private final String logFile = "Pz10/application.log";
        public enum LogLevel {
            INFO, WARNING, ERROR
        }

        private Logger() {
            try {
                FileWriter fileWriter = new FileWriter(logFile, true);
                writer = new PrintWriter(fileWriter);
                log(LogLevel.INFO, "Logger initialized successfully");
            } catch (IOException e) {
                System.err.println("Failed to initialize logger: " + e.getMessage());
            }
        }

        public static Logger getInstance() {
            if (instance == null) {
                synchronized (Logger.class) {
                    if (instance == null) {
                        instance = new Logger();
                    }
                }
            }
            return instance;
        }

        public void log(LogLevel level, String message) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String logEntry = String.format("[%s] %s: %s", timestamp, level, message);

            synchronized (this) {
                if (writer != null) {
                    writer.println(logEntry);
                    writer.flush();
                }
                System.out.println(logEntry);
            }
        }

        public void info(String message) {
            log(LogLevel.INFO, message);
        }

        public void warning(String message) {
            log(LogLevel.WARNING, message);
        }

        public void error(String message) {
            log(LogLevel.ERROR, message);
        }

        public void close() {
            if (writer != null) {
                log(LogLevel.INFO, "Logger shutting down");
                writer.close();
            }
        }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            throw new CloneNotSupportedException("Singleton cannot be cloned");
        }
    }

    static class Application {
        private String appName;

        public Application(String appName) {
            this.appName = appName;
        }

        public void start() {
            Logger logger = Logger.getInstance();
            logger.info("Application '" + appName + "' started");
        }

        public void processData(String data) {
            Logger logger = Logger.getInstance();
            logger.info("Processing data in '" + appName + "': " + data);

            if (data == null || data.isEmpty()) {
                logger.warning("Empty data received in '" + appName + "'");}
        }

        public void stop() {
            Logger logger = Logger.getInstance();
            logger.info("Application '" + appName + "' stopped");
        }
    }

    static class AuthenticationService {
        public boolean login(String username, String password) {
            Logger logger = Logger.getInstance();

            if (username == null || password == null) {
                logger.error("Login attempt with null credentials");
                return false;
            }

            if (username.equals("admin") && password.equals("password")) {
                logger.info("Successful login for user: " + username);
                return true;
            } else {
                logger.warning("Failed login attempt for user: " + username);
                return false;
            }
        }

        public void logout(String username) {
            Logger logger = Logger.getInstance();
            logger.info("User logged out: " + username);
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Демонстрация системы логирования Singleton ===");

        System.out.println("\n--- Сценарий 1: Проверка Singleton ---");
        Logger logger1 = Logger.getInstance();
        Logger logger2 = Logger.getInstance();

        System.out.println("logger1 и logger2 - один и тот же объект: " + (logger1 == logger2));

        System.out.println("\n--- Сценарий 2: Работа приложения ---");
        Application app1 = new Application("Web Server");
        Application app2 = new Application("Database Manager");

        app1.start();
        app2.start();

        app1.processData("Sample data 1");
        app2.processData("");

        System.out.println("\n--- Сценарий 3: Аутентификация ---");
        AuthenticationService authService = new AuthenticationService();

        authService.login("admin", "password");
        authService.login("user", "wrongpass");
        authService.login(null, "pass");

        authService.logout("admin");

        System.out.println("\n--- Сценарий 4: Многопоточность ---");

        Thread thread1 = new Thread(() -> {
            Logger logger = Logger.getInstance();
            for (int i = 1; i <= 3; i++) {
                logger.info("Thread 1 - Message " + i);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        Thread thread2 = new Thread(() -> {
            Logger logger = Logger.getInstance();
            for (int i = 1; i <= 3; i++) {
                logger.info("Thread 2 - Message " + i);
                try {
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\n--- Завершение ---");
        app1.stop();
        app2.stop();

        Logger.getInstance().close();

        System.out.println("\nПроверьте файл 'application.log' для просмотра всех записей логирования");
    }
}
