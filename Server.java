import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class Server {
    public static void main(String args[]) {
        try (ServerSocket serverSocket = new ServerSocket(4321)) {
            System.out.println("Server started on port 4321.");
            System.out.println("Waiting for clients to connect...");

            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("New client connected.");

                    ServerThread serverThread = new ServerThread(clientSocket);
                    serverThread.start();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.out.println("Failed to open server socket: " + e);
        }
    }
}