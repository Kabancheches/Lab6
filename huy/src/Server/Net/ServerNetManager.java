package src.Server.Net;

import Common.Commands.CommandRequest;
import Common.Commands.CommandResponse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;

/**
 * Менеджер сетевого взаимодействия сервера.
 * Использует UDP (DatagramChannel) в неблокирующем режиме.
 * Работает в однопоточном режиме с использованием Selector.
 */
public class ServerNetManager {
    private final int port;
    private final DatagramChannel channel;
    private final Selector selector;
    private final CommandProcessor commandProcessor;
    
    public ServerNetManager(int port, CommandProcessor commandProcessor) throws IOException {
        this.port = port;
        this.commandProcessor = commandProcessor;
        this.channel = DatagramChannel.open();
        this.channel.configureBlocking(false);
        this.channel.bind(new InetSocketAddress(port));
        this.selector = Selector.open();
        this.channel.register(selector, SelectionKey.OP_READ);
        
        System.out.println("[SERVER] Сервер запущен на порту " + port);
    }
    
    /**
     * Сериализует объект ответа в байтовый массив.
     */
    private byte[] serialize(Object obj) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(obj);
            oos.flush();
            return baos.toByteArray();
        }
    }
    
    /**
     * Десериализует объект команды из байтового массива.
     */
    private Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(data);
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            return ois.readObject();
        }
    }
    
    /**
     * Запускает сервер в однопоточном режиме.
     * Обрабатывает подключения и запросы клиентов.
     */
    public void start() throws IOException {
        System.out.println("[SERVER] Ожидание подключений...");
        
        while (true) {
            // Блокирующий вызов select, но каналы работают в неблокирующем режиме
            selector.select();
            
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectedKeys.iterator();
            
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                
                if (key.isReadable()) {
                    handleRead(key);
                }
            }
        }
    }
    
    /**
     * Обработка полученного запроса от клиента.
     */
    private void handleRead(SelectionKey key) {
        try {
            DatagramChannel clientChannel = (DatagramChannel) key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(65535);
            
            SocketAddress remoteAddress = clientChannel.receive(buffer);
            InetSocketAddress clientAddress = null;
            if (remoteAddress instanceof InetSocketAddress) {
                clientAddress = (InetSocketAddress) remoteAddress;
            }
            
            if (clientAddress != null) {
                buffer.flip();
                byte[] data = new byte[buffer.remaining()];
                buffer.get(data);
                
                // Десериализация команды
                Object requestObj = deserialize(data);
                
                if (requestObj instanceof CommandRequest) {
                    CommandRequest request = (CommandRequest) requestObj;
                    System.out.println("[SERVER] Получена команда: " + request.getCommandName());
                    
                    // Обработка команды
                    CommandResponse response = commandProcessor.processCommand(request);
                    
                    // Отправка ответа клиенту
                    sendResponse(clientChannel, clientAddress, response);
                } else {
                    System.out.println("[SERVER] Получен неизвестный объект: " + requestObj);
                }
            }
        } catch (IOException e) {
            System.err.println("[SERVER] Ошибка при чтении: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("[SERVER] Ошибка десериализации: " + e.getMessage());
        }
    }
    
    /**
     * Отправка ответа клиенту.
     */
    private void sendResponse(DatagramChannel channel, InetSocketAddress address, CommandResponse response) throws IOException {
        byte[] data = serialize(response);
        ByteBuffer buffer = ByteBuffer.wrap(data);
        channel.send(buffer, address);
        System.out.println("[SERVER] Ответ отправлен клиенту " + address);
    }
    
    /**
     * Закрывает серверный канал.
     */
    public void close() {
        try {
            selector.close();
            channel.close();
        } catch (IOException e) {
            System.err.println("[SERVER] Ошибка при закрытии канала: " + e.getMessage());
        }
    }
}
