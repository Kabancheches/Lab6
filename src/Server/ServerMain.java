package Server;

import Common.Model.Classes.Product;
import Server.Managers.CollectionManager;
import Server.Managers.CommandManager;
import Server.Managers.FileManager;
import Server.Net.ServerNetManager;

import java.io.IOException;
import java.util.PriorityQueue;

public class ServerMain {
    private static final int DEFAULT_PORT = 6778;

    public static void main(String[] args) {
        String str = "Запуск сервера управления коллекцией продуктов";
        System.out.println("=".repeat(str.length()));
        System.out.println(str);
        System.out.println("=".repeat(str.length()));


        if (args.length < 1) {
            System.out.println("ОШИБКА: Укажите путь к файлу коллекции первым аргументом.");
            System.out.println("Пример: java -jar server.jar /путь/к/файлу.csv");
            System.exit(1);
        }
        String filePath = args[0];
        FileManager fileManager = new FileManager(filePath);
            System.out.println("Корректный ли файл: " + fileManager.validateWritingReadingFile());
        PriorityQueue<Product> collection = fileManager.readCollection();
        if (collection == null) {
            System.err.println("Не удалось загрузить коллекцию, создана пустая.");
            collection = new PriorityQueue<>();
        }
        CollectionManager collectionManager = new CollectionManager(collection);
        System.out.println("Коллекция загружена. Размер: " + collectionManager.getCollection().size());

        CommandManager commandManager = new CommandManager(collectionManager, fileManager);

        ServerNetManager netManager;
        try {
            netManager = new ServerNetManager(DEFAULT_PORT, commandManager);
        } catch (IOException e) {
            System.err.println("Не удалось запустить сетевой менеджер: " + e.getMessage());
            return;
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println();
            System.out.println("Завершение работы, сохранение коллекции...");
            boolean saved = fileManager.saveCollection(collectionManager.getCollection());
            if (saved) {
                System.out.println("Коллекция сохранена.");
            } else {
                System.err.println("Не удалось сохранить коллекцию.");
            }
        }));

        System.out.println("Ожидание подключений на порту " + DEFAULT_PORT + "...");
        try {
            netManager.start();
        } catch (IOException e) {
            System.err.println("Критическая ошибка: " + e.getMessage());
            System.exit(1);
        }
    }
}