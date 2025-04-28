import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MaliciousClientReplay {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 4321);
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());

            // Step 1: Send the first valid Attach Request
            ProtocolMessage firstAttach = new ProtocolMessage("ATTACH_REQUEST", "UE_ID: device5678");
            outputStream.writeObject(firstAttach);
            outputStream.flush();

            // Step 2: Wait for Attach Accept from the server
            ProtocolMessage attachResponse = (ProtocolMessage) inputStream.readObject();

            if (attachResponse.getType().equals("ATTACH_ACCEPT")) {
                System.out.println("First attach accepted by server.");

                // Step 3: Replay the Attach Request (send it again!)
                System.out.println("Replaying attach request...");
                outputStream.writeObject(firstAttach);
                outputStream.flush();

                // Step 4: Now send a math operation normally
                outputStream.writeInt(1); // 1 = Addition
                outputStream.flush();

                outputStream.writeInt(2); // number of numbers
                outputStream.flush();

                outputStream.writeDouble(5.0);
                outputStream.flush();
                outputStream.writeDouble(7.0);
                outputStream.flush();

                // Step 5: Try to read the result
                double result = inputStream.readDouble();
                System.out.println("Result after replaying attach: " + result);
            } else {
                System.out.println("Attach rejected. Cannot continue.");
            }

            inputStream.close();
            outputStream.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}