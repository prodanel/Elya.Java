package Pz11;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;

public class MessageDecoratorDemo {

    interface Message {
        String getContent();
        void display();
    }

    static class BasicMessage implements Message {
        private String content;

        public BasicMessage(String content) {
            this.content = content;
        }

        @Override
        public String getContent() {
            return content;
        }

        @Override
        public void display() {
            System.out.println("Сообщение: " + content);
        }
    }

    static abstract class MessageDecorator implements Message {
        protected Message decoratedMessage;

        public MessageDecorator(Message message) {
            this.decoratedMessage = message;
        }

        @Override
        public String getContent() {
            return decoratedMessage.getContent();
        }

        @Override
        public void display() {
            decoratedMessage.display();
        }
    }

    static class EncryptedMessage extends MessageDecorator {
        public EncryptedMessage(Message message) {
            super(message);
        }

        @Override
        public String getContent() {
            String originalContent = decoratedMessage.getContent();
            return encrypt(originalContent);
        }

        @Override
        public void display() {
            String encryptedContent = getContent();
            System.out.println("Зашифрованное сообщение: " + encryptedContent);
        }

        private String encrypt(String content) {
            try {
                return Base64.getEncoder().encodeToString(content.getBytes(StandardCharsets.UTF_8));
            } catch (Exception e) {
                return "Ошибка шифрования: " + e.getMessage();
            }
        }

        public String decrypt() {
            try {
                byte[] decodedBytes = Base64.getDecoder().decode(getContent());
                return new String(decodedBytes, StandardCharsets.UTF_8);
            } catch (Exception e) {
                return "Ошибка расшифровки: " + e.getMessage();
            }
        }
    }

    static class TimestampedMessage extends MessageDecorator {
        public TimestampedMessage(Message message) {
            super(message);
        }

        @Override
        public String getContent() {
            String originalContent = decoratedMessage.getContent();
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            return originalContent + " [Время отправки: " + timestamp + "]";
        }

        @Override
        public void display() {
            System.out.println("Сообщение с временной меткой: " + getContent());
        }

        public String getTimestamp() {
            return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
    }

    static class SignedMessage extends MessageDecorator {
        private String signer;

        public SignedMessage(Message message, String signer) {
            super(message);
            this.signer = signer;
        }

        @Override
        public String getContent() {
            String originalContent = decoratedMessage.getContent();
            String signature = generateSignature(originalContent);
            return originalContent + " [Подпись: " + signature + ", Подписал: " + signer + "]";
        }@Override
        public void display() {
            System.out.println("Подписанное сообщение: " + getContent());
        }

        private String generateSignature(String content) {
            try {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] hash = digest.digest(content.getBytes(StandardCharsets.UTF_8));
                return Base64.getEncoder().encodeToString(hash).substring(0, 16) + "...";
            } catch (Exception e) {
                return "Ошибка подписи: " + e.getMessage();
            }
        }

        public String getSigner() {
            return signer;
        }
    }

    static class HTMLMessage extends MessageDecorator {
        public HTMLMessage(Message message) {
            super(message);
        }

        @Override
        public String getContent() {
            String originalContent = decoratedMessage.getContent();
            return "<html><body><p>" + originalContent + "</p></body></html>";
        }

        @Override
        public void display() {
            System.out.println("HTML сообщение: " + getContent());
        }
    }

    static class UrgentMessage extends MessageDecorator {
        public UrgentMessage(Message message) {
            super(message);
        }

        @Override
        public String getContent() {
            String originalContent = decoratedMessage.getContent();
            return "!!! СРОЧНО !!! " + originalContent + " !!! СРОЧНО !!!";
        }

        @Override
        public void display() {
            System.out.println("Срочное сообщение: " + getContent());
        }
    }

    static class MessageSystem {
        public static void processMessage(Message message, String description) {
            System.out.println("\n--- " + description + " ---");
            message.display();
            System.out.println("Полное содержимое: " + message.getContent());
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Демонстрация системы персонализации сообщений с шаблоном Decorator ===\n");

        Message basicMessage = new BasicMessage("Важное уведомление для всех сотрудников");

        MessageSystem.processMessage(basicMessage, "Базовое сообщение");

        Message timestamped = new TimestampedMessage(basicMessage);
        MessageSystem.processMessage(timestamped, "Сообщение с временной меткой");

        Message encrypted = new EncryptedMessage(basicMessage);
        MessageSystem.processMessage(encrypted, "Зашифрованное сообщение");

        if (encrypted instanceof EncryptedMessage) {
            String decrypted = ((EncryptedMessage) encrypted).decrypt();
            System.out.println("Расшифрованное содержимое: " + decrypted);
        }

        Message signed = new SignedMessage(basicMessage, "Директор Иванов");
        MessageSystem.processMessage(signed, "Подписанное сообщение");

        Message timestampedAndSigned = new TimestampedMessage(new SignedMessage(basicMessage, "Главный бухгалтер"));
        MessageSystem.processMessage(timestampedAndSigned, "Подписанное сообщение с временной меткой");

        Message htmlMessage = new HTMLMessage(basicMessage);
        MessageSystem.processMessage(htmlMessage, "HTML сообщение");
        Message urgentEncryptedSigned = new UrgentMessage(
                new EncryptedMessage(
                        new SignedMessage(basicMessage, "Секретарь")
                )
        );
        MessageSystem.processMessage(urgentEncryptedSigned, "Срочное зашифрованное подписанное сообщение");

        Message fullDecorated = new UrgentMessage(
                new HTMLMessage(
                        new TimestampedMessage(
                                new EncryptedMessage(
                                        new SignedMessage(basicMessage, "Системный администратор")
                                )
                        )
                )
        );
        MessageSystem.processMessage(fullDecorated, "Полностью декорированное сообщение");

        System.out.println("\n=== Разные типы сообщений с различными декораторами ===");

        Message personalMessage = new BasicMessage("Привет! Как дела?");
        Message decoratedPersonal = new TimestampedMessage(
                new EncryptedMessage(personalMessage)
        );
        MessageSystem.processMessage(decoratedPersonal, "Личное сообщение");

        Message officialMessage = new BasicMessage("Завтра собрание в 10:00 в зале заседаний");
        Message decoratedOfficial = new SignedMessage(
                new TimestampedMessage(officialMessage),
                "Начальник отдела"
        );
        MessageSystem.processMessage(decoratedOfficial, "Официальное уведомление");

        Message emergencyMessage = new BasicMessage("Пожарная тревога! Немедленно покинуть здание!");
        Message decoratedEmergency = new UrgentMessage(
                new TimestampedMessage(emergencyMessage)
        );
        MessageSystem.processMessage(decoratedEmergency, "Экстренное оповещение");

        System.out.println("\n=== Демонстрация гибкости и переиспользования ===");

        Message base = new BasicMessage("Одно сообщение - много форматов");

        Message[] decoratedVariants = {
                base,
                new TimestampedMessage(base),
                new EncryptedMessage(base),
                new SignedMessage(base, "Отправитель"),
                new UrgentMessage(new TimestampedMessage(base)),
                new HTMLMessage(new SignedMessage(base, "Веб-мастер"))
        };

        for (int i = 0; i < decoratedVariants.length; i++) {
            MessageSystem.processMessage(decoratedVariants[i], "Вариант " + (i + 1));
        }

        System.out.println("\n=== Демонстрация завершена ===");
    }
}
