package src.Server;

import Server.Managers.CollectionManager;
import Server.Managers.FileManager;
import Server.Model.Classes.Product;
import Server.Net.ServerNetManager;
import Server.Processors.CommandProcessor;

import java.io.File;
import java.io.IOException;
import java.util.PriorityQueue;

/**
 * Точка входа серверного приложения.
 * Отвечает за:
 * - Загрузку коллекции из файла при запуске
 * - Инициализацию сетевого менеджера
 * - Сохранение коллекции при завершении
 */
public class ServerMain {
    private static final int DEFAULT_PORT = 6778;

    public static void main(String[] args) {
        System.out.println("=".repeat(30));
        System.out.println("Запуск сервера управления коллекцией продуктов");
        System.out.println("=".repeat(30));

        FileManager fileManager = initializeFileManager(args);
        PriorityQueue<Product> collection = loadCollection(fileManager);
        CollectionManager collectionManager = new CollectionManager(collectio);

        System.out.println("[SERVER] Коллекция загружена. Размер: " + collectionManager.getSize());

        CommandProcessor commandProcessor = new CommandProcessor(collectionManager, fileManager);

        // 3. Запуск сетевого менеджера (Selector + DatagramChannel)
        ServerNetManager networkManager = new ServerNetManager(DEFAULT_PORT, commandProcessor);

        // 4. Регистрация хука для сохранения при завершении
        registerShutdownHook(fileManager, collectionManager);

        // 5. Запуск сервера (блокирующий вызов)
        System.out.println("[SERVER] Ожидание подключений на порту " + DEFAULT_PORT + "...");
        try {
            networkManager.start();
        } catch (IOException e) {
            System.err.println("[SERVER] Критическая ошибка сетевого менеджера: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Инициализация FileManager из аргументов или интерактивно
     */
    private static FileManager initializeFileManager(String[] args) {
        String fileName = null;

        if (args.length >= 1) {
            fileName = args[0].trim();
            System.out.println("[SERVER] Используется файл из аргументов: " + fileName);
        } else {
            // Интерактивный ввод пути к файлу
            fileName = readFileNameInteractively();
        }

        return validateAndCreateFileManager(fileName);
    }

    private static String readFileNameInteractively() {
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        String fileName;

        while (true) {
            System.out.print("[SERVER] Введите путь до файла для загрузки коллекции: ");
            fileName = scanner.nextLine().trim();

            if (fileName.isBlank()) {
                System.out.println("[ERROR] Имя файла не может быть пустым.");
                continue;
            }

            File file = new File(fileName);
            if (file.exists()) {
                return fileName;
            }

            System.out.print("[SERVER] Файл не существует. Создать новый? (y/n): ");
            String answer = scanner.nextLine().trim().toLowerCase();

            if (answer.equals("y") || answer.equals("yes")) {
                try {
                    // Создаем родительские директории если нужно
                    File parentDir = file.getParentFile();
                    if (parentDir != null && !parentDir.exists()) {
                        parentDir.mkdirs();
                    }
                    file.createNewFile();
                    System.out.println("[SERVER] Файл создан: " + fileName);
                    return fileName;
                } catch (IOException e) {
                    System.out.println("[ERROR] Не удалось создать файл: " + e.getMessage());
                }
            } else if (answer.equals("n") || answer.equals("no")) {
                System.exit(1);
            }
        }
    }

    /**
     * Валидация и создание FileManager
     */
    private static FileManager validateAndCreateFileManager(String fileName) {
        try {
            return new FileManager(fileName);
        } catch (IllegalArgumentException e) {
            System.err.println("[ERROR] Ошибка при работе с файлом: " + e.getMessage());
            System.exit(1);
            return null;
        }
    }

    /**
     * Загрузка коллекции из файла
     */
    private static PriorityQueue<Product> loadCollection(FileManager fileManager) {
        try {
            PriorityQueue<Product> products = fileManager.readCollection();
            if (products != null && !products.isEmpty()) {
                System.out.println("[SERVER] Успешно загружено элементов: " + products.size());
            } else {
                System.out.println("[SERVER] Коллекция пуста или файл не содержит данных");
            }
            return products;
        } catch (IllegalArgumentException e) {
            System.err.println("[WARNING] Ошибка при загрузке данных: " + e.getMessage());
            System.out.println("[SERVER] Будет использована пустая коллекция");
            return new PriorityQueue<>();
        }
    }

    /**
     * Регистрация хука для сохранения коллекции при завершении JVM
     */
    private static void registerShutdownHook(FileManager fileManager, CollectionManager collectionManager) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\n[SERVER] Завершение работы сервера...");
            try {
                fileManager.saveCollection(collectionManager.getCollection());
                System.out.println("[SERVER] Коллекция сохранена в файл: " + fileManager.getFileName());
            } catch (Exception e) {
                System.err.println("[ERROR] Не удалось сохранить коллекцию: " + e.getMessage());
            }
            System.out.println("[SERVER] Сервер остановлен.");
        }));
    }
}