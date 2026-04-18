package src.Client;

import src.Client.View.ConsoleUI;
import src.Client.View.InputReader;
import src.Client.Net.ClientNetManager;
import Common.Commands.CommandRequest;
import Common.Commands.CommandResponse;
import Common.Commands.CommandType;
import Common.Model.Product;

import java.io.IOException;

/**
 * Точка входа клиентского приложения.
 * Отвечает за:
 * - Чтение команд из консоли
 * - Валидацию вводимых данных
 * - Сериализацию команды и её аргументов
 * - Отправку команды на сервер
 * - Обработку ответа от сервера
 */
public class ClientMain {
    public static void main(String[] args) {
        ConsoleUI ui = new ConsoleUI();
        InputReader inputReader = new InputReader();
        ClientNetManager netManager = null;
        
        ui.printSeparator();
        System.out.println("Клиент управления коллекцией продуктов");
        ui.printSeparator();
        
        try {
            netManager = new ClientNetManager();
            ui.printSuccess("Сетевое соединение установлено");
        } catch (IOException e) {
            ui.printError("Не удалось установить сетевое соединение: " + e.getMessage());
            System.exit(1);
        }
        
        boolean needExit = false;
        
        while (!needExit) {
            System.out.print("> ");
            String input;
            try {
                input = inputReader.readLine();
            } catch (IOException e) {
                ui.printError("Ошибка ввода: " + e.getMessage());
                continue;
            }
            
            if (input == null || input.trim().isEmpty()) {
                continue;
            }
            
            String[] parts = input.trim().split("\\s+", 2);
            String commandName = parts[0].toLowerCase();
            String argumentStr = parts.length > 1 ? parts[1] : null;
            
            // Проверка команды exit перед созданием запроса
            if ("exit".equals(commandName)) {
                ui.printInfo("Завершение работы клиента...");
                needExit = true;
                break;
            }
            
            CommandRequest request = createCommandRequest(commandName, argumentStr, inputReader, ui);
            
            if (request == null) {
                continue;
            }
            
            // Отправка команды на сервер
            CommandResponse response = netManager.sendCommand(request);
            
            // Обработка ответа
            if (response != null) {
                if (response.isSuccess()) {
                    ui.printSuccess(response.getMessage());
                    Object data = response.getData();
                    if (data != null && !data.toString().isEmpty()) {
                        System.out.println(data);
                    }
                } else {
                    ui.printError(response.getMessage());
                }
            } else {
                ui.printError("Не получен ответ от сервера");
            }
        }
        
        // Закрытие сетевого соединения
        if (netManager != null) {
            netManager.close();
        }
        
        System.exit(0);
    }
    
    /**
     * Создает объект команды на основе введенной строки.
     * Команды передаются как объекты с CommandType (enum) и аргументом (Object).
     */
    private static CommandRequest createCommandRequest(String commandName, String argumentStr, 
                                                        InputReader inputReader, ConsoleUI ui) {
        try {
            CommandType commandType;
            try {
                commandType = CommandType.fromString(commandName);
            } catch (IllegalArgumentException e) {
                ui.printError("Неизвестная команда: " + commandName);
                ui.printInfo("Введите 'help' для получения справки.");
                return null;
            }
            
            Object argument = null;
            
            switch (commandType) {
                case HELP:
                case INFO:
                case SHOW:
                case CLEAR:
                    // Команды без аргументов
                    break;
                    
                case ADD:
                case ADD_IF_MIN:
                case REMOVE_GREATER:
                case REMOVE_LOWER:
                    Product product = inputReader.readProduct();
                    if (product == null) {
                        ui.printError("Не удалось создать продукт");
                        return null;
                    }
                    argument = product;
                    break;
                    
                case UPDATE:
                    if (argumentStr == null) {
                        ui.printError("Команда update требует ID продукта");
                        return null;
                    }
                    String[] updateParts = argumentStr.split("\\s+", 2);
                    Long updateId = Long.parseLong(updateParts[0]);
                    Product updateProduct = inputReader.readProduct();
                    if (updateProduct == null) {
                        ui.printError("Не удалось создать продукт для обновления");
                        return null;
                    }
                    // Для update аргумент - это массив [id, Product]
                    argument = new Object[]{updateId, updateProduct};
                    break;
                    
                case REMOVE_BY_ID:
                    if (argumentStr == null) {
                        ui.printError("Команда remove_by_id требует ID");
                        return null;
                    }
                    argument = Long.parseLong(argumentStr.trim());
                    break;
                    
                case FILTER_BY_PRICE:
                case FILTER_GREATER_THAN_PRICE:
                    if (argumentStr == null) {
                        ui.printError("Команда " + commandName + " требует цену");
                        return null;
                    }
                    argument = Float.parseFloat(argumentStr.trim().replace(',', '.'));
                    break;
                    
                case SAVE:
                    // Команда save доступна только серверу, но клиент может отправить запрос
                    break;
                    
                case EXIT:
                    // Эта команда обрабатывается на клиенте
                    break;
                    
                default:
                    ui.printError("Неизвестная команда: " + commandName);
                    return null;
            }
            
            return new CommandRequest(commandType, argument);
            
        } catch (NumberFormatException e) {
            ui.printError("Некорректный формат числа: " + e.getMessage());
            return null;
        } catch (IOException e) {
            ui.printError("Ошибка ввода: " + e.getMessage());
            return null;
        }
    }
}