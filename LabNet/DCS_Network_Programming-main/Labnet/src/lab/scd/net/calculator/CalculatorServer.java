package src.lab.scd.net.calculator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class CalculatorServer {
    public static void main(String[] args) {
        int port = 5050;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Calculator Server started on port " + port);
            while (true) {
                Socket socket = serverSocket.accept();
                new ClientHandler(socket).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler extends Thread {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String request = in.readLine(); // Format: num1,operator,num2
            if (request != null) {
                String[] parts = request.split(",");
                if (parts.length == 3) {
                    double num1 = Double.parseDouble(parts[0]);
                    String op = parts[1];
                    double num2 = Double.parseDouble(parts[2]);
                    double result = 0;
                    boolean valid = true;

                    switch (op) {
                        case "+": result = num1 + num2; break;
                        case "-": result = num1 - num2; break;
                        case "*": result = num1 * num2; break;
                        case "/":
                            if (num2 != 0) result = num1 / num2;
                            else valid = false;
                            break;
                        default: valid = false;
                    }

                    if (valid) {
                        out.println("Result: " + result);
                    } else {
                        out.println("Error: Division by zero or invalid op");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}