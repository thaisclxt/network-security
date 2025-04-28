import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.io.EOFException;

public class ServerThread extends Thread {
    private Socket clientSocket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private boolean isAttached;

    public ServerThread(Socket socket) {
        this.clientSocket = socket;
        this.isAttached = false;
    }

    public void run() {
        try {
            outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            inputStream = new ObjectInputStream(clientSocket.getInputStream());

            // Attach phase
            ProtocolMessage request = (ProtocolMessage) inputStream.readObject();

            if (request.getType().equals("ATTACH_REQUEST")) {
                System.out.println("Received Attach Request: " + request.getPayload());
                isAttached = true;
                ProtocolMessage response = new ProtocolMessage("ATTACH_ACCEPT", "Session established.");
                outputStream.writeObject(response);
                outputStream.flush();
            } else {
                System.out.println("Client failed to attach. Closing connection.");
                clientSocket.close();
                return;
            }

            // Math operations loop
            while (true) {
                int operation = inputStream.readInt();
                System.out.println("Operation selected: " + operation);

                // Exit signal
                if (operation == 0) {
                    System.out.println("Client requested to disconnect.");
                    break;
                }

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
                        System.out.println("Invalid operation.");
                        break;
                }

                System.out.println("Calculated result: " + result);
                System.out.println("Sending result to client...");

                outputStream.writeDouble(result);
                outputStream.flush();
            }

        } catch (EOFException e) {
            System.out.println("Client disconnected unexpectedly.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                outputStream.close();
                clientSocket.close();
                System.out.println("Closed client connection.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}