package Server.Net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;
import java.util.function.BiConsumer;

public class UdpServerChannel {
    private final DatagramChannel channel;
    private final Selector selector;
    private final int port;

    public UdpServerChannel(int port) throws IOException {
        this.port = port;
        this.channel = DatagramChannel.open();
        this.channel.configureBlocking(false);
        this.channel.bind(new InetSocketAddress("0.0.0.0", port));
        this.selector = Selector.open();
        this.channel.register(selector, SelectionKey.OP_READ);
    }

    public void startListening(BiConsumer<byte[], InetSocketAddress> onReceive) throws IOException {
        System.out.println("Сервер запущен на порту " + port);
        boolean flag = true;
        while (flag) {
            selector.select();
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> it = keys.iterator();
            while (it.hasNext()) {
                SelectionKey key = it.next();
                it.remove();
                if (key.isReadable()) {
                    DatagramChannel clientChannel = (DatagramChannel) key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(65000);
                    SocketAddress sender = clientChannel.receive(buffer);
                    if (sender instanceof InetSocketAddress) {
                        buffer.flip();
                        byte[] data = new byte[buffer.remaining()];
                        buffer.get(data);
                        onReceive.accept(data, (InetSocketAddress) sender);
                    }
                }
            }
        }
    }

    public void sendResponse(byte[] responseData, InetSocketAddress target) throws IOException {
        ByteBuffer buffer = ByteBuffer.wrap(responseData);
        channel.send(buffer, target);
    }
}