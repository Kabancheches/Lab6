package Server.Net;

import Common.Net.CommandRequest;
import Common.Net.CommandResponse;
import Common.Net.Serializer;

import java.io.IOException;

public class ServerNetManager {
    private final UdpServerChannel channel;
    private final CommandDispatcher commandDispatcher;

    public ServerNetManager(int port, CommandDispatcher commandDispatcher) throws IOException {
        this.channel = new UdpServerChannel(port);
        this.commandDispatcher = commandDispatcher;
    }

    public void start() throws IOException {
        channel.startListening((data, clientAddress) -> {
            try {
                Object obj = Serializer.deserialize(data);
                if (obj instanceof CommandRequest request) {
                    System.out.println("Получена команда: " + request.getCommandName());
                    CommandResponse response = commandDispatcher.dispatch(request);
                    byte[] responseData = Serializer.serialize(response);
                    channel.sendResponse(responseData, clientAddress);
                    System.out.println("Ответ отправлен " + clientAddress + ": " + response.getMessage());
                } else {
                    System.err.println("Неизвестный объект от " + clientAddress);
                    CommandResponse err = new CommandResponse(false, "Неизвестный тип запроса");
                    byte[] errData = Serializer.serialize(err);
                    channel.sendResponse(errData, clientAddress);
                }
            } catch (Exception e) {
                System.err.println("Ошибка обработки запроса: " + e.getMessage());
            }
        });
    }
}