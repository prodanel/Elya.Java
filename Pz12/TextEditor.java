package Pz12;

import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;

public class TextEditor {
    public static void main(String[] args) {
        EditorApplication app = new EditorApplication();
        app.run();
    }
}

class EditorApplication {
    private TextDocument document;
    private CommandHistory history;
    private Scanner scanner;

    public EditorApplication() {
        this.document = new TextDocument();
        this.history = new CommandHistory();
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        System.out.println("=== Текстовый редактор с отменой действий ===");
        System.out.println("Доступные команды:");
        System.out.println("1 - Добавить текст");
        System.out.println("2 - Удалить текст");
        System.out.println("3 - Заменить текст");
        System.out.println("4 - Показать документ");
        System.out.println("5 - Отменить действие");
        System.out.println("6 - Вернуть действие");
        System.out.println("7 - Сохранить документ");
        System.out.println("8 - Загрузить документ");
        System.out.println("0 - Выход");

        while (true) {
            System.out.print("\nВыберите команду: ");
            String input = scanner.nextLine();

            try {
                int choice = Integer.parseInt(input);

                switch (choice) {
                    case 1:
                        addText();
                        break;
                    case 2:
                        deleteText();
                        break;
                    case 3:
                        replaceText();
                        break;
                    case 4:
                        showDocument();
                        break;
                    case 5:
                        undo();
                        break;
                    case 6:
                        redo();
                        break;
                    case 7:
                        saveDocument();
                        break;
                    case 8:
                        loadDocument();
                        break;
                    case 0:
                        System.out.println("Выход из редактора...");
                        return;
                    default:
                        System.out.println("Неверная команда!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Введите число от 0 до 8!");
            }
        }
    }

    private void addText() {
        System.out.print("Введите текст для добавления: ");
        String text = scanner.nextLine();
        System.out.print("Введите позицию для вставки (или -1 для конца): ");
        int position = Integer.parseInt(scanner.nextLine());

        Command command = new AddTextCommand(document, text, position);
        executeCommand(command);
    }

    private void deleteText() {
        System.out.print("Введите начальную позицию для удаления: ");
        int start = Integer.parseInt(scanner.nextLine());
        System.out.print("Введите длину удаляемого текста: ");
        int length = Integer.parseInt(scanner.nextLine());

        Command command = new DeleteTextCommand(document, start, length);
        executeCommand(command);
    }

    private void replaceText() {
        System.out.print("Введите текст для поиска: ");
        String search = scanner.nextLine();
        System.out.print("Введите текст для замены: ");
        String replacement = scanner.nextLine();

        Command command = new ReplaceTextCommand(document, search, replacement);
        executeCommand(command);}

    private void showDocument() {
        document.display();
        System.out.println("Длина документа: " + document.getContent().length() + " символов");
    }

    private void undo() {
        if (history.canUndo()) {
            history.undo();
            System.out.println("Действие отменено");
            showDocument();
        } else {
            System.out.println("Нет действий для отмены");
        }
    }

    private void redo() {
        if (history.canRedo()) {
            history.redo();
            System.out.println("Действие возвращено");
            showDocument();
        } else {
            System.out.println("Нет действий для возврата");
        }
    }

    private void saveDocument() {
        System.out.print("Введите имя файла для сохранения: ");
        String filename = scanner.nextLine();
        Command command = new SaveDocumentCommand(document, filename);
        executeCommand(command);
    }

    private void loadDocument() {
        System.out.print("Введите имя файла для загрузки: ");
        String filename = scanner.nextLine();
        Command command = new LoadDocumentCommand(document, filename);
        executeCommand(command);
    }

    private void executeCommand(Command command) {
        command.execute();
        history.push(command);
        showDocument();
    }
}

interface Command {
    void execute();
    void undo();
    void redo();
}

interface DocumentState {
    String getContent();
}

class TextDocument {
    private StringBuilder content;
    private DocumentHistory history;

    public TextDocument() {
        this.content = new StringBuilder();
        this.history = new DocumentHistory();
    }

    public TextDocument(String initialContent) {
        this.content = new StringBuilder(initialContent);
        this.history = new DocumentHistory();
        saveState();
    }

    public String getContent() {
        return content.toString();
    }

    public void setContent(String content) {
        this.content = new StringBuilder(content);
    }

    public void insert(String text, int position) {
        if (position < 0 || position > content.length()) {
            content.append(text);
        } else {
            content.insert(position, text);
        }
    }

    public void delete(int start, int length) {
        if (start < 0 || start >= content.length()) {
            return;
        }
        int end = Math.min(start + length, content.length());
        content.delete(start, end);
    }

    public void replace(String search, String replacement) {
        String currentContent = content.toString();
        String newContent = currentContent.replace(search, replacement);
        content = new StringBuilder(newContent);
    }

    public DocumentState saveState() {
        DocumentState state = new ConcreteDocumentState(content.toString());
        history.saveState(state);
        return state;
    }

    public void restoreState(DocumentState state) {
        this.content = new StringBuilder(state.getContent());
    }

    public void display() {
        System.out.println("\n=== СОДЕРЖИМОЕ ДОКУМЕНТА ===");
        if (content.length() == 0) {
            System.out.println("[Документ пуст]");
        } else {
            System.out.println(content.toString());
        }
        System.out.println("==========================\n");
    }

    private static class ConcreteDocumentState implements DocumentState {
        private final String content;

        public ConcreteDocumentState(String content) {
            this.content = content;}

        @Override
        public String getContent() {
            return content;
        }
    }

    private class DocumentHistory {
        private final Stack<DocumentState> states = new Stack<>();

        public void saveState(DocumentState state) {
            states.push(state);
        }

        public DocumentState getLastState() {
            if (states.isEmpty()) {
                return new ConcreteDocumentState("");
            }
            return states.pop();
        }

        public boolean hasStates() {
            return !states.isEmpty();
        }
    }
}


class AddTextCommand implements Command {
    private TextDocument document;
    private String text;
    private int position;
    private DocumentState previousState;

    public AddTextCommand(TextDocument document, String text, int position) {
        this.document = document;
        this.text = text;
        this.position = position;
    }

    @Override
    public void execute() {
        previousState = document.saveState();
        document.insert(text, position);
    }

    @Override
    public void undo() {
        document.restoreState(previousState);
    }

    @Override
    public void redo() {
        document.insert(text, position);
    }

    @Override
    public String toString() {
        return "Добавление текста: '" + text + "' в позицию " + position;
    }
}

class DeleteTextCommand implements Command {
    private TextDocument document;
    private int start;
    private int length;
    private String deletedText;
    private DocumentState previousState;

    public DeleteTextCommand(TextDocument document, int start, int length) {
        this.document = document;
        this.start = start;
        this.length = length;
    }

    @Override
    public void execute() {
        previousState = document.saveState();
        String content = document.getContent();
        if (start >= 0 && start < content.length()) {
            int end = Math.min(start + length, content.length());
            deletedText = content.substring(start, end);
            document.delete(start, length);
        }
    }

    @Override
    public void undo() {
        document.restoreState(previousState);
    }

    @Override
    public void redo() {
        document.delete(start, length);
    }

    @Override
    public String toString() {
        return "Удаление текста: '" + deletedText + "' с позиции " + start;
    }
}

class ReplaceTextCommand implements Command {
    private TextDocument document;
    private String search;
    private String replacement;
    private DocumentState previousState;

    public ReplaceTextCommand(TextDocument document, String search, String replacement) {
        this.document = document;
        this.search = search;
        this.replacement = replacement;
    }

    @Override
    public void execute() {
        previousState = document.saveState();
        document.replace(search, replacement);
    }

    @Override
    public void undo() {
        document.restoreState(previousState);
    }

    @Override
    public void redo() {
        document.replace(search, replacement);
    }

    @Override
    public String toString() {
        return "Замена '" + search + "' на '" + replacement + "'";
    }
}

class SaveDocumentCommand implements Command {
    private TextDocument document;
    private String filename;
    private DocumentState previousState;

    public SaveDocumentCommand(TextDocument document, String filename) {
        this.document = document;this.filename = filename;
    }

    @Override
    public void execute() {
        previousState = document.saveState();
        System.out.println("Документ сохранен в файл: " + filename);
        System.out.println("Содержимое: " + document.getContent());
    }

    @Override
    public void undo() {
        System.out.println("Отмена сохранения - файл удален: " + filename);
    }

    @Override
    public void redo() {
        System.out.println("Повторное сохранение в файл: " + filename);
        System.out.println("Содержимое: " + document.getContent());
    }

    @Override
    public String toString() {
        return "Сохранение документа в файл: " + filename;
    }
}

class LoadDocumentCommand implements Command {
    private TextDocument document;
    private String filename;
    private DocumentState previousState;

    public LoadDocumentCommand(TextDocument document, String filename) {
        this.document = document;
        this.filename = filename;
    }

    @Override
    public void execute() {
        previousState = document.saveState();
        String loadedContent = "[Загруженное содержимое из " + filename + "] Пример текста после загрузки.";
        document.setContent(loadedContent);
        System.out.println("Документ загружен из файла: " + filename);
    }

    @Override
    public void undo() {
        document.restoreState(previousState);
        System.out.println("Отмена загрузки - восстановлен предыдущий документ");
    }

    @Override
    public void redo() {
        String loadedContent = "[Загруженное содержимое из " + filename + "] Пример текста после загрузки.";
        document.setContent(loadedContent);
        System.out.println("Повторная загрузка из файла: " + filename);
    }

    @Override
    public String toString() {
        return "Загрузка документа из файла: " + filename;
    }
}

class CommandHistory {
    private Stack<Command> undoStack = new Stack<>();
    private Stack<Command> redoStack = new Stack<>();

    public void push(Command command) {
        undoStack.push(command);
        redoStack.clear();
    }

    public void undo() {
        if (!undoStack.isEmpty()) {
            Command command = undoStack.pop();
            command.undo();
            redoStack.push(command);
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            Command command = redoStack.pop();
            command.redo();
            undoStack.push(command);
        }
    }

    public boolean canUndo() {
        return !undoStack.isEmpty();
    }

    public boolean canRedo() {
        return !redoStack.isEmpty();
    }

    public void displayHistory() {
        System.out.println("История команд (последняя - сверху):");
        for (int i = undoStack.size() - 1; i >= 0; i--) {
            System.out.println((undoStack.size() - i) + ". " + undoStack.get(i));
        }
    }
}
