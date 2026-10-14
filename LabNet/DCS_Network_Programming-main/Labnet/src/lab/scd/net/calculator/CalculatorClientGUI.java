package src.lab.scd.net.calculator;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class CalculatorClientGUI extends JFrame {
    private JTextField num1Field, num2Field, resultField;

    public CalculatorClientGUI() {
        setTitle("Network Calculator Client");
        setSize(350, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 2, 10, 10));

        add(new JLabel(" Number 1:"));
        num1Field = new JTextField();
        add(num1Field);

        add(new JLabel(" Number 2:"));
        num2Field = new JTextField();
        add(num2Field);

        add(new JLabel(" Operation:"));
        JPanel panelButtons = new JPanel(new GridLayout(1, 4, 5, 5));
        JButton btnAdd = new JButton("+");
        JButton btnSub = new JButton("-");
        JButton btnMul = new JButton("*");
        JButton btnDiv = new JButton("/");

        panelButtons.add(btnAdd);
        panelButtons.add(btnSub);
        panelButtons.add(btnMul);
        panelButtons.add(btnDiv);
        add(panelButtons);

        add(new JLabel(" Result:"));
        resultField = new JTextField();
        resultField.setEditable(false);
        add(resultField);

        btnAdd.addActionListener(e -> sendCalculation("+"));
        btnSub.addActionListener(e -> sendCalculation("-"));
        btnMul.addActionListener(e -> sendCalculation("*"));
        btnDiv.addActionListener(e -> sendCalculation("/"));
    }

    private void sendCalculation(String op) {
        String n1 = num1Field.getText().trim();
        String n2 = num2Field.getText().trim();

        if (n1.isEmpty() || n2.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both numbers.");
            return;
        }

        try (
                Socket socket = new Socket("localhost", 5050);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            out.println(n1 + "," + op + "," + n2);
            String response = in.readLine();
            resultField.setText(response);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Connection Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CalculatorClientGUI().setVisible(true));
    }
}
