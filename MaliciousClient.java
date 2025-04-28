import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MaliciousClient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 4321);
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());

            // Send a fake Attach Request with a forged device ID
            ProtocolMessage fakeAttach = new ProtocolMessage("ATTACH_REQUEST", "UE_ID: attacker9999");
            outputStream.writeObject(fakeAttach);
            outputStream.flush();

            // Try sending a math operation even if not properly attached
            outputStream.writeInt(1); // 1 = Addition
            outputStream.flush();
            
            // Send number of elements
            outputStream.writeInt(2); 
            outputStream.flush();

            // Send two numbers
            outputStream.writeDouble(5.0);
            outputStream.flush();
            outputStream.writeDouble(10.0);
            outputStream.flush();

            // Try to read the result
            double result = inputStream.readDouble();
            System.out.println("Malicious Result received: " + result);

            inputStream.close();
            outputStream.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}