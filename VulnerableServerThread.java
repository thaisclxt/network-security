import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.io.EOFException;
import java.net.SocketException;

public class VulnerableServerThread extends Thread {
    private Socket clientSocket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private boolean isAttached;

    public VulnerableServerThread(Socket socket) {
        this.clientSocket = socket;
        this.isAttached = false;
    }

    public void run() {
        try {
            outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            inputStream = new ObjectInputStream(clientSocket.getInputStream());

            while (true) {
                try {
                    // Attach phase (only once normally, but now accepting anything)
                    if (!isAttached) {
                        Object message = inputStream.readObject();
                        if (message instanceof ProtocolMessage) {
                            ProtocolMessage request = (ProtocolMessage) message;
                            System.out.println("Received Attach Request: " + request.getPayload());
                            isAttached = true;
                            ProtocolMessage response = new ProtocolMessage("ATTACH_ACCEPT", "Session established.");
                            outputStream.writeObject(response);
                            outputStream.flush();
                        } else {
                            System.out.println("Warning: Unexpected message instead of attach, ignoring.");
                            // Still continue, simulate vulnerability
                        }
                    }

                    // Always ready to receive operations
                    int operation = inputStream.readInt();
                    System.out.println("Operation selected: " + operation);

                    Calculator calculator = new Calculator();

                    if (operation == 1 || operation == 2 || operation == 3 || operation == 4) {
                        int count = inputStream.readInt();
                        System.out.println("Expecting " + count + " numbers.");
                        for (int i = 0; i < count; i++) {
                            double number = inputStream.readDouble();
                            calculator.addNumber(number);
                            System.out.println("Received number: " + number);
                        }
                    } else if (operation == 5 || operation == 6 || operation == 7) {
                        double number = inputStream.readDouble();
                        calculator.addNumber(number);
                        System.out.println("Received number: " + number);
                    } else if (operation == 0) {
                        System.out.println("Client requested to disconnect.");
                        break;
                    } else {
                        System.out.println("Unknown operation. Ignoring.");
                    }

                    double result = 0;

                    switch (operation) {
                        case 1:
                            result = calculator.add();
                            break;
                        case 2:
                            result = calculator.subtract();
                            break;
                        case 3:
                            result = calculator.multiply();
                            break;
                        case 4:
                            result = calculator.divide();
                            break;
                        case 5:
                            result = calculator.sqrt();
                            break;
                        case 6:
                            result = calculator.sin();
                            break;
                        case 7:
                            result = calculator.cos();
                            break;
                        default:
                            // Unknown operations result in 0.0
                            break;
                    }

                    System.out.println("Calculated result: " + result);
                    outputStream.writeDouble(result);
                    outputStream.flush();
                } catch (EOFException e) {
                    System.out.println("Client disconnected normally.");
                    break;
                } catch (SocketException e) {
                    System.out.println("Socket closed or connection reset.");
                    break;
                } catch (Exception e) {
                    System.out.println("Warning: Error processing client data: " + e.getMessage());
                    // Do not close connection, just continue waiting for new data
                }
            }

        } catch (Exception e) {
            System.out.println("Server error: " + e.getMessage());
        } finally {
            try {
                inputStream.close();
                outputStream.close();
                clientSocket.close();
                System.out.println("Closed client connection.");
            } catch (Exception e) {
                System.out.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}