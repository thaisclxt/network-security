import java.net.*;
import java.io.*;

class Server {
    public static void main(String args[]) {
        try (ServerSocket socket = new ServerSocket(4321)) {
            while (true) {
                try {
                    Socket s = socket.accept();
                    var serviceThread = new ServerThread(s);

                    serviceThread.start();

                } catch (IOException e) {
                    System.out.println(e);
                }
            }
        } catch (IOException e) {
            System.out.println("Failed to open server socket: " + e);
        }
    }
}