package Server.Net;

import Common.Net.CommandRequest;
import Common.Net.CommandResponse;
import Server.Managers.CommandManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ServerNetManager {
    private final int port;
    private final DatagramChannel channel;
    private final Selector selector;
    private final CommandManager commandManager;

    public ServerNetManager(int port, CommandManager commandProcessor) throws IOException {
        this.port = port;
        this.commandManager = commandProcessor;
        this.channel = DatagramChannel.open();
        this.channel.configureBlocking(false);
        this.channel.bind(new InetSocketAddress(InetAddress.getByName("0.0.0.0"), this.port));
        this.selector = Selector.open();
        this.channel.register(selector, SelectionKey.OP_READ);
        System.out.println("Порт сервера: " + port);
        System.out.print("IP сервера: ");
        printIpAddresses();
    }

    private byte[] serialize(Object obj) throws IOException {
        try (ByteArrayOutputStream bytes = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bytes)) {
            oos.writeObject(obj);
            oos.flush();
            return bytes.toByteArray();
        }
    }

    private Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream bytes = new ByteArrayInputStream(data);
             ObjectInputStream ois = new ObjectInputStream(bytes)) {
            return ois.readObject();
        }
    }

    public void start() throws IOException {
        System.out.println("Ожидание подключений...");

        while (true) {
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

                Object requestObj = deserialize(data);

                if (requestObj instanceof CommandRequest) {
                    CommandRequest request = (CommandRequest) requestObj;
                    System.out.println("Получена команда: " + request.getCommandName());
                    CommandResponse response = commandManager.dispatch(request);
                    sendResponse(clientChannel, clientAddress, response);
                } else {
                    System.out.println("Получен неизвестный объект: " + requestObj);
                    CommandResponse errorResponse = new CommandResponse(false, "Неизвестный тип запроса");
                    sendResponse(clientChannel, clientAddress, errorResponse);
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка при чтении: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Ошибка десериализации: " + e.getMessage());
        }
    }

    private void sendResponse(DatagramChannel channel, InetSocketAddress address, CommandResponse response) throws IOException {
        byte[] data = serialize(response);
        ByteBuffer buffer = ByteBuffer.wrap(data);
        channel.send(buffer, address);
        System.out.println("Ответ отправлен клиенту " + address + ": " + response.getMessage());
    }

    public static List<InetAddress> getNonLoopbackAddresses() {
        try {
            return NetworkInterface.networkInterfaces().flatMap(NetworkInterface::inetAddresses).filter(addr -> addr instanceof Inet4Address).collect(Collectors.toList());
        } catch (SocketException e) {
            System.err.println("Ошибка получения сетевых интерфейсов: " + e.getMessage());
            return List.of();
        }
    }

    public void printIpAddresses() {
        List<InetAddress> inetAddresses = getNonLoopbackAddresses();
        for (InetAddress inetAddress : inetAddresses) {
            System.out.print(inetAddress.getHostAddress() + "  ");
        }
        System.out.println();
    }
}