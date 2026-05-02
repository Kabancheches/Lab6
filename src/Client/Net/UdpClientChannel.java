package Client.Net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class UdpClientChannel {
    private final DatagramSocket socket;
    private final InetSocketAddress serverAddress;

    public UdpClientChannel(String host, int port) throws IOException {
        this.socket = new DatagramSocket();
        this.socket.setSoTimeout(5000);
        this.serverAddress = new InetSocketAddress(InetAddress.getByName(host), port);
    }

    public byte[] sendAndReceive(byte[] requestData) throws IOException {
        DatagramPacket sendPacket = new DatagramPacket(requestData, requestData.length, serverAddress);
        socket.send(sendPacket);

        byte[] buffer = new byte[65000];
        DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
        socket.receive(receivePacket);

        byte[] responseData = new byte[receivePacket.getLength()];
        System.arraycopy(receivePacket.getData(), 0, responseData, 0, responseData.length);
        return responseData;
    }

    public void close() {
        if (socket != null && !socket.isClosed()) socket.close();
    }
}