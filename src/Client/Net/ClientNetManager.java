package Client.Net;

import Common.Net.CommandRequest;
import Common.Net.CommandResponse;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ClientNetManager {
    private final String host;
    private final int port;
    private final DatagramSocket socket;

    public ClientNetManager(String host, int port) throws IOException {
        this.host = host;
        this.port = port;
        this.socket = new DatagramSocket();
        socket.setSoTimeout(5000);
    }

    private byte[] serialize(Object obj) throws IOException {
        try (ByteArrayOutputStream bytes = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bytes)) {
            oos.writeObject(obj);
            return bytes.toByteArray();
        }
    }

    private Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream bytesOut = new ByteArrayInputStream(data);
             ObjectInputStream ois = new ObjectInputStream(bytesOut)) {
            return ois.readObject();
        }
    }

    public CommandResponse sendRequest(CommandRequest request) throws IOException, ClassNotFoundException {
        byte[] sendData = serialize(request);
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
                InetAddress.getByName(host), port);
        socket.send(sendPacket);

        byte[] receiveBuffer = new byte[65535];
        DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
        socket.receive(receivePacket);

        byte[] data = new byte[receivePacket.getLength()];
        System.arraycopy(receivePacket.getData(), 0, data, 0, data.length);
        return (CommandResponse) deserialize(data);
    }

    public void close() {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }
}