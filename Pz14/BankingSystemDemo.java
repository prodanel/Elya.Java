package Pz14;

import java.util.*;
import java.util.EventObject;
import java.util.EventListener;

class TransactionCompletedEvent extends EventObject {
    private double amount;
    private String recipient;
    private String transactionType;

    public TransactionCompletedEvent(Object source, double amount, String recipient, String transactionType) {
        super(source);
        this.amount = amount;
        this.recipient = recipient;
        this.transactionType = transactionType;
    }

    public double getAmount() {
        return amount;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getTransactionType() {
        return transactionType;
    }
}

interface TransactionListener extends EventListener {
    void transactionCompleted(TransactionCompletedEvent event);
}

class BankAccount {
    private String accountNumber;
    private double balance;
    private List<TransactionListener> transactionListeners;

    public BankAccount(String accountNumber, double initialBalance) {
        this.accountNumber = accountNumber;
        this.balance = initialBalance;
        this.transactionListeners = new ArrayList<>();
    }

    public void addTransactionListener(TransactionListener listener) {
        transactionListeners.add(listener);
    }

    public void removeTransactionListener(TransactionListener listener) {
        transactionListeners.remove(listener);
    }

    private void notifyTransactionCompleted(double amount, String recipient, String transactionType) {
        TransactionCompletedEvent event = new TransactionCompletedEvent(this, amount, recipient, transactionType);
        for (TransactionListener listener : transactionListeners) {
            listener.transactionCompleted(event);
        }
    }

    public boolean transferMoney(double amount, String recipientAccount) {
        if (amount <= 0) {
            System.out.println("Сумма перевода должна быть положительной");
            return false;
        }

        if (balance < amount) {
            System.out.println("Недостаточно средств на счете");
            return false;
        }

        balance -= amount;
        System.out.printf("Перевод выполнен: %.2f руб. -> %s%n", amount, recipientAccount);
        System.out.printf("Остаток на счете: %.2f руб.%n", balance);

        notifyTransactionCompleted(amount, recipientAccount, "TRANSFER");
        return true;
    }

    public double getBalance() {
        return balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }
}

class ConsoleTransactionListener implements TransactionListener {
    private String listenerName;

    public ConsoleTransactionListener(String name) {
        this.listenerName = name;
    }

    @Override
    public void transactionCompleted(TransactionCompletedEvent event) {
        System.out.printf("[%s] Транзакция завершена: %.2f руб. -> %s (%s)%n",
                listenerName,
                event.getAmount(),
                event.getRecipient(),
                event.getTransactionType());
    }
}

class LoggingTransactionListener implements TransactionListener {
    @Override
    public void transactionCompleted(TransactionCompletedEvent event) {
        String logEntry = String.format("LOG: %s - Перевод %.2f руб. получателю: %s",
                new Date(),
                event.getAmount(),
                event.getRecipient());
        System.out.println(logEntry);
    }
}

class FraudDetectionListener implements TransactionListener {
    private final double SUSPICIOUS_AMOUNT = 50000.0;@Override
    public void transactionCompleted(TransactionCompletedEvent event) {
        if (event.getAmount() >= SUSPICIOUS_AMOUNT) {
            System.out.printf("⚠️  ПРЕДУПРЕЖДЕНИЕ: Подозрительно крупный перевод! %.2f руб. -> %s%n",
                    event.getAmount(), event.getRecipient());
        }
    }
}

public class BankingSystemDemo {
    public static void main(String[] args) {
        System.out.println("=== БАНКОВСКАЯ СИСТЕМА ===");

        BankAccount account = new BankAccount("40702810500001234567", 100000.0);
        System.out.printf("Создан счет: %s с балансом: %.2f руб.%n%n",
                account.getAccountNumber(), account.getBalance());

        TransactionListener consoleListener = new ConsoleTransactionListener("Консольный монитор");
        TransactionListener loggingListener = new LoggingTransactionListener();
        TransactionListener fraudListener = new FraudDetectionListener();

        account.addTransactionListener(consoleListener);
        account.addTransactionListener(loggingListener);
        account.addTransactionListener(fraudListener);

        System.out.println("1. Перевод 15000 руб.");
        account.transferMoney(15000.0, "40817810500009876543");

        System.out.println("\n2. Перевод 55000 руб.");
        account.transferMoney(55000.0, "40702810500001112233");

        System.out.println("\n3. Попытка перевода больше чем на счете");
        account.transferMoney(50000.0, "40817810500009998877");

        System.out.println("\n4. Перевод 25000 руб.");
        account.transferMoney(25000.0, "40702810500004455667");

        System.out.println("\n--- Удаляем консольного слушателя ---");
        account.removeTransactionListener(consoleListener);

        System.out.println("5. Перевод 10000 руб. (после удаления слушателя)");
        account.transferMoney(10000.0, "40817810500007776655");

        System.out.printf("%nИтоговый баланс: %.2f руб.%n", account.getBalance());
    }
}
