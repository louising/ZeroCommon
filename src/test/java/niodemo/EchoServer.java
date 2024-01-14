package niodemo;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class EchoServer {
    private static final String POISON_PILL = "POISON_PILL";

    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open(); //Selector: A multiplexor of SelectableChannel objects. 
        
        ServerSocketChannel serverSocket = ServerSocketChannel.open(); // ServerSocketChannel: A selectable channel for stream-oriented listening sockets. 
        serverSocket.bind(new InetSocketAddress("localhost", 5454)); //bind(): can accept connection
        serverSocket.configureBlocking(false);
        //2: 3: 4:29
        serverSocket.register(selector, SelectionKey.OP_ACCEPT); //1 READ 4 Write 8 Connect 16 Accept 
        System.out.println("ServerSocketChannel: " + serverSocket);
        ByteBuffer buffer = ByteBuffer.allocate(10);
        while (true) {
            selector.select(); // blocking operation
            Set<SelectionKey> selectedKeys = selector.selectedKeys(); // SelectionKey: A token representing the registration of a SelectableChannel with a Selector.
            Iterator<SelectionKey> iter = selectedKeys.iterator();
            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                //java.nio.channels.SocketChannel[connected local=/127.0.0.1:5454 remote=/127.0.0.1:51043] 1(read)
                //System.out.printf("SelectionKey Channel: %s  readyOps: %d \n", key.channel(), key.readyOps());
                if (key.isAcceptable()) {
                    //register(selector, serverSocket);
                    SocketChannel client = serverSocket.accept();
                    System.out.println("Get client connection: " + client + ";  " + client.hashCode());
                    client.configureBlocking(false);
                    client.register(selector, SelectionKey.OP_READ);
                }

                if (key.isReadable()) {
                    answerWithEcho(buffer, key);
                }
                iter.remove();
            }
        }
    }

    private static void answerWithEcho(ByteBuffer buffer, SelectionKey key) throws IOException {
        SocketChannel client = (SocketChannel) key.channel();
        int r = client.read(buffer);
        if (r == -1 || new String(buffer.array()).trim().equals(POISON_PILL)) {
            client.close();
            System.out.println("Not accepting client messages anymore " + r);
        } else {
            System.out.println("Read from client " + new String(buffer.array()));
            buffer.flip();
            client.write(buffer);
            buffer.clear();
        }
    }

    //Quit: process.destroy()
    public static Process start() throws IOException, InterruptedException {
        String javaHome = System.getProperty("java.home");
        String javaBin = javaHome + File.separator + "bin" + File.separator + "java";
        String classpath = System.getProperty("java.class.path");
        String className = EchoServer.class.getCanonicalName();

        ProcessBuilder builder = new ProcessBuilder(javaBin, "-cp", classpath, className);
        return builder.start();
    }
}