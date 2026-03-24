import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Calculator extends JFrame {
    JTextField display;
    JTextArea historyArea;
    ArrayList<String> historyList = new ArrayList<>();
    String expression = "";
    boolean historyVisible = false;
    JPanel mainPanel;
    JScrollPane scrollPane;

    public Calculator() {
        setTitle("Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 350);
        setLocationRelativeTo(null);
        setResizable(false);

        mainPanel = new JPanel(new BorderLayout(5, 5));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        display = new JTextField();
        display.setFont(new Font("Arial", Font.PLAIN, 20));
        display.setEditable(false);
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setText("0");
        mainPanel.add(display, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(4, 4, 3, 3));
        String[] buttons = {
            "7","8","9","/",
            "4","5","6","*",
            "1","2","3","-",
            "0",".","=","+"
        };

        for (String btn : buttons) {
            JButton button = new JButton(btn);
            button.setFont(new Font("Arial", Font.PLAIN, 16));
            buttonPanel.add(button);
            button.addActionListener(e -> handleButton(btn));
        }

        historyArea = new JTextArea(15, 15);
        historyArea.setFont(new Font("Arial", Font.PLAIN, 11));
        historyArea.setEditable(false);
        historyArea.setBackground(new Color(240, 240, 240));
        scrollPane = new JScrollPane(historyArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("History"));

        JButton clearButton = new JButton("C");
        clearButton.setFont(new Font("Arial", Font.PLAIN, 14));
        clearButton.setBackground(new Color(255, 100, 100));
        clearButton.setForeground(Color.WHITE);
        clearButton.addActionListener(e -> {expression = ""; display.setText("0");});

        JButton historyButton = new JButton("History");
        historyButton.setFont(new Font("Arial", Font.PLAIN, 14));
        historyButton.setBackground(new Color(100, 150, 255));
        historyButton.setForeground(Color.WHITE);
        historyButton.addActionListener(e -> toggleHistory());

        JPanel controlPanel = new JPanel(new GridLayout(1, 2, 3, 3));
        controlPanel.add(clearButton);
        controlPanel.add(historyButton);

        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
        centerPanel.add(buttonPanel, BorderLayout.CENTER);
        centerPanel.add(controlPanel, BorderLayout.SOUTH);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        add(mainPanel);
        setVisible(true);
    }

    void handleButton(String btn) {
        if (btn.equals("=")) {
            if (!expression.isEmpty()) {
                try {
                    double result = evaluateExpression(expression);
                    historyList.add(expression + " = " + result);
                    if (historyVisible) updateHistory();
                    display.setText(String.valueOf(result));
                    expression = String.valueOf(result);
                } catch (Exception ex) {
                    display.setText("Error");
                    expression = "";
                }
            }
        } else if (btn.matches("[+\\-*/]")) {
            if (!expression.isEmpty() && !expression.endsWith("+") && !expression.endsWith("-") 
                && !expression.endsWith("*") && !expression.endsWith("/")) {
                expression += " " + btn + " ";
                display.setText(expression);
            }
        } else if (btn.equals(".")) {
            if (!expression.isEmpty()) {
                String lastNum = expression.substring(expression.lastIndexOf(" ") + 1);
                if (!lastNum.contains(".")) {
                    expression += ".";
                    display.setText(expression);
                }
            }
        } else {
            expression = expression.equals("0") ? btn : expression + btn;
            display.setText(expression);
        }
    }

    void toggleHistory() {
        if (historyVisible) {
            mainPanel.remove(scrollPane);
            historyVisible = false;
            setSize(400, 350);
        } else {
            updateHistory();
            mainPanel.add(scrollPane, BorderLayout.EAST);
            historyVisible = true;
            setSize(600, 350);
        }
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    void updateHistory() {
        historyArea.setText("");
        for (String h : historyList) historyArea.append(h + "\n");
    }

    double evaluateExpression(String expr) {
        String[] tokens = expr.split(" ");
        double result = Double.parseDouble(tokens[0]);
        for (int i = 1; i < tokens.length; i += 2) {
            String op = tokens[i];
            double num = Double.parseDouble(tokens[i + 1]);
            if (op.equals("+")) result += num;
            else if (op.equals("-")) result -= num;
            else if (op.equals("*")) result *= num;
            else if (op.equals("/")) result /= num;
        }
        return result;
    }

    public static void main(String[] args) {
        new Calculator();
    }
}