import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.JOptionPane;

public class Client {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 4321);
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());

            // Step 1: Send Attach Request
            ProtocolMessage attachRequest = new ProtocolMessage("ATTACH_REQUEST", "UE_ID: device5678");
            outputStream.writeObject(attachRequest);
            outputStream.flush();

            // Step 2: Wait for Server Attach Response
            ProtocolMessage attachResponse = (ProtocolMessage) inputStream.readObject();

            if (attachResponse.getType().equals("ATTACH_ACCEPT")) {
                boolean keepRunning = true;

                while (keepRunning) {
                    // Step 3: Ask user to choose a math operation
                    String[] options = {"Addition", "Subtraction", "Multiplication", "Division", "Square Root", "Sine", "Cosine"};
                    int operation = JOptionPane.showOptionDialog(null, "Choose an operation", "Operation",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

                    if (operation == JOptionPane.CLOSED_OPTION) {
                        operation = -1; // User closed the window
                    }

                    if (operation == -1) {
                        // User closed or canceled: send exit code
                        outputStream.writeInt(0);
                        outputStream.flush();
                        keepRunning = false;
                        break;
                    }

                    // Step 4: Send selected operation to the server
                    outputStream.writeInt(operation + 1); // because server expects 1,2,3...
                    outputStream.flush();

                    if (operation >= 0 && operation <= 3) { // Addition, Subtraction, Multiplication, Division
                        String input = JOptionPane.showInputDialog("How many numbers?");
                        if (input == null || input.trim().isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Invalid input. Disconnecting.");
                            outputStream.writeInt(0);
                            outputStream.flush();
                            keepRunning = false;
                            break;
                        }
                        int quantity = Integer.parseInt(input);
                        outputStream.writeInt(quantity);
                        outputStream.flush();

                        for (int i = 0; i < quantity; i++) {
                            input = JOptionPane.showInputDialog("Enter number " + (i + 1) + ":");
                            if (input == null || input.trim().isEmpty()) {
                                JOptionPane.showMessageDialog(null, "Invalid input. Disconnecting.");
                                outputStream.writeInt(0);
                                outputStream.flush();
                                keepRunning = false;
                                break;
                            }
                            double number = Double.parseDouble(input);
                            outputStream.writeDouble(number);
                            outputStream.flush();
                        }
                    } else if (operation >= 4 && operation <= 6) { // Square Root, Sine, Cosine
                        String input = JOptionPane.showInputDialog("Enter the number:");
                        if (input == null || input.trim().isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Invalid input. Disconnecting.");
                            outputStream.writeInt(0);
                            outputStream.flush();
                            keepRunning = false;
                            break;
                        }
                        double number = Double.parseDouble(input);
                        outputStream.writeDouble(number);
                        outputStream.flush();
                    }

                    // Step 5: Receive and show the result
                    double result = inputStream.readDouble();
                    JOptionPane.showMessageDialog(null, "Result: " + result);

                    // Step 6: (No asking to continue — immediately go back to Step 3)
                }
            } else {
                JOptionPane.showMessageDialog(null, "Attach failed. Cannot proceed.");
            }

            inputStream.close();
            outputStream.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}