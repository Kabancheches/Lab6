package src.Server.Processors;

import Common.Commands.CommandRequest;
import Common.Commands.CommandResponse;
import Common.Commands.CommandType;
import src.Server.Managers.CollectionManager;
import src.Server.Managers.FileManager;
import src.Server.Model.Classes.Product;

import java.util.PriorityQueue;
import java.util.stream.Collectors;

/**
 * Процессор команд сервера.
 * Обрабатывает команды, полученные от клиентов.
 */
public class CommandProcessor {
    private final CollectionManager collectionManager;
    private final FileManager fileManager;
    
    public CommandProcessor(CollectionManager collectionManager, FileManager fileManager) {
        this.collectionManager = collectionManager;
        this.fileManager = fileManager;
    }
    
    /**
     * Обрабатывает команду и возвращает ответ.
     */
    public CommandResponse processCommand(CommandRequest request) {
        if (request == null) {
            return new CommandResponse(false, "Пустая команда");
        }
        
        CommandType commandType = request.getCommandType();
        Object argument = request.getArgument();
        
        try {
            switch (commandType) {
                case HELP:
                    return handleHelp();
                case INFO:
                    return handleInfo();
                case SHOW:
                    return handleShow();
                case ADD:
                    return handleAdd((Product) argument);
                case UPDATE:
                    return handleUpdate(request);
                case REMOVE_BY_ID:
                    return handleRemoveById((Long) argument);
                case CLEAR:
                    return handleClear();
                case SAVE:
                    return handleSave();
                case EXIT:
                    return handleExit();
                case ADD_IF_MIN:
                    return handleAddIfMin((Product) argument);
                case REMOVE_GREATER:
                    return handleRemoveGreater((Product) argument);
                case REMOVE_LOWER:
                    return handleRemoveLower((Product) argument);
                case FILTER_BY_PRICE:
                    return handleFilterByPrice((Float) argument);
                case FILTER_GREATER_THAN_PRICE:
                    return handleFilterGreaterThanPrice((Float) argument);
                default:
                    return new CommandResponse(false, "Неизвестная команда: " + commandType);
            }
        } catch (ClassCastException e) {
            return new CommandResponse(false, "Ошибка типа аргумента команды: " + e.getMessage());
        } catch (Exception e) {
            return new CommandResponse(false, "Ошибка выполнения команды: " + e.getMessage());
        }
    }
    
    private CommandResponse handleHelp() {
        StringBuilder helpText = new StringBuilder("Доступные команды:\n");
        helpText.append("  help - вывести справку по командам\n");
        helpText.append("  info - вывести информацию о коллекции\n");
        helpText.append("  show - вывести все элементы коллекции\n");
        helpText.append("  add {element} - добавить новый элемент\n");
        helpText.append("  update {id} {element} - обновить элемент по id\n");
        helpText.append("  remove_by_id {id} - удалить элемент по id\n");
        helpText.append("  clear - очистить коллекцию\n");
        helpText.append("  save - сохранить коллекцию в файл (только сервер)\n");
        helpText.append("  exit - завершить работу клиента\n");
        helpText.append("  add_if_min {element} - добавить если меньше минимального\n");
        helpText.append("  remove_greater {element} - удалить элементы больше заданного\n");
        helpText.append("  remove_lower {element} - удалить элементы меньше заданного\n");
        helpText.append("  filter_by_price {price} - фильтровать по цене\n");
        helpText.append("  filter_greater_than_price {price} - фильтровать по цене больше заданной\n");
        return new CommandResponse(true, helpText.toString());
    }
    
    private CommandResponse handleInfo() {
        StringBuilder info = new StringBuilder();
        info.append("Тип коллекции: PriorityQueue<Product>\n");
        info.append("Дата инициализации: ").append(collectionManager.getInitializationDate()).append("\n");
        info.append("Количество элементов: ").append(collectionManager.getCollection().size()).append("\n");
        return new CommandResponse(true, info.toString());
    }
    
    private CommandResponse handleShow() {
        PriorityQueue<Product> collection = collectionManager.getCollection();
        if (collection.isEmpty()) {
            return new CommandResponse(true, "Коллекция пуста");
        }
        
        // Используем Stream API для преобразования коллекции в строку
        String result = collection.stream()
                .map(Product::toString)
                .collect(Collectors.joining("\n"));
        
        return new CommandResponse(true, result, collection);
    }
    
    private CommandResponse handleAdd(Product product) {
        if (product == null) {
            return new CommandResponse(false, "Продукт не может быть null");
        }
        
        Long newId = collectionManager.getFirstNotUsedIdProduct();
        product.setId(newId);
        
        if (product.getManufacturer() != null) {
            product.getManufacturer().setId(collectionManager.getFirstNotUsedIdOrganization());
        }
        
        if (collectionManager.addProduct(product)) {
            return new CommandResponse(true, "Продукт успешно добавлен с ID: " + newId);
        } else {
            return new CommandResponse(false, "Не удалось добавить продукт");
        }
    }
    
    private CommandResponse handleUpdate(CommandRequest request) {
        // Аргумент - это массив Object[] где [0] = id, [1] = Product
        Object arg = request.getArgument();
        if (!(arg instanceof Object[])) {
            return new CommandResponse(false, "Неверный формат аргумента для update");
        }
        Object[] args = (Object[]) arg;
        if (args.length != 2) {
            return new CommandResponse(false, "Для команды update требуется 2 аргумента: id и Product");
        }
        
        Long id = (Long) args[0];
        Product newProduct = (Product) args[1];
        
        if (id == null || newProduct == null) {
            return new CommandResponse(false, "ID и продукт не могут быть null");
        }
        
        Product existing = collectionManager.getProductById(id);
        if (existing == null) {
            return new CommandResponse(false, "Элемент с ID " + id + " не найден");
        }
        
        newProduct.setId(id);
        collectionManager.removeById(id);
        
        if (newProduct.getManufacturer() != null && newProduct.getManufacturer().getId() == null) {
            newProduct.getManufacturer().setId(collectionManager.getFirstNotUsedIdOrganization());
        }
        
        if (collectionManager.addProduct(newProduct)) {
            return new CommandResponse(true, "Элемент с ID " + id + " успешно обновлен");
        } else {
            return new CommandResponse(false, "Не удалось обновить элемент");
        }
    }
    
    private CommandResponse handleRemoveById(Long id) {
        if (id == null) {
            return new CommandResponse(false, "ID не может быть null");
        }
        
        if (collectionManager.removeById(id)) {
            return new CommandResponse(true, "Элемент с ID " + id + " успешно удален");
        } else {
            return new CommandResponse(false, "Элемент с ID " + id + " не найден");
        }
    }
    
    private CommandResponse handleClear() {
        collectionManager.clear();
        return new CommandResponse(true, "Коллекция очищена");
    }
    
    private CommandResponse handleSave() {
        try {
            fileManager.saveCollection(collectionManager.getCollection());
            return new CommandResponse(true, "Коллекция сохранена в файл: " + fileManager.getFileName());
        } catch (Exception e) {
            return new CommandResponse(false, "Ошибка сохранения: " + e.getMessage());
        }
    }
    
    private CommandResponse handleExit() {
        // Эта команда обрабатывается на клиенте, сервер просто подтверждает
        return new CommandResponse(true, "Команда exit принята (обрабатывается на клиенте)");
    }
    
    private CommandResponse handleAddIfMin(Product product) {
        if (product == null) {
            return new CommandResponse(false, "Продукт не может быть null");
        }
        
        PriorityQueue<Product> collection = collectionManager.getCollection();
        
        // Используем Stream API для нахождения минимального элемента
        boolean isEmpty = collection.isEmpty();
        Product minProduct = collection.stream()
                .min(Product::compareTo)
                .orElse(null);
        
        if (isEmpty || product.compareTo(minProduct) < 0) {
            Long newId = collectionManager.getFirstNotUsedIdProduct();
            product.setId(newId);
            
            if (product.getManufacturer() != null) {
                product.getManufacturer().setId(collectionManager.getFirstNotUsedIdOrganization());
            }
            
            if (collectionManager.addProduct(product)) {
                return new CommandResponse(true, "Продукт добавлен (он меньше минимального). ID: " + newId);
            }
        }
        
        return new CommandResponse(false, "Продукт не добавлен (не меньше минимального)");
    }
    
    private CommandResponse handleRemoveGreater(Product product) {
        if (product == null) {
            return new CommandResponse(false, "Продукт не может быть null");
        }
        
        PriorityQueue<Product> collection = collectionManager.getCollection();
        
        // Используем Stream API для фильтрации и подсчета элементов
        long removedCount = collection.stream()
                .filter(p -> p.compareTo(product) > 0)
                .peek(p -> collectionManager.removeById(p.getId()))
                .count();
        
        return new CommandResponse(true, "Удалено элементов: " + removedCount);
    }
    
    private CommandResponse handleRemoveLower(Product product) {
        if (product == null) {
            return new CommandResponse(false, "Продукт не может быть null");
        }
        
        PriorityQueue<Product> collection = collectionManager.getCollection();
        
        // Создаем копию списка ID для удаления, чтобы избежать ConcurrentModificationException
        java.util.List<Long> idsToRemove = collection.stream()
                .filter(p -> p.compareTo(product) < 0)
                .map(Product::getId)
                .collect(Collectors.toList());
        
        for (Long id : idsToRemove) {
            collectionManager.removeById(id);
        }
        
        return new CommandResponse(true, "Удалено элементов: " + idsToRemove.size());
    }
    
    private CommandResponse handleFilterByPrice(Float price) {
        if (price == null) {
            return new CommandResponse(false, "Цена не может быть null");
        }
        
        PriorityQueue<Product> collection = collectionManager.getCollection();
        
        // Используем Stream API для фильтрации
        java.util.List<Product> filtered = collection.stream()
                .filter(p -> Float.compare(p.getPrice(), price) == 0)
                .collect(Collectors.toList());
        
        if (filtered.isEmpty()) {
            return new CommandResponse(true, "Продукты с ценой " + price + " не найдены");
        }
        
        String result = filtered.stream()
                .map(Product::toString)
                .collect(Collectors.joining("\n"));
        
        return new CommandResponse(true, result);
    }
    
    private CommandResponse handleFilterGreaterThanPrice(Float price) {
        if (price == null) {
            return new CommandResponse(false, "Цена не может быть null");
        }
        
        PriorityQueue<Product> collection = collectionManager.getCollection();
        
        // Используем Stream API для фильтрации
        java.util.List<Product> filtered = collection.stream()
                .filter(p -> p.getPrice() > price)
                .collect(Collectors.toList());
        
        if (filtered.isEmpty()) {
            return new CommandResponse(true, "Продукты с ценой больше " + price + " не найдены");
        }
        
        String result = filtered.stream()
                .map(Product::toString)
                .collect(Collectors.joining("\n"));
        
        return new CommandResponse(true, result);
    }
}
