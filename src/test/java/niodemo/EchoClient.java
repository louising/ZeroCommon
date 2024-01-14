package niodemo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class EchoClient {
    public static void main(String[] args) throws Exception {
        try (SocketChannel client = SocketChannel.open(new InetSocketAddress("localhost", 5454))) {
            sendMessage(client, "hello");        
            sendMessage(client, "world");
            sendMessage(client, "POISON_PILL");
        };
    }
    
    static String sendMessage(SocketChannel client, String msg) {
        ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
        String response = null;
        try {
            client.write(buffer);
            buffer.clear();
            client.read(buffer);
            response = new String(buffer.array()).trim();
            System.out.println("response=" + response);
            buffer.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}