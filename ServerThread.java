import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class ServerThread extends Thread {
    Socket socket;
    Double result;
    DataOutputStream socketOutput;
    DataInputStream socketInput;
    ArrayList<Double> numbers = new ArrayList<>();

    public ServerThread(Socket s) {
        super();
        this.socket = s;
    }

    @Override
    public void run() {
        try {
            socketOutput = new DataOutputStream(socket.getOutputStream());
            socketInput = new DataInputStream(socket.getInputStream());

            while (true) {
                var operation = socketInput.readInt();

                if (operation == -1) {
                    socketOutput.writeUTF("Server: Connection closed");
                    socket.close();
                    break;
                }

                if (operation == 4 || operation == 5 || operation == 6) {
                    var num = socketInput.readDouble();
                    numbers.add(num);
                } else {
                    for (var index = 0; index < 2; index++) {
                        var num = socketInput.readDouble();
                        numbers.add(num);
                    }
                }

                var calc = new Calculator(numbers);

                switch (operation) {
                    case 0:
                        result = calc.sum();
                        break;
                    case 1:
                        result = calc.subtraction();
                        break;
                    case 2:
                        result = calc.multiplication();
                        break;
                    case 3:
                        result = calc.division();
                        break;
                    case 4:
                        result = calc.squareRoot();
                        break;
                    case 5:
                        result = calc.sine();
                        break;
                    default:
                        result = calc.cosine();
                        break;
                }

                socketOutput.writeUTF("The result is: " + result);
                numbers.clear();
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}