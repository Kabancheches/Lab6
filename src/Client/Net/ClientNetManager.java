package Client.Net;

import Common.Net.CommandRequest;
import Common.Net.CommandResponse;
import Common.Net.Serializer;

import java.io.IOException;

public class ClientNetManager {
    private final UdpClientChannel channel;

    public ClientNetManager(String host, int port) throws IOException {
        this.channel = new UdpClientChannel(host, port);
    }

    public CommandResponse sendRequest(CommandRequest request) throws IOException, ClassNotFoundException {
        byte[] requestData = Serializer.serialize(request);
        byte[] responseData = channel.sendAndReceive(requestData);
        return (CommandResponse) Serializer.deserialize(responseData);
    }

    public void close() {
        channel.close();
    }
}