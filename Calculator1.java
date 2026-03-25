import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Stack;

public class Calculator1 extends JFrame {
    JTextField display;
    JTextArea historyArea;
    ArrayList<String> historyList = new ArrayList<>();
    String expression = "";
    boolean historyVisible = false;
    JPanel mainPanel;
    JScrollPane scrollPane;

    public Calculator1() {
        setTitle("Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
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

        JPanel buttonPanel = new JPanel(new GridLayout(5, 4, 3, 3));
        String[] buttons = {
            "C", "Del", "%", "History",
            "7", "8", "9", "/",
            "4", "5", "6", "*",
            "1", "2", "3", "-",
            "0", ".", "=", "+"
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

        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        add(mainPanel);
        setVisible(true);
    }

    void handleButton(String btn) {
        switch (btn) {
            case "=":
                if (!expression.isEmpty()) {
                    try {
                        double result = evaluateExpression(expression);
                        
                        if (Double.isInfinite(result) || Double.isNaN(result)) {
                            display.setText("Error");
                            expression = "";
                        } else {
                            historyList.add(expression + " = " + result);
                            if (historyVisible) updateHistory();
                            display.setText(String.valueOf(result));
                            expression = String.valueOf(result);
                        }
                    } catch (Exception ex) {
                        display.setText("Error");
                        expression = "";
                    }
                }
                break;
            case "C":
                expression = "";
                display.setText("0");
                break;
            case "Del":
                if (!expression.isEmpty()) {
                    expression = expression.substring(0, expression.length() - 1);
                    display.setText(expression.isEmpty() ? "0" : expression);
                }
                break;
            case "History":
                toggleHistory();
                break;
            case "%":
                if (!expression.isEmpty() && !expression.endsWith("+") && !expression.endsWith("-")
                        && !expression.endsWith("*") && !expression.endsWith("/") && !expression.endsWith("%")) {
                    expression += "%";
                    display.setText(expression);
                }
                break;
            default:
                if (btn.matches("[+\\-*/]")) {
                    if (!expression.isEmpty() && !expression.endsWith("+") && !expression.endsWith("-")
                            && !expression.endsWith("*") && !expression.endsWith("/") && !expression.endsWith("%")) {
                        expression += btn;
                        display.setText(expression);
                    } else if (expression.isEmpty() && btn.equals("-")) {
                        expression += btn;
                        display.setText(expression);
                    }
                } else if (btn.equals(".")) {
                    if (!expression.isEmpty()) {
                        String lastNum = expression.replaceAll("[+\\-*/%]", " ").trim();
                        String[] parts = lastNum.split(" ");
                        String lastPart = parts[parts.length - 1];
                        if (!lastPart.contains(".")) {
                            expression += ".";
                            display.setText(expression);
                        }
                    }
                } else {
                    expression = expression.equals("0") ? btn : expression + btn;
                    display.setText(expression);
                }
        }
    }

    void toggleHistory() {
        if (historyVisible) {
            mainPanel.remove(scrollPane);
            historyVisible = false;
            setSize(400, 400);
        } else {
            updateHistory();
            mainPanel.add(scrollPane, BorderLayout.EAST);
            historyVisible = true;
            setSize(600, 400);
        }
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    void updateHistory() {
        historyArea.setText("");
        for (String h : historyList) historyArea.append(h + "\n");
    }

    double evaluateExpression(String expr) {
        expr = expr.replaceAll("\\s+", ""); 
        
        expr = processPercentages(expr);
        
        ArrayList<String> tokens = tokenize(expr);
        ArrayList<String> postfix = infixToPostfix(tokens);
        return evaluatePostfix(postfix);
    }

    String processPercentages(String expr) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < expr.length(); i++) {
            if (i < expr.length() - 1 && expr.charAt(i + 1) == '%') {
                int j = i;
                while (j >= 0 && (Character.isDigit(expr.charAt(j)) || expr.charAt(j) == '.')) {
                    j--;
                }
                double num = Double.parseDouble(expr.substring(j + 1, i + 1));
                double percentage = num / 100;
                
                result.setLength(j + 1); 
                result.append(percentage);
                i++; 
            } else if (expr.charAt(i) != '%') {
                result.append(expr.charAt(i));
            }
        }
        return result.toString();
    }

    ArrayList<String> tokenize(String expr) {
        ArrayList<String> tokens = new ArrayList<>();
        StringBuilder num = new StringBuilder();

        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);
            if (Character.isDigit(c) || c == '.') {
                num.append(c);
            } else if ("+-*/".indexOf(c) >= 0) {
                if (num.length() > 0) {
                    tokens.add(num.toString());
                    num = new StringBuilder();
                }
                if (c == '-' && (tokens.isEmpty() || "+-*/".contains(tokens.get(tokens.size() - 1)))) {
                    num.append(c); 
                } else {
                    tokens.add(String.valueOf(c)); 
                }
            }
        }
        if (num.length() > 0) {
            tokens.add(num.toString());
        }
        return tokens;
    }

    ArrayList<String> infixToPostfix(ArrayList<String> tokens) {
        ArrayList<String> output = new ArrayList<>();
        Stack<String> operators = new Stack<>();

        for (String token : tokens) {
            if (isNumber(token)) {
                output.add(token);
            } else if ("+-*/".contains(token)) {
                while (!operators.isEmpty() && 
                       precedence(operators.peek()) >= precedence(token)) {
                    output.add(operators.pop());
                }
                operators.push(token);
            }
        }

        while (!operators.isEmpty()) {
            output.add(operators.pop());
        }

        return output;
    }

    double evaluatePostfix(ArrayList<String> postfix) {
        Stack<Double> stack = new Stack<>();

        for (String token : postfix) {
            if (isNumber(token)) {
                stack.push(Double.parseDouble(token));
            } else if ("+-*/".contains(token)) {
                double b = stack.pop();
                double a = stack.pop();
                double result = 0;
                switch (token) {
                    case "+":
                        result = a + b;
                        break;
                    case "-":
                        result = a - b;
                        break;
                    case "*":
                        result = a * b;
                        break;
                    case "/":
                        if (b == 0) {
                            throw new ArithmeticException("Division by zero");
                        }
                        result = a / b;
                        break;
                }
                stack.push(result);
            }
        }

        return stack.pop();
    }

    boolean isNumber(String token) {
        try {
            Double.parseDouble(token);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    int precedence(String op) {
        if (op.equals("+") || op.equals("-")) return 1;
        if (op.equals("*") || op.equals("/")) return 2;
        return 0;
    }

    public static void main(String[] args) {
        new Calculator1();
    }
}