package src.Client.Net;

import Common.Commands.CommandRequest;
import Common.Commands.CommandResponse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;

/**
 * Менеджер сетевого взаимодействия клиента.
 * Использует UDP (DatagramChannel) в неблокирующем режиме.
 */
public class ClientNetManager {
    private static final int PORT = 6778;
    private static final String SERVER_HOST = "localhost";
    private static final int TIMEOUT_MS = 5000; // Таймаут ожидания ответа
    
    private final DatagramChannel channel;
    private final Selector selector;
    private final InetSocketAddress serverAddress;
    
    public ClientNetManager() throws IOException {
        this.channel = DatagramChannel.open();
        this.channel.configureBlocking(false);
        this.selector = Selector.open();
        this.serverAddress = new InetSocketAddress(SERVER_HOST, PORT);
        this.channel.register(selector, SelectionKey.OP_READ);
    }
    
    /**
     * Сериализует объект команды в байтовый массив.
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
     * Десериализует объект из байтового массива.
     */
    private Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(data);
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            return ois.readObject();
        }
    }
    
    /**
     * Отправляет команду на сервер и получает ответ.
     * Корректно обрабатывает временную недоступность сервера.
     * 
     * @param request команда для отправки
     * @return ответ от сервера или null если сервер недоступен
     */
    public CommandResponse sendCommand(CommandRequest request) {
        try {
            byte[] data = serialize(request);
            ByteBuffer buffer = ByteBuffer.wrap(data);
            
            // Отправка датаграммы
            channel.send(buffer, serverAddress);
            
            // Ожидание ответа в неблокирующем режиме с таймаутом
            long startTime = System.currentTimeMillis();
            while (System.currentTimeMillis() - startTime < TIMEOUT_MS) {
                int readyChannels = selector.select(100); // Проверка каждые 100мс
                
                if (readyChannels > 0) {
                    Set<SelectionKey> selectedKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectedKeys.iterator();
                    
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        iterator.remove();
                        
                        if (key.isReadable()) {
                            ByteBuffer receiveBuffer = ByteBuffer.allocate(65535);
                            InetSocketAddress sender = (InetSocketAddress) channel.receive(receiveBuffer);
                            
                            if (sender != null) {
                                receiveBuffer.flip();
                                byte[] responseBytes = new byte[receiveBuffer.remaining()];
                                receiveBuffer.get(responseBytes);
                                
                                Object responseObj = deserialize(responseBytes);
                                if (responseObj instanceof CommandResponse) {
                                    return (CommandResponse) responseObj;
                                }
                            }
                        }
                    }
                }
            }
            
            // Таймаут - сервер недоступен
            System.out.println("[CLIENT] Сервер временно недоступен. Превышен таймаут ожидания ответа.");
            return null;
            
        } catch (IOException e) {
            System.out.println("[CLIENT] Ошибка сети: " + e.getMessage());
            return null;
        } catch (ClassNotFoundException e) {
            System.out.println("[CLIENT] Ошибка десериализации ответа: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Закрывает сетевой канал.
     */
    public void close() {
        try {
            selector.close();
            channel.close();
        } catch (IOException e) {
            System.err.println("[CLIENT] Ошибка при закрытии канала: " + e.getMessage());
        }
    }
}
