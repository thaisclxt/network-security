import java.io.*;
import java.net.*;
import javax.swing.JOptionPane;

class Client{
    public static void main(String args[]) throws IOException {
        Object[] operators = { "sum", "subtraction", "multiplication", "division", "square root", "sine", "cosine" };
        var message = "Enter the number:";
        String inputNumber;
        DataInputStream socketInput;
        DataOutputStream socketOutput;

        try {
            var s = new Socket();

            var ip = JOptionPane.showInputDialog("Enter the IP adress: ");
            var door = Integer.parseInt(JOptionPane.showInputDialog("Enter the port: "));

            var address = new InetSocketAddress(ip, door);
            s.connect(address, 1000);

            socketInput = new DataInputStream(s.getInputStream());
            socketOutput = new DataOutputStream(s.getOutputStream());

            while (true) {
                var selectedOperator = JOptionPane.showInputDialog(null, "Choose an operation:", "Input",
                        JOptionPane.INFORMATION_MESSAGE, null, operators, operators[0]);

                if (selectedOperator == null) {
                    System.out.println("Connection closed.");
                    socketOutput.writeInt(-1);
                    break;
                }

                for (var index = 0; index < operators.length; index++) {
                    if (selectedOperator.equals(operators[index])) {
                        socketOutput.writeInt(index);
                    }
                }

                if (selectedOperator.equals(operators[4]) || selectedOperator.equals(operators[5])
                        || selectedOperator.equals(operators[6])) {
                    inputNumber = JOptionPane.showInputDialog(message);
                    socketOutput.writeDouble(Double.parseDouble(inputNumber));
                } else {
                    for (var index = 0; index < 2; index++) {
                        inputNumber = JOptionPane.showInputDialog(message);
                        socketOutput.writeDouble(Double.parseDouble(inputNumber));
                    }
                }

                var msg = socketInput.readUTF();
                System.out.println("\nMessage received: \"" + msg + "\"");
            }

            s.close();

        } catch (IOException e) {
            System.out.println(e);
        }
    }
}

